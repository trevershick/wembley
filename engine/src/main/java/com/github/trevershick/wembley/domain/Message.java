package com.github.trevershick.wembley.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.github.trevershick.wembley.api.PipelinePhase;
import com.github.trevershick.wembley.api.ProcessingState;

@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	String applicationId;
	

	@NotNull
	@Basic
	boolean testMode;
	
	@NotNull
	@Basic
	@Enumerated(EnumType.STRING)
	@Column(length=25)
	ProcessingState state = ProcessingState.Unprocessed;

	@Basic
	@Size(min=1,max=50)
	@Column(length=50)
	String templateName;
	
	int templateVersion;
	
	@Basic
	int retryCount = 0;
	
	@Basic
	int maxRetries = 0;
	
	@Basic
	@Size(min=1,max=64)
	@Column(length=64)
	String worker;
	
	PipelinePhase phase = PipelinePhase.Ingest;

	
	

	public PipelinePhase getPhase() {
		return phase;
	}


	public void setPhase(PipelinePhase phase) {
		this.phase = phase;
	}


	public Message(long id, String appId, boolean testMode, ProcessingState state,
			String templateName, int templateVersion, int retryCount, int maxRetries, String worker, PipelinePhase phase) {
		this.id = id;
		this.applicationId = appId;
		this.testMode = testMode;
		this.state = state;
		this.templateName = templateName;
		this.templateVersion = templateVersion;
		this.retryCount = retryCount;
		this.maxRetries = maxRetries;
		this.worker = worker;
		this.phase = phase;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	public ProcessingState getState() {
		return state;
	}

	public void setState(ProcessingState state) {
		this.state = state;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getTemplateVersion() {
		return templateVersion;
	}

	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}
	
	
	
}
