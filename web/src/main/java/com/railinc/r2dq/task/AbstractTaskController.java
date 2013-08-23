package com.railinc.r2dq.task;

import org.springframework.beans.factory.annotation.Required;

import com.railinc.r2dq.domain.views.TaskView;

public abstract class AbstractTaskController {
	private TaskService taskService;

	public TaskService getTaskService() {
		return taskService;
	}
	@Required
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
	
	
	
	
	
}
