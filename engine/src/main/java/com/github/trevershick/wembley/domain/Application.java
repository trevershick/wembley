package com.github.trevershick.wembley.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Application {
	
	public Application(String id, boolean testMode, boolean onHold, String successRrn,
			String failureRrn) {
		this.applicationId = id;
		this.testMode = testMode;
		this.onHold = onHold;
		this.successTopicRrn = successRrn;
		this.failureTopicRrn = failureRrn;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	public boolean isOnHold() {
		return onHold;
	}

	public void setOnHold(boolean onHold) {
		this.onHold = onHold;
	}

	public String getSuccessTopicRrn() {
		return successTopicRrn;
	}

	public void setSuccessTopicRrn(String successTopicRrn) {
		this.successTopicRrn = successTopicRrn;
	}

	public String getFailureTopicRrn() {
		return failureTopicRrn;
	}

	public void setFailureTopicRrn(String failureTopicRrn) {
		this.failureTopicRrn = failureTopicRrn;
	}

	@NotNull
	@Size(min=1,max=6)
	@Id
	String applicationId;
	
	@NotNull
	@Basic
	boolean testMode;
	
	@NotNull
	@Basic
	boolean onHold;
	
	
	@Basic
	String successTopicRrn;
	
	@Basic
	String failureTopicRrn;
	
}
