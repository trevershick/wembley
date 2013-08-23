package com.railinc.r2dq.log;

import java.util.Date;

import org.springframework.context.ApplicationEvent;


public abstract class SystemAuditLogEvent extends ApplicationEvent{
	private static final long serialVersionUID = 1L;
	private String action;
	private String entityName;
	private String entityId;
	private String details;
	
	public SystemAuditLogEvent(Object source,String action, String entityName, String entittyId) {
		super(source);
		this.action = action;
		this.entityName = entityName;
		this.entityId= entittyId;
	}
	
	public SystemAuditLogEvent(Object source,String action, String entityName, String entittyId, String details) {
		super(source);
		this.action = action;
		this.entityName = entityName;
		this.entityId= entittyId;
		this.details = details;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String enitityName) {
		this.entityName = enitityName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entittyId) {
		this.entityId = entittyId;
	}
	
	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public Date getEventDate() {
		return new Date(getTimestamp());
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
		.append("action:" + getAction())
		.append(" entityName:"+ getEntityName())
		.append(" entityId:"+ getEntityId())
		.append(" details:"+getDetails())
		.toString();
	}
	
	
}
