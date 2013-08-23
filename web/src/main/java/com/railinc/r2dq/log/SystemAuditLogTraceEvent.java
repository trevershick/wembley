package com.railinc.r2dq.log;

public class SystemAuditLogTraceEvent extends SystemAuditLogEvent {
	private static final long serialVersionUID = 1L;
	private String sourceEntityName;
	private String sourceEntityId;

	public SystemAuditLogTraceEvent(Object source, String action, String entityName, String entittyId, String details) {
		super(source, action, entityName, entittyId, details);
	}
	
	public SystemAuditLogTraceEvent(Object source, String action, String entityName, String entittyId,String sourceEntityName, String sourceEntityId, String details) {
		super(source, action, entityName, entittyId, details);
		this.sourceEntityName = sourceEntityName;
		this.sourceEntityId = sourceEntityId;
	}
	
	public String getSourceEntityName() {
		return sourceEntityName;
	}
	
	public String getSourceEntityId() {
		return sourceEntityId;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
		.append("action" + getAction())
		.append(" entityName:"+ getEntityName())
		.append(" entityId:"+ getEntityId())
		.append(" sourceEntityName:"+getSourceEntityName())
		.append(" sourceEntityId:"+getSourceEntityId())
		.append(" details:"+getDetails())
		.toString();
	}


}
