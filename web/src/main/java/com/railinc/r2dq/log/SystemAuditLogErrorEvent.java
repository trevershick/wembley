package com.railinc.r2dq.log;

public class SystemAuditLogErrorEvent extends SystemAuditLogEvent {
	private static final long serialVersionUID = 1L;
	private String cause;

	public SystemAuditLogErrorEvent(Object source, String action, String entityName, String entittyId) {
		super(source, action, entityName, entittyId);
	}
	
	public SystemAuditLogErrorEvent(Object source, String action, String entityName, String entittyId, String cause, String details) {
		super(source, action, entityName, entittyId, details);
		this.cause = cause;
	}
	
	public String getCause() {
		return cause;
	}
	
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
		.append("action:" + getAction())
		.append(" entityName:"+ getEntityName())
		.append(" entityId:"+ getEntityId())
		.append(" cause:"+getCause())
		.append(" details:"+getDetails())
		.toString();
	}

}
