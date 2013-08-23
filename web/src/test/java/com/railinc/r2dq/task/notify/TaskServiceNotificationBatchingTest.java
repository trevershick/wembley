package com.railinc.r2dq.task.notify;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.configuration.R2DQConfigurationService;
import com.railinc.r2dq.configuration.R2DQConfigurationServiceImpl;
import com.railinc.r2dq.correspondence.CorrespondenceService;
import com.railinc.r2dq.correspondence.SimpleContact;
import com.railinc.r2dq.dataexception.DataExceptionService;
import com.railinc.r2dq.dataexception.DataExceptionServiceImplTest;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.identity.IdentityService;
import com.railinc.r2dq.identity.contact.ContactInfoNotFoundException;
import com.railinc.r2dq.task.TaskServiceImpl;
import com.railinc.r2dq.util.PagedCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-r2dq-h2.xml", "classpath:spring-r2dq-hibernate.xml" })
@TransactionConfiguration(defaultRollback=true)
@Transactional
public class TaskServiceNotificationBatchingTest {
	@Autowired
	SessionFactory f;

	TaskServiceImpl s = new TaskServiceImpl();
	private DataExceptionService des;
	private IdentityService identityService;
	
	ApplicationContext applicationContext;

	private CorrespondenceService correspondenceService;

	private R2DQConfigurationService configurationService;

	@Before
	public void setup() {
		this.identityService = mock(IdentityService.class);
		this.des = mock(DataExceptionService.class);
		this.correspondenceService = mock(CorrespondenceService.class);
		this.configurationService = mock(R2DQConfigurationService.class);
		
		applicationContext = mock(ApplicationContext.class);
		s.setSessionFactory(f);
		s.setIdentityService(identityService);
		s.setDataExceptionService(des);
		s.setApplicationContext(applicationContext);
		s.setCorrespondenceService(correspondenceService);
		s.setConfigurationService(configurationService);
	}
	
	
	
	

	private void setupSsoIdAsCurrentIdentity(String string) {
		Identity id = new Identity(IdentityType.SsoId, string);
		when(this.identityService.getCurrentIdentities()).thenReturn(newArrayList(id));
		when(this.identityService.getMostSpecificSingularIdentity(anyCollection())).thenReturn(id);
		
	}



	private DataException createDataException() {
		DataExceptionServiceImplTest t = new DataExceptionServiceImplTest();
		long suffix = System.currentTimeMillis();
		DataException de = t.exceptionTemplate(suffix);
		de.setSource(t.createMessage(String.valueOf(suffix),f.getCurrentSession()));
		de.setSourceSystem(t.createSourceSystem(String.valueOf(suffix),f.getCurrentSession()));
		return de;
	}

	@Test
	public void test_task_notifs_are_bundled() throws ContactInfoNotFoundException {
		
		PagedCollection<TaskView> all = this.s.all(null);
		setupSsoIdAsCurrentIdentity(userID());
		DataException de1 = createDataException();
		de1.setImplementationType(ImplementationType.Automated);
		de1.setResponsiblePerson(user());

		DataException de2 = createDataException();
		de2.setImplementationType(ImplementationType.Automated);
		de2.setResponsiblePerson(user());

		DataException de3 = createDataException();
		de3.setImplementationType(ImplementationType.Automated);
		de3.setResponsiblePerson(user());
		
		Task task1 = s.createTaskFor(de1);
		Task task2 = s.createTaskFor(de2);
		Task task3 = s.createTaskFor(de3);
		
		s.save(task1);
		s.save(task2);
		s.save(task3);
		
		when(identityService.identitiesForPerson(user())).thenReturn(newArrayList(user()));
		when(identityService.contactInfo(user())).thenReturn(new SimpleContact("trever.shick@railinc.com"));
		when(configurationService.getMaximumTasksPerEmail()).thenReturn(R2DQConfigurationServiceImpl.MAXIMUM_TASKS_PER_EMAIL_DEFAULT);
		
		s.notifyUserOfTasks(user());
		ArgumentCaptor<YouHaveTasks> captor = ArgumentCaptor.forClass(YouHaveTasks.class);
		verify(correspondenceService, times(1)).convertAndSend(captor.capture());
		
		Collection<Task> tasks = (Collection<Task>) captor.getValue().data().get("tasks");
		assertEquals(3, tasks.size());
		
	}





	private Identity user() {
		return Identity.forSsoId(userID());
	}
	
	@Test
	public void notifications_split_by_max_1() throws ContactInfoNotFoundException {
		
		PagedCollection<TaskView> all = this.s.all(null);
		setupSsoIdAsCurrentIdentity(userID());
		DataException de1 = createDataException();
		de1.setImplementationType(ImplementationType.Automated);
		de1.setResponsiblePerson(user());

		DataException de2 = createDataException();
		de2.setImplementationType(ImplementationType.Automated);
		de2.setResponsiblePerson(user());

		DataException de3 = createDataException();
		de3.setImplementationType(ImplementationType.Automated);
		de3.setResponsiblePerson(user());
		
		Task task1 = s.createTaskFor(de1);
		Task task2 = s.createTaskFor(de2);
		Task task3 = s.createTaskFor(de3);
		
		s.save(task1);
		s.save(task2);
		s.save(task3);
		
		when(configurationService.getMaximumTasksPerEmail()).thenReturn(1);
		when(identityService.identitiesForPerson(user())).thenReturn(newArrayList(user()));
		when(identityService.contactInfo(user())).thenReturn(new SimpleContact("trever.shick@railinc.com"));

		
		s.notifyUserOfTasks(user());
		ArgumentCaptor<YouHaveTasks> captor = ArgumentCaptor.forClass(YouHaveTasks.class);
		
		// should be called three times because max is 1 and there are three tasks.
		verify(correspondenceService, times(3)).convertAndSend(captor.capture());
		
		Collection<Task> tasks = (Collection<Task>) captor.getValue().data().get("tasks");
		assertEquals("Should be ONE task per email", 1, tasks.size());
		
	}
	
	
	@Test
	public void notifications_split_by_max_2() throws ContactInfoNotFoundException {
		
		PagedCollection<TaskView> all = this.s.all(null);
		setupSsoIdAsCurrentIdentity(userID());
		DataException de1 = createDataException();
		de1.setImplementationType(ImplementationType.Automated);
		de1.setResponsiblePerson(user());

		DataException de2 = createDataException();
		de2.setImplementationType(ImplementationType.Automated);
		de2.setResponsiblePerson(user());

		DataException de3 = createDataException();
		de3.setImplementationType(ImplementationType.Automated);
		de3.setResponsiblePerson(user());
		
		Task task1 = s.createTaskFor(de1);
		Task task2 = s.createTaskFor(de2);
		Task task3 = s.createTaskFor(de3);
		
		s.save(task1);
		s.save(task2);
		s.save(task3);
		
		when(configurationService.getMaximumTasksPerEmail()).thenReturn(2);
		when(identityService.identitiesForPerson(user())).thenReturn(newArrayList(user()));
		when(identityService.contactInfo(user())).thenReturn(new SimpleContact("trever.shick@railinc.com"));

		
		s.notifyUserOfTasks(user());
		ArgumentCaptor<YouHaveTasks> captor = ArgumentCaptor.forClass(YouHaveTasks.class);
		
		// should be called three times because max is 1 and there are three tasks.
		verify(correspondenceService, times(2)).convertAndSend(captor.capture());
		
		List<YouHaveTasks> vs = captor.getAllValues();
		Collection<Task> tasks1 = (Collection<Task>) vs.get(0).data().get("tasks");
		Collection<Task> tasks2 = (Collection<Task>) vs.get(1).data().get("tasks");
		
		assertEquals("Should be TWO", 2, tasks1.size());
		assertEquals("Should be ONE", 1, tasks2.size());
		
	}





	private String userID() {
		return "jiminy";
	}
}
