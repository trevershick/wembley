package com.railinc.r2dq.util.bc;

import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

public class ExecutionTransformer {
	private boolean renderExceptions;
	private boolean renderSteps;

	public ExecutionTransformer() {
		
	}
	public ExecutionTransformer(boolean stepExecutions, boolean exceptions) {
		
		this.renderSteps = stepExecutions;
		this.renderExceptions = exceptions;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject apply(JobExecution e) {

		JSONObject o = new JSONObject();
		o.put("id", e.getId());
		o.put("jobId", e.getJobId());
		o.put("created",  String.valueOf(e.getCreateTime()));
		o.put("updated",  String.valueOf(e.getLastUpdated()));
		o.put("started", String.valueOf(e.getStartTime()));
		o.put("ended",  String.valueOf(e.getEndTime()));
		o.put("status", e.getStatus().toString());
		JSONObject exitStatus = new JSONObject();
		exitStatus.put("code", e.getExitStatus().getExitCode());
		exitStatus.put("description", e.getExitStatus().getExitDescription());
		o.put("exitStatus", exitStatus);
		
		ExecutionOperation availableCommand = ExecutionOperation.getAvailableCommand(e.getStatus());
		if (!availableCommand.isNoop()) {
			JSONArray a = new JSONArray();
			a.add(new JSONObject(availableCommand.asMap()));
			o.put("commands", a);
		}
		
		if (renderExceptions) {
			ThrowableTransformer throwableTransformer = new ThrowableTransformer();
			o.put("exceptions", throwableTransformer.apply(e.getFailureExceptions()));
		}
		if (renderSteps) {
			JSONArray a = new JSONArray();
			Collection<StepExecution> stepExecutions = e.getStepExecutions();
			StepExecutionTransformer xformer = new StepExecutionTransformer();
			for (StepExecution se : stepExecutions) {
				a.add(xformer.apply(se));
			}
			o.put("steps", a);
		}
		return o;
	}
}
