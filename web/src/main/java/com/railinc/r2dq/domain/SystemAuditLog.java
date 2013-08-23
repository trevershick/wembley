package com.railinc.r2dq.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Table(name="SYSTEM_AUDIT_LOG")
@Entity(name="systemAuditLog")
public class SystemAuditLog {
	
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_ACTION = "action";
	public static final String PROPERTY_ENTITYNAME = "entityName";
	public static final String PROPERTY_ENTITYID = "entityId";
	public static final String PROPERTY_SRCENTITYNAME = "sourceEntityName";
	public static final String PROPERTY_SRCENTITYID = "sourceEntityId";
	public static final String PROPERTY_CAUSE = "cause";
	public static final String PROPERTY_DETAILS = "details";
	public static final String PROPERTY_TYPE = "type";
	public static final String PROPERTY_CREATEDDATE = "createdDate";
	
	public static final int MAX_LENGTH_ACTION = 50;
	public static final int MAX_LENGTH_ENTITY_NAME = 255;
	public static final int MAX_LENGTH_ENTITY_ID = 50; 
	public static final int MAX_LENGTH_TYPE = 50;
	public static final int PROPERTY_DETAILS_MAX_LENGTH = 4000;
	public static final int PROPERTY_ERROR_CAUSE_MAX_LENGTH = 4000;
	
	@Id
	@SequenceGenerator(name="SYSTEM_AUDIT_LOG_SEQ", sequenceName="SYSTEM_AUDIT_LOG_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SYSTEM_AUDIT_LOG_SEQ")
    @Column(name="SYSTEM_AUDIT_LOG_ID")
	private Long id;
	
	@Basic(optional=false)
	@Column(name="ACTION_CODE", nullable=false, length=MAX_LENGTH_ACTION)
	private String action;
	
	@Basic(optional=false)
	@Column(name="ENTITY_NAME", nullable=false, length=MAX_LENGTH_ENTITY_NAME)
	private String entityName;
	
	@Basic(optional=false)
	@Column(name="ENTITY_CODE", nullable=false, length=MAX_LENGTH_ENTITY_ID)
	private String entityId;
	
	@Basic(optional=true)
	@Column(name="SOURCE_ENTITY_NAME", nullable=true, length=MAX_LENGTH_ENTITY_NAME)
	private String sourceEntityName;
	
	@Basic(optional=true)
	@Column(name="SOURCE_ENTITY_CODE", nullable=true, length=MAX_LENGTH_ENTITY_ID)
	private String sourceEntityId;
	
	@Basic(optional=true)
	@Column(name="ERROR_CAUSE_TEXT", length= PROPERTY_ERROR_CAUSE_MAX_LENGTH, nullable=true)
	private String cause;
	
	@Basic(optional=true)
	@Column(name="DETAILS_TEXT", length=PROPERTY_DETAILS_MAX_LENGTH)
	private String details;
	
	public static enum AuditLogType{
		TRACE, ERROR
	}
	
	@Basic(optional=false)
	@Column(name="AUDIT_LOG_TYPE", length=MAX_LENGTH_TYPE, nullable=false)
	@Enumerated(EnumType.STRING)
	private AuditLogType type;
	
	@Column(name="CREATED_TS", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		if(StringUtils.isNotBlank(cause) && cause.length() > PROPERTY_ERROR_CAUSE_MAX_LENGTH){
			cause = cause.substring(0, PROPERTY_ERROR_CAUSE_MAX_LENGTH);
		}
		this.cause = cause;
	}
	
	public String getSourceEntityName() {
		return sourceEntityName;
	}

	public void setSourceEntityName(String sourceEntityName) {
		this.sourceEntityName = sourceEntityName;
	}

	public String getSourceEntityId() {
		return sourceEntityId;
	}

	public void setSourceEntityId(String sourceEntityId) {
		this.sourceEntityId = sourceEntityId;
	}

	public AuditLogType getType() {
		return type;
	}

	public void setType(AuditLogType type) {
		this.type = type;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		if(StringUtils.isNotBlank(details)&& details.length() > PROPERTY_DETAILS_MAX_LENGTH){
			details = details.substring(0, PROPERTY_DETAILS_MAX_LENGTH);
		}
		this.details = details;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	

}
