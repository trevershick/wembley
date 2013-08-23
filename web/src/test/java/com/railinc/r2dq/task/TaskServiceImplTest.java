package com.railinc.r2dq.task;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.railinc.r2dq.dataexception.DataExceptionService;
import com.railinc.r2dq.dataexception.DataExceptionServiceImplTest;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.DataExceptionBundle;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.domain.tasks.ExceptionRemediationTask;
import com.railinc.r2dq.domain.tasks.ManualRemediationTask;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.identity.IdentityService;
import com.railinc.r2dq.log.SystemAuditLogTraceEvent;
import com.railinc.r2dq.sourcesystem.SourceSystemService;
import com.railinc.r2dq.task.exceptionremediation.ExceptionRemediationTaskCompletion;
import com.railinc.r2dq.task.manualremediation.ManualRemediationTaskCompletion;
import com.railinc.r2dq.util.PagedCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-r2dq-h2.xml", "classpath:spring-r2dq-hibernate.xml" })
@TransactionConfiguration
@Transactional
public class TaskServiceImplTest {
	@Autowired
	SessionFactory f;

	TaskServiceImpl s = new TaskServiceImpl();
	private DataExceptionService dataExceptionService;
	private IdentityService identityService;
	private SourceSystemService sourceSystemService;
	
	ApplicationContext applicationContext;

	@Before
	public void setup() {
		this.identityService = mock(IdentityService.class);
		this.dataExceptionService = mock(DataExceptionService.class);
		applicationContext = mock(ApplicationContext.class);
		sourceSystemService = mock(SourceSystemService.class);
		s.setSessionFactory(f);
		s.setIdentityService(identityService);
		s.setDataExceptionService(dataExceptionService);
		s.setApplicationContext(applicationContext);
		s.setSourceSystemService(sourceSystemService);
	}
	
	
	
	
	@Test
	public void test_search_free_text() {
		DataException de1 = createDataException();
		de1.setImplementationType(ImplementationType.Manual);
		de1.setMdmAttributeValue("thevalue1");

		DataException de1a = createDataException();
		de1a.setImplementationType(ImplementationType.Manual);
		de1a.setMdmAttributeValue("thevalue1");
		

		Task one = s.createTaskFor(de1);
		de1a.setTask(one);
		one.addDataException(de1a);
		s.save(one);
		
		
		DataException de2 = createDataException();
		de2.setImplementationType(ImplementationType.Manual);
		de2.setMdmAttributeValue("thevalue2");
		Task two = s.createTaskFor(de2);
		s.save(two);
		
		

		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		TaskCriteria c = new TaskCriteria();
		c.setFreeText("thevalue1");
		
		PagedCollection<TaskView> all = s.all(c);
		assertEquals( 1, all.size() );
	}
	
	
	@Test
	public void testCompleteTaskManualRemediationTaskCompletion() {
		setupSsoIdAsCurrentIdentity("sdtxs01");
		DataException de1 = createDataException();
		de1.setImplementationType(ImplementationType.Manual);

		Task task = s.createTaskFor(de1);
		s.save(task);
		assertTrue(task instanceof ManualRemediationTask);
		
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		
		assertEquals("CREATE_TASK", systemAuditLog.getValue().getAction());
		assertEquals(task.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(task.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(de1.getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(de1.getId().toString(), systemAuditLog.getValue().getSourceEntityId());
		
		ManualRemediationTaskCompletion command = new ManualRemediationTaskCompletion();
		command.addComment(de1.getId(), "Comment 1");
		command.approveMdmValue(de1.getId());
		command.setTaskId(task.getId());
		s.completeTask(command);
		
		verify(dataExceptionService).approve(de1, "Comment 1");
		 systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		
		assertEquals("MANUAL_COMPLETE_TASK", systemAuditLog.getValue().getAction());
		assertEquals(task.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(task.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(de1.getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(de1.getId().toString(), systemAuditLog.getValue().getSourceEntityId());

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
	public void testCompleteTaskExceptionRemediationTaskCompletion() {
		setupSsoIdAsCurrentIdentity("sdtxs01");
		DataException de1 = createDataException();
		de1.setImplementationType(ImplementationType.Automated);

		Task task = s.createTaskFor(de1);
		s.save(task);
		assertTrue(task instanceof ExceptionRemediationTask);
		
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		
		assertEquals("CREATE_TASK", systemAuditLog.getValue().getAction());
		assertEquals(task.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(task.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(de1.getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(de1.getId().toString(), systemAuditLog.getValue().getSourceEntityId());
		
		ExceptionRemediationTaskCompletion command = new ExceptionRemediationTaskCompletion();
		command.addComment(de1.getId(), "Comment 1");
		command.approveMdmValue(de1.getId());
		command.setTaskId(task.getId());
		s.completeTask(command);
		
		verify(dataExceptionService).approve(de1, "Comment 1");
		
		systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		
		assertEquals("COMPLETE_TASK", systemAuditLog.getValue().getAction());
		assertEquals(task.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(task.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(de1.getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(de1.getId().toString(), systemAuditLog.getValue().getSourceEntityId());
	}
	
	@Test
	public void testCreateTask_withDataExceptionBundleHasNoExceptionsAvailabe_doNothing(){
		DataExceptionBundle dataExceptionBundle = mock(DataExceptionBundle.class);
		when(dataExceptionBundle.getSourceSystem()).thenReturn(new SourceSystem());
		s.createTask(dataExceptionBundle);
		verify(dataExceptionService, times(1)).get(dataExceptionBundle);
		//verify(s, never()).save(any(Task.class));
	}
	
	@Test
	public void testCreateTask_withDataExceptionBundleHasOneManualDataException_doCreateManualTask(){
		DataExceptionBundle dataExceptionBundle = mock(DataExceptionBundle.class);
		List<DataException> dataExceptions = new ArrayList<DataException>();
		DataException dataException = createDataException(); 
		dataException.setImplementationType(ImplementationType.Manual);
		dataExceptions.add(dataException);
		when(dataExceptionBundle.getSourceSystem()).thenReturn(new SourceSystem());
		when(dataExceptionService.get(dataExceptionBundle)).thenReturn(dataExceptions);
		s.createTask(dataExceptionBundle);
		verify(dataExceptionService, times(1)).get(dataExceptionBundle);
	}
	
	@Test
	public void testCreateTask_withDataExceptionBundleHasTwoManualDataException_doCreateManualTask(){
		DataExceptionBundle dataExceptionBundle = mock(DataExceptionBundle.class);
		List<DataException> dataExceptions = new ArrayList<DataException>();
		DataException dataException1 = createDataException(); 
		dataException1.setImplementationType(ImplementationType.Manual);
		dataExceptions.add(dataException1);
		
		DataException dataException2 = createDataException(); 
		dataException2.setImplementationType(ImplementationType.Manual);
		dataExceptions.add(dataException2);
		when(dataExceptionBundle.getSourceSystem()).thenReturn(new SourceSystem());
		when(dataExceptionService.get(dataExceptionBundle)).thenReturn(dataExceptions);
		s.createTask(dataExceptionBundle);
		verify(dataExceptionService, times(1)).get(dataExceptionBundle);
	}
}
