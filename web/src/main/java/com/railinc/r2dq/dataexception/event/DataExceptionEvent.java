package com.railinc.r2dq.dataexception.event;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

public abstract class DataExceptionEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private Long mdmExceptionId;
	
	public DataExceptionEvent(Object source, Long mdmExceptionId) {
		super(source);
		this.mdmExceptionId = mdmExceptionId;
	}
	
	public Long getMdmExceptionId() {
		return mdmExceptionId;
	}
	
	public void setMdmExceptionId(Long mdmExceptionId) {
		this.mdmExceptionId = mdmExceptionId;
	}

	public Date getEventDate() {
		return new Date(getTimestamp());
	}
	

}
