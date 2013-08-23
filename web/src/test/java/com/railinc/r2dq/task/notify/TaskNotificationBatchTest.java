package com.railinc.r2dq.task.notify;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.tasks.ManualRemediationTask;
import com.railinc.r2dq.identity.IdentityService;
import com.railinc.r2dq.identity.contact.ContactInfoNotFoundException;
import com.railinc.r2dq.task.TaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"/spring-test-r2dq-h2.xml", /* h2 data source*/
		"/spring-test-r2dq-mock-identityservice.xml",
		"/spring-test-r2dq-mock-taskservice.xml",
		"/spring-test-r2dq-batch.xml", /* core batch stuff */
		
		"/spring-r2dq-hibernate.xml", /* hibernate and tx manager */
		"/spring-r2dq-job-task-notification.xml"
})
public class TaskNotificationBatchTest {
	@Autowired
	DataSource h2;
	
	@Autowired
	JobRegistry registry;
	
	@Autowired
	JobOperator operator;
	
	@Autowired
	JobExplorer explorer;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	PlatformTransactionManager tm;

	@Autowired
	IdentityService identityServiceMock;
	
	@Autowired
	TaskService taskServiceMock;

	@Before
	public void resetMocks() {
		reset(taskServiceMock, identityServiceMock);
		
	}
	@BeforeClass
	public static void start() throws SQLException {
//		server = Server.createWebServer(new String[] { "-trace" }).start();
	}
	@AfterClass
	public static void stop() {
//		server.stop();
	}
	@Before
	public void deleteRows() {
		Session s = this.sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		s.createQuery("delete from Task").executeUpdate();
		tx.commit();
		s.close();
	}
	@Test
	public void test_no_tasks() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {

		createTask(Identity.forSsoId("sdtxs01"), true, new Date()); // this task has been notified on 'Date()'

		// can start the job
		Long executionId = operator.start("job-task-notif", "x=" + System.currentTimeMillis());
		JobExecution exec = explorer.getJobExecution(executionId);

		
		assertCompleted(exec);
		
		
	}
	private void assertCompleted(JobExecution exec) {
		Collection<StepExecution> steps = exec.getStepExecutions();
		for (StepExecution se : steps) {
			List<Throwable> failureExceptions = se.getFailureExceptions();
			for (Throwable t:failureExceptions) {
				t.printStackTrace();
			}
			assertEquals("step " + se.getStepName() + " completed", ExitStatus.COMPLETED.getExitCode(),se.getExitStatus().getExitCode());
		}
		assertEquals(ExitStatus.COMPLETED,  exec.getExitStatus());
		
	}
	@Test
	public void test_two_tasks() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {

		createTask(Identity.forSsoId("sdtxs01"), true, new Date()); // this task has been notified on 'Date()'
		createTask(Identity.forSsoId("sdtxs01"), true, null); // this task has NOT been notified (null);
		createTask(Identity.forSsoId("sdtxs01"), true, null); // this task has NOT been notified (null);

		
		// can start the job
		Long executionId = operator.start("job-task-notif", "RUNDATE=" + System.currentTimeMillis());
		JobExecution exec = explorer.getJobExecution(executionId);

		
		Collection<StepExecution> steps = exec.getStepExecutions();
		assertEquals("Should be showing two unnotified tasks", 2, getLast(steps, null).getReadCount());
		assertCompleted(exec);
		
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_puts_identities_into_exec_context() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {
		// we're not testing the identity service's functionality, we're testing here
		// that the batch job calls the identity service and puts the result
		// into the job context.
		when(this.identityServiceMock.resolveToSingularIdentities(anyCollection())).thenAnswer(new Answer<Collection<Identity>>() {
			@Override
			public Collection<Identity> answer(InvocationOnMock invocation) throws Throwable {
				return newArrayList(Identity.forSsoId("sdtxs01"));
			}
		});
		
		// create two tasks for the reader in step 1 to find that have not been notified.
		createTask(Identity.forSsoId("sdtxs01"), true, null); // this task has NOT been notified (null);
		createTask(Identity.forSsoId("sdtxs01"), true, null); // this task has NOT been notified (null);

		// start the job
		Long executionId = operator.start("job-task-notif", "RUNDATE=" + System.currentTimeMillis());
		JobExecution exec = explorer.getJobExecution(executionId);

		assertCompleted(exec);
		
		// get the_ids from the context. these are the raw ids for the tasks above.
		// there will be one for each task.
		@SuppressWarnings("unchecked")

		Collection<StepExecution> steps = exec.getStepExecutions();
		assertEquals("Should be showing two unassigned tasks", 2, getLast(steps, null).getReadCount());
		//assertEquals("The ids should have been 'distinct'ed :) ", 1, ids.size());
		
		// the single id should have been 'singularized' to just the one id
//		ids = (Collection<Identity>) exec.getExecutionContext().get("singular_ids");
//		assertEquals("The ids should have been 'distinct'ed :) and singularized through the ident service", 1, ids.size());
	}
	
	
	@Test
	public void test_removes_redundant_and_calls_taskservice() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException, ContactInfoNotFoundException {
		when(this.identityServiceMock.resolveToSingularIdentities(anyCollection())).thenAnswer(new Answer<Collection<Identity>>() {
			@Override
			public Collection<Identity> answer(InvocationOnMock invocation) throws Throwable {
				return newArrayList(Identity.forSsoId("sdtxs01"));
			}
		});
		createTask(Identity.forSsoId("sdtxs01"), true, null); // this task has NOT been notified (null);
		createTask(Identity.forExternalEmailUser("trever.shick@railinc.com"), true, null); // this task has NOT been notified (null);

		// can start the job
		Long executionId = operator.start("job-task-notif", "RUNDATE=" + System.currentTimeMillis());
		JobExecution exec = explorer.getJobExecution(executionId);

		assertCompleted(exec);
		// verify that the 'resolveToSingular' method is called
		verify(identityServiceMock).resolveToSingularIdentities(anyCollection());
		verify(taskServiceMock).notifyUserOfTasks(Identity.forSsoId("sdtxs01"));
	}
	


	
	
	


	private void createTask(final Identity candidate, final boolean claimed, final Date notifSent) {
		TransactionTemplate tt = new TransactionTemplate(this.tm);
		tt.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				ManualRemediationTask t = new ManualRemediationTask();
				t.setCandidate(candidate);
				t.setNotificationSent(notifSent);
				if (claimed) {
					t.claim(candidate);
				}
				Session s = sessionFactory.getCurrentSession();
				s.save(t);
			}
		});
	}
}
