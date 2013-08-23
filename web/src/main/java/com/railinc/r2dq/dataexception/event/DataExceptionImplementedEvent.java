package com.railinc.r2dq.dataexception.event;


public class DataExceptionImplementedEvent extends DataExceptionEvent {
	private static final long serialVersionUID = -7721780185197320452L;

	public DataExceptionImplementedEvent(Object source, Long mdmExceptionId) {
		super(source, mdmExceptionId);
	}

	

}
