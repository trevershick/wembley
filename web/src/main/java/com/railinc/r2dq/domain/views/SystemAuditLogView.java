package com.railinc.r2dq.domain.views;

import java.util.Date;

public class SystemAuditLogView implements Comparable<SystemAuditLogView> {
	
	public static final int ERROR_CAUSE_MAX_LENGTH = 2000;
	public static final int ERROR_DATA_MAX_LENGTH = 2500;
	private Long id;
	private String action;
	private String entityName;
	private String entityId;
	private String sourceEntityName;
	private String sourceEntityId;
	private String cause;
	private String details;
	private Date createdDate;
	private com.railinc.r2dq.domain.SystemAuditLog.AuditLogType type;
	

	public void setAction(String action) {
		this.action = action;
	}
	public String getAction() {
		return action;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setSourceEntityName(String sourceEntityName) {
		this.sourceEntityName = sourceEntityName;
	}
	public String getSourceEntityName() {
		return sourceEntityName;
	}
	public void setSourceEntityId(String sourceEntityId) {
		this.sourceEntityId = sourceEntityId;
	}
	public String getSourceEntityId() {
		return sourceEntityId;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getCause() {
		return cause;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getDetails() {
		return details;
	}
	public void setType(com.railinc.r2dq.domain.SystemAuditLog.AuditLogType auditLogType) {
		this.type = auditLogType;
	}
	public com.railinc.r2dq.domain.SystemAuditLog.AuditLogType getType() {
		return type;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public boolean isLogSource() {
		return (null == this.getSourceEntityName() && null == this.getSourceEntityId());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SystemAuditLogView other = (SystemAuditLogView) obj;
		if (id == null && other.id != null || id != null && other.id == null) {
			return false;
		} else if (action == null && other.action != null || action != null && other.action == null) {	
			return false;	
		} else if (entityId == null && other.entityId != null || entityId != null && other.entityId == null) {
			return false;	
		} else if (entityName == null && other.entityName != null || entityName != null && other.entityName == null) {
			return false;
		} else if (sourceEntityId == null && other.sourceEntityId != null || sourceEntityId != null && other.sourceEntityId == null) {
			return false;
		} else if (sourceEntityName == null && other.sourceEntityName != null || sourceEntityName != null && other.sourceEntityName == null) {
			return false;
		} else if (cause == null && other.cause != null || cause != null && other.cause == null) {
			return false;
		} else if (type == null && other.type != null || type != null && other.type == null) {
			return false;
		} else if (details == null && other.details != null || details != null && other.details == null) {
			return false;
		} else if (createdDate == null && other.createdDate != null || createdDate != null && other.createdDate == null) {
			return false;
		} else if (null != id && null != other.id && !id.equals(other.id)) {
			return false;
		} else if (null != action && null != other.action && !action.equals(other.action)) {
			return false;
		} else if (null != entityId && null != other.entityId && !entityId.equals(other.entityId)) {
			return false;
		} else if (null != entityName && null != other.entityName && !entityName.equals(other.entityName)) {
			return false;
		} else if (null != sourceEntityId && null != other.sourceEntityId && !sourceEntityId.equals(other.sourceEntityId)) {
			return false;
		} else if (null != sourceEntityName && null != other.sourceEntityName && !sourceEntityName.equals(other.sourceEntityName)) {
			return false;
		} else if (null != cause && null != other.cause && !cause.equals(other.cause)) {
			return false;
		} else if (null != type && null != other.type && !type.equals(other.type)) {
			return false;
		} else if (null != details && null != other.details && !details.equals(other.details)) {
			return false;
		} else if (null != createdDate && null != other.createdDate && !createdDate.equals(other.createdDate)) {
			return false;
		}
		return true;
	}
	@Override
	public int compareTo(SystemAuditLogView arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
