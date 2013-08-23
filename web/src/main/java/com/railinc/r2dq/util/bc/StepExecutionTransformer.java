package com.railinc.r2dq.util.bc;

import org.json.simple.JSONObject;
import org.springframework.batch.core.StepExecution;

public class StepExecutionTransformer {

	public StepExecutionTransformer() {
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject apply(StepExecution e) {

		JSONObject o = new JSONObject();
		o.put("id", e.getId());
		o.put("jobExecutionId",e.getJobExecutionId());
		o.put("name", e.getStepName());
		o.put("summary",e.getSummary());

		o.put("commitCount", e.getCommitCount());
		o.put("filterCount", e.getFilterCount());
		o.put("processSkipCount", e.getProcessSkipCount());
		o.put("readCount",e.getReadCount());
		o.put("readSkipCount", e.getReadSkipCount());
		o.put("rollbackCount", e.getRollbackCount());
		o.put("skipCount", e.getSkipCount());
		o.put("writeCount", e.getWriteCount());
		o.put("writeSkipCount", e.getWriteSkipCount());
		

		o.put("started",  String.valueOf(e.getStartTime()));
		o.put("ended",  String.valueOf(e.getEndTime()));
		o.put("status", e.getStatus().toString());
		JSONObject exitStatus = new JSONObject();
		exitStatus.put("code", e.getExitStatus().getExitCode());
		exitStatus.put("description", e.getExitStatus().getExitDescription());
		o.put("exitStatus", exitStatus);
		o.put("updated",  String.valueOf(e.getLastUpdated()));

			ThrowableTransformer throwableTransformer = new ThrowableTransformer();
			o.put("exceptions", throwableTransformer.apply(e.getFailureExceptions()));

		return o;
	}
}
