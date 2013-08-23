package com.railinc.r2dq.domain.views;

import com.google.common.base.Function;
import com.railinc.r2dq.dataexception.DataExceptionService;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.tasks.Task;

public class TaskToView implements Function<Task, TaskView> {

	private boolean convertExceptions = false;
	private DataExceptionService dataExceptionService;
	
	public TaskToView() {}
	public TaskToView(DataExceptionService service) {
		this.dataExceptionService = service;
	}
	
	public void setDataExceptionService(DataExceptionService dataExceptionService) {
		this.dataExceptionService = dataExceptionService;
	}
	public TaskToView convertExceptions() {
		this.convertExceptions = true;
		return this;
	}

	@Override
	public TaskView apply(Task input) {
		TaskView v = new TaskView();
		v.setAssigned(input.isClaimed());
		v.setWho(input.isClaimed() ? input.getClaimant() : input.getCandidate());
		v.setDescription(input.getTaskDescription());
		v.setDone(input.isDone());
		v.setNotified(input.getNotificationSent());
		if (input.isDone()) {
			v.setDoneDate(input.getDispositionDate());
		}

		v.setDue(input.getDue());
		v.setId(input.getId());
		v.setCreated(input.getAuditData().getCreated());
		v.setName(input.getTaskName());
		v.setType(input.getClass().getSimpleName());
		if (convertExceptions) {
			DataExceptionToView xf = new DataExceptionToView();
			for (DataException de : input.getExceptions()) {
				v.add(xf.apply(de));
			}
		}
		return v;
	}

	public TaskToView copy() {
		TaskToView t = new TaskToView(dataExceptionService);
		t.convertExceptions = this.convertExceptions;
		return t;
	}

}
