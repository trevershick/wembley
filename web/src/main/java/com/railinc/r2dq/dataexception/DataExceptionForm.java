package com.railinc.r2dq.dataexception;

import static org.apache.commons.lang.StringUtils.trimToNull;

import java.util.Collection;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.railinc.r2dq.domain.ApprovalDisposition;
import com.railinc.r2dq.domain.AuditData;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.ImplementationDisposition;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.SourceSystem;

public class DataExceptionForm {

	public static final String DEFAULT_FORM_NAME = "dataexception";

	private AuditData auditData = new AuditData();
	
    //	private Task task;
	private String bundleName;
	private String description;

	private Date exceptionCreated;
	private boolean deleted;
	

	@NotNull
	private Long id;
	
	private String mdmAttributevalue;
	private String mdmObjectAttribute;
	private String mdmObjectType;
	private String person;

	@NotNull
	private IdentityType personType;
	
	private Long taskId;

	
	private Long ruleNumber;

private InboundMessage source;
	

	
	
	private SourceSystem sourceSystem;

	private String sourceSystemKeyColumn;

	@Size(min=1,max=50)
    private String sourceSystemKey;

	@Size(max=1024)
    private String sourceSystemObjectData; // assumed wrong

	// TODO - make these numbers constnats in data exception
	@Size(min=1,max=50)
	private String sourceSystemValue; // assumed wrong

	@Size(min=DataException.MIN_LENGTH_USER_COMMENT,max=DataException.MAX_LENGTH_USER_COMMENT)
	private String userComment;

	private ImplementationType implementationType;

	private ImplementationDisposition implementationDisposition;

	private ApprovalDisposition approvalDisposition;

	private Collection<String> personTypes;

	private Collection<String> implementationDispositions;

	private Collection<String> implementationTypes;

	private Collection<String> approvalDispositions;

	private Collection<SourceSystem> sourceSystems;

	public DataExceptionForm() {
		id = 0L;
	}

	public AuditData getAuditData() {
		return auditData;
	}

	public String getBundleName() {
		return bundleName;
	}

	public String getDescription() {
		return description;
	}

	public Date getExceptionCreated() {
		return exceptionCreated;
	}

	public Long getId() {
		return id;
	}

	public String getMdmAttributevalue() {
		return mdmAttributevalue;
	}

	public String getMdmObjectAttribute() {
		return mdmObjectAttribute;
	}

	public String getMdmObjectType() {
		return mdmObjectType;
	}

	public String getPerson() {
		return person;
	}

	public IdentityType getPersonType() {
		return personType;
	}

	public Long getRuleNumber() {
		return ruleNumber;
	}

	public InboundMessage getSource() {
		return source;
	}

	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	public String getSourceSystemKeyColumn() {
		return sourceSystemKeyColumn;
	}

	public String getSourceSystemKey() {
		return sourceSystemKey;
	}

	public String getSourceSystemObjectData() {
		return sourceSystemObjectData;
	}

    public String getSourceSystemValue() {
		return sourceSystemValue;
	}

	public void setAuditData(AuditData auditData) {
		this.auditData = (AuditData) auditData.clone();
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	

	public void setDescription(String description) {
		this.description = description;
	}

	public void setExceptionCreated(Date exceptionCreated) {
		this.exceptionCreated = exceptionCreated;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMdmAttributevalue(String mdmAttributevalue) {
		this.mdmAttributevalue = mdmAttributevalue;
	}
	




	public void setMdmObjectAttribute(String mdmObjectAttribute) {
		this.mdmObjectAttribute = mdmObjectAttribute;
	}

	public void setMdmObjectType(String mdmObjectType) {
		this.mdmObjectType = mdmObjectType;
	}

	public void setPerson(String person) {
		this.person = trimToNull(person);
	}

	public void setPersonType(IdentityType personType) {
		this.personType = personType;
	}

	public void setRuleNumber(Long ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public void setSource(InboundMessage source) {
		this.source = source;
	}



	
	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	
	public void setSourceSystemKeyColumn(String sourceSystemKeyColumn) {
		this.sourceSystemKeyColumn = sourceSystemKeyColumn;
	}


	public void setSourceSystemKey(String v) {
		this.sourceSystemKey = v;
	}

	public void setSourceSystemObjectData(String sourceSystemObjectData) {
		this.sourceSystemObjectData = sourceSystemObjectData;
	}

	public void setSourceSystemValue(String sourceSystemValue) {
		this.sourceSystemValue = sourceSystemValue;
	}
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public void setUserComment(String userComment) {
		this.userComment = trimToNull(userComment);
	}

	public String getUserComment() {
		return userComment;
	}

	public void setApprovalDisposition(ApprovalDisposition value) {
		this.approvalDisposition = value;
	}

	public void setImplementationDisposition(ImplementationDisposition value) {
		this.implementationDisposition = value;
	}

	public void setImplementationType(ImplementationType value) {
		this.implementationType = value;
	}

	public ImplementationType getImplementationType() {
		return implementationType;
	}

	public ImplementationDisposition getImplementationDisposition() {
		return implementationDisposition;
	}

	public ApprovalDisposition getApprovalDisposition() {
		return approvalDisposition;
	}

	public void setSourceSystems(Collection<SourceSystem> sourceSystems) {
		this.sourceSystems = sourceSystems;
	}

	public void setApprovalDispositions(Collection<String> approvalDispositions) {
		this.approvalDispositions = approvalDispositions;
	}

	public void setImplementationTypes(Collection<String> implementationTypes) {
		this.implementationTypes = implementationTypes;
	}

	public void setImplementationDispositions(Collection<String> implementationDispositions) {
		this.implementationDispositions = implementationDispositions;
	}

	public void setPersonTypes(Collection<String> personTypes) {
		this.personTypes = personTypes;
	}

	public Collection<String> getPersonTypes() {
		return personTypes;
	}

	public Collection<String> getImplementationDispositions() {
		return implementationDispositions;
	}

	public Collection<String> getImplementationTypes() {
		return implementationTypes;
	}

	public Collection<String> getApprovalDispositions() {
		return approvalDispositions;
	}

	public Collection<SourceSystem> getSourceSystems() {
		return sourceSystems;
	}
	

}
