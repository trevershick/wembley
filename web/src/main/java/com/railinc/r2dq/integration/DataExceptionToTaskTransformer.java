package com.railinc.r2dq.integration;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.task.TaskService;



public class DataExceptionToTaskTransformer implements Function<DataException, Task> {

	private TaskService taskService;
	
	// TODO - i don't think we can do this like this.
	// what task type to be created is determined by a variety of factors.
	@Override
	public Task apply(DataException dataException) {
		return taskService.createTaskFor(dataException);
	}
	
	// TODO - this should NOT be here
	public DataException updateResponsiblePerson(DataException dataException, Identity identity) {
		dataException.setResponsiblePerson(identity);
		return dataException;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	@Required
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

}
