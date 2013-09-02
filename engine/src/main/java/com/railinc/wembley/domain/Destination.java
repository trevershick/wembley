package com.railinc.wembley.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.railinc.wembley.api.Intent;
import com.railinc.wembley.api.ProcessingState;

@Entity
public class Destination {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	
	
	Long messageId;
	
	@NotNull
	@Basic
	@Column(length=256,nullable=false)
	String parentRrn;
	

	@Basic
	@Column(length=256,nullable=true)
	String rrn;
	
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length=25)
	private Intent intent = Intent.Email;

	
	@Enumerated(EnumType.STRING)
	@Column(length=25)
	ProcessingState status = ProcessingState.Unprocessed;
	
	
	
	public Destination(long id, long messageId, String parentRrn, String rrn,
			Intent intent, ProcessingState state) {
		this.id = id;
		this.messageId = messageId;
		this.parentRrn = parentRrn;
		this.rrn = rrn;
		this.intent = intent;
		this.status = state;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}



	public Long getMessageId() {
		return messageId;
	}


	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}


	public String getParentRrn() {
		return parentRrn;
	}


	public void setParentRrn(String parentRrn) {
		this.parentRrn = parentRrn;
	}


	public String getRrn() {
		return rrn;
	}


	public void setRrn(String rrn) {
		this.rrn = rrn;
	}


	public Intent getIntent() {
		return intent;
	}


	public void setIntent(Intent intent) {
		this.intent = intent;
	}


	public ProcessingState getStatus() {
		return status;
	}


	public void setStatus(ProcessingState status) {
		this.status = status;
	}


	
	
}
