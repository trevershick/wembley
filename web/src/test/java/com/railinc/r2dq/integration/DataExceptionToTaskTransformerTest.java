package com.railinc.r2dq.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.task.TaskService;

public class DataExceptionToTaskTransformerTest {
	
	@Test
	public void testApply(){
		Identity responsiblePerson = new Identity(IdentityType.LocalGroup, "Abc1");
		DataException dataException = new DataException();
		dataException.setResponsiblePerson(responsiblePerson);
		
		TaskService taskService = mock(TaskService.class);
		
		
		DataExceptionToTaskTransformer transformer = new DataExceptionToTaskTransformer();
		transformer.setTaskService(taskService);
		Task task = transformer.apply(dataException);
		
		verify(taskService).createTaskFor(dataException);
	}
	
	@Test
	public void testUpdateResponsiblePerson(){
		Identity responsiblePerson = new Identity(IdentityType.LocalGroup, "Abc1");
		DataException dataException = new DataException();
		DataExceptionToTaskTransformer transformer = new DataExceptionToTaskTransformer();
		dataException = transformer.updateResponsiblePerson(dataException, responsiblePerson);
		assertEquals(responsiblePerson, dataException.getResponsiblePerson());
	}
	

}
