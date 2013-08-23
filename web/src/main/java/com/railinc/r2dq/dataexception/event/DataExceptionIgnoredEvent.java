package com.railinc.r2dq.dataexception.event;


public class DataExceptionIgnoredEvent extends DataExceptionEvent {
	private static final long serialVersionUID = 1L;
	
	public DataExceptionIgnoredEvent(Object source, Long mdmExceptionId) {
		super(source, mdmExceptionId);
	}

}
