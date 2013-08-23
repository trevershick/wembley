package com.railinc.r2dq.dataexception.event;


public class DataExceptionRejectedEvent extends DataExceptionEvent {
	
	private static final long serialVersionUID = 1L;

	public DataExceptionRejectedEvent(Object source, Long mdmExceptionId){
		super(source, mdmExceptionId);
	}
	
	

}
