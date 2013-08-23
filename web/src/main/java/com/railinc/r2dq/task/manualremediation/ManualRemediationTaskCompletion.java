package com.railinc.r2dq.task.manualremediation;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import java.util.Collection;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.google.gson.Gson;

public class ManualRemediationTaskCompletion {
	@NotNull
	private Long taskId;

	private final Map<Long,String> userComments = newHashMap();

	private final Map<Long,Resolution> resolutions = newHashMap();
	
	enum Resolution {
		Approve,Disapprove,Ignore;
	}
	
	public void setTaskId(Long taskIdToPerform) {
		this.taskId = taskIdToPerform;
	}

	public void approveMdmValue(long exceptionId) {
		this.resolutions.put(exceptionId,  Resolution.Approve);
	}

	public void disapproveMdmValue(long exceptionId) {
		this.resolutions.put(exceptionId, Resolution.Disapprove);
		
	}

	public void ignoreMdmException(long exceptionId) {
		this.resolutions.put(exceptionId, Resolution.Ignore);
		
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

	public Long getTaskId() {
		return this.taskId;
	}

	public Collection<Long> getExceptionIds() {
		return newArrayList(resolutions.keySet());
	}

	public boolean wasIgnored(Long id) {
		return Resolution.Ignore.equals(resolutions.get(id));
	}	
	public boolean wasApproved(Long id) {
		return Resolution.Approve.equals(resolutions.get(id));
	}	
	public boolean wasDisapproved(Long id) {
		return Resolution.Disapprove.equals(resolutions.get(id));
	}
	
	
	public void addComment(long exceptionId, String comment) {
		if (isNotBlank(comment)) {
			this.userComments.put(exceptionId, trimToEmpty(comment));
		}
	}

	public boolean hasComment(Long id) {
		return userComments.containsKey(id);
	}

	public String getComment(Long id) {
		return this.userComments.get(id);
	}	

	
}
