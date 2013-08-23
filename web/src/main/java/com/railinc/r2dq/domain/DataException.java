package com.railinc.r2dq.domain;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.trimToNull;

import java.util.Date;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;

import com.google.common.base.Optional;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.integration.FlowEntity;
import com.railinc.r2dq.integration.FlowEntityId;
import com.railinc.r2dq.integration.FromJson;
import com.railinc.r2dq.integration.ToJson;
import com.railinc.r2dq.util.GsonUtil;

@Table(name="DATA_EXCEPTION")
@Entity
@FlowEntity

public class DataException implements SoftDelete, Auditable {



	public static final int MAX_LENGTH_MDM_VALUE = 255;
	public static final String PROPERTY_EXCEPTION_CREATED = "exceptionCreated";
	@Basic(optional=false)
	@Column(name="EXCPTN_CREATE_TS",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date exceptionCreated = new Date();

	
	
	public static final String PROPERTY_SOURCE_SYSTEM = "sourceSystem";
	@ManyToOne(optional=false)
	@JoinColumn(name="SOURCE_SYSTEM_CODE", nullable=false)
	@NotNull
	SourceSystem sourceSystem;
	
	
	public static final String PROPERTY_SOURCE_SYSTEM_KEY = "sourceSystemKey";
	@Basic
	@Column(name="SOURCE_SYSTEM_ROW_KEY",length=1000,nullable=false)
	@NotNull
	@Size(min=1,max=1000)
    private String sourceSystemKey;

	public static final String PROPERTY_SOURCE_SYSTEM_KEY_COL = "sourceSystemKeyColumn";
	@Basic
	@Column(name="SOURCE_SYSTEM_COL_NAME",length=100,nullable=false)
	@NotNull
	@Size(min=1,max=100)
    private String sourceSystemKeyColumn;


	@Basic
	@Column(name="SOURCE_SYSTEM_VALUE",length=1000,nullable=true)
	@Size(min=1,max=1000)
	private String sourceSystemValue; // assumed wrong
	public static final String PROPERTY_SOURCE_SYSTEM_VALUE = "sourceSystemValue";

	@Basic
	@Column(name="SOURCE_SYSTEM_INFO",length=1024,nullable=true)
	@Size(max=1024)
    private String sourceSystemObjectData; // assumed wrong
	public static final String PROPERTY_SOURCE_SYSTEM_OBJ_DATA = "sourceSystemObjectData";
	

	public static final String PROPERTY_RULE_NUMBER = "ruleNumber";
	@NotNull
	@Basic(optional=false)
	@Column(name="EXCPTN_CODE",nullable=false)
	private Long ruleNumber;

	public static final String PROPERTY_EXCPTION_DESCRIPTION = "description";
	@Size(min=0,max=1024)
	@Basic(optional=true)
	@Column(name="EXCPTN_DESCR",nullable=true,length=1024)
	private String description;

	public static final String PROPERTY_MDM_OBJECT_TYPE = "mdmObjectType";
	@Size(min=1,max=50)
	@NotNull
	@Basic(optional=false)
	@Column(name="MDM_EXCPTN_TYPE",nullable=false,length=50)
	private String mdmObjectType;

	public static final String PROPERTY_MDM_OBJECT_ATTRIBUTE = "mdmObjectAttribute";
	@Size(min=1,max=100)
	@NotNull
	@Basic(optional=false)
	@Column(name="MDM_EXCPTN_COL_NAME",nullable=false,length=100)
	private String mdmObjectAttribute;
	
	public static final String PROPERTY_MDM_VALUE = "mdmAttributeValue";
	@Size(min=1,max=255)
	@Basic(optional=true)
	@Column(name="MDM_VALUE",nullable=true,length=MAX_LENGTH_MDM_VALUE)
	private String mdmAttributeValue;
	

	public static final String PROPERTY_USER_COMMENT = "userComment";
	public static final int MAX_LENGTH_USER_COMMENT = 1024;
	public static final int MIN_LENGTH_USER_COMMENT = 1;
	public static final boolean NULLABLE_USER_COMMENT = true;
	@Size(min=MIN_LENGTH_USER_COMMENT,max=MAX_LENGTH_USER_COMMENT)
	@Basic(optional=NULLABLE_USER_COMMENT)
	@Column(name="USER_COMMENT",nullable=NULLABLE_USER_COMMENT,length=MAX_LENGTH_USER_COMMENT)
	private String userComment;
	
	
	
	public String getSourceSystemKey() {
		return sourceSystemKey;
	}

	public void setSourceSystemKey(String sourceSystemKey) {
		this.sourceSystemKey = sourceSystemKey;
	}

	// ------- r2dq specific
	@Version
    @Column(name="VERSION")
    private Integer version;
		
	public static final String PROPERTY_ID = "id";
	/**
	 * Internal Identifier for the r2dq system.
	 */
	@Id
	@SequenceGenerator(name="DATA_EXCEPTION_SEQ", sequenceName="DATA_EXCEPTION_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="DATA_EXCEPTION_SEQ")
	@Column(name="DATA_EXCEPTION_ID")
	@FlowEntityId
	private Long id;

	
	public static final String PROPERTY_MDM_EXCEPTION_ID = "mdmExceptionId";
	/**
	 * This is the exception id within the MDM system.  
	 */
	@Basic(optional=false)
	@Column(name="MDM_EXCPTN_ID",nullable=false)
	private Long mdmExceptionId;

	
	public static final String PROPERTY_INBOUND_MESSAGE = "source";
	@ManyToOne(optional=false)
	@NotNull
	@JoinColumn(name="MESSAGE_ID", nullable=false)
	private InboundMessage source;

	public static final String PROPERTY_RESPONSIBLE_PERSON_TYPE = "responsiblePerson." + Identity.PROPERTY_TYPE;
	public static final String PROPERTY_RESPONSIBLE_PERSON_ID = "responsiblePerson." + Identity.PROPERTY_ID;
	/**
	 * so, we need to determine who is reponsibile for a data exception.  this may not be the 
	 * person who ultimately handles the exception, but we must identify the person
	 * so that we can build tasks from the data exceptions.  
	 */
	@Embedded
	@AttributeOverrides( {
        @AttributeOverride(name="type", column = @Column(name="CANDIDATE_RESP_TYPE") ),
        @AttributeOverride(name="id", column = @Column(name="CANDIDATE_RESP_REFERENCE") )
	} )
	private Identity responsiblePerson;
	
	
	public static final String PROPERTY_TASK = "task";
	@ManyToOne(optional=true)
	@JoinColumn(name="TASK_ID", nullable=true)
	private Task task;

	public static final String PROPERTY_BUNDLE_NAME = "bundleName";
	@Basic
	@Size(min=0,max=32)
	@Column(name="BUNDLE_NAME",nullable=true,length=32)
	private String bundleName;

	@Basic
	@Enumerated(EnumType.STRING)
	@Column(name="EXCPTN_DISPOSITION_STATUS_CODE",nullable=false,updatable=true,length=ApprovalDisposition.MAX_LENGTH)
	private ApprovalDisposition approvalDisposition = ApprovalDisposition.Initial;
	public static final String PROPERTY_APPROVAL_DISPOSITION = "implementationDisposition";

	@Basic
	@Enumerated(EnumType.STRING)
	@Column(name="IMPLEMENTATION_CODE",nullable=false,updatable=true,length=ImplementationType.MAX_LENGTH)
	private ImplementationType implementationType = ImplementationType.Manual;
	public static final String PROPERTY_IMPL_TYPE = "implementationType";

	@Basic
	@Enumerated(EnumType.STRING)
	@Column(name="IMPLEMENTATION_DISP_STATUS",nullable=false,updatable=true,length=ImplementationDisposition.MAX_LENGTH)
	private ImplementationDisposition implementationDisposition = ImplementationDisposition.Initial;
	public static final String PROPERTY_IMPL_DISPO = "implementationDisposition";

	
	
	public static final String PROPERTY_SUGGESTED_VALUE = "userSuggestedValue";
	public static final int MAX_LENGTH_SUGGESTED_VALUE = 255;
	@Basic
	@Column(name="SUGGESTED_VALUE",nullable=true,updatable=true,length=MAX_LENGTH_SUGGESTED_VALUE)
	private String userSuggestedValue;

	
	public static final String PROPERTY_DELETED = "deleted";
	@Basic
	@Enumerated(value=EnumType.STRING)
	@Column(name="DELETED_IND",nullable=false,updatable=true,length=YesNo.MAX_LENGTH, columnDefinition="char")
	private YesNo deleted = YesNo.N;

	
	@Embedded
	private AuditData auditData = new AuditData();
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getExceptionCreated() {
		return exceptionCreated;
	}

	public void setExceptionCreated(Date exceptionCreated) {
		this.exceptionCreated = exceptionCreated;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getMdmAttributeValue() {
		return mdmAttributeValue;
	}

	public void setMdmAttributeValue(String mdmAttributeValue) {
		this.mdmAttributeValue = trimToNull(mdmAttributeValue);
	}

	public Long getMdmExceptionId() {
		return mdmExceptionId;
	}

	public void setMdmExceptionId(Long mdmExceptionId) {
		this.mdmExceptionId = mdmExceptionId;
	}

	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * The internal data structure column from the source system (ex. RAPID_ABMASTER.ABID )
	 * @return
	 */
	public String getSourceSystemKeyColumn() {
		return sourceSystemKeyColumn;
	}

	public void setSourceSystemKeyColumn(String sourceSystemKeyColumn) {
		this.sourceSystemKeyColumn = sourceSystemKeyColumn;
	}



	public String getSourceSystemRecordIdentifier() {
		return sourceSystemKey;
	}

	public void setSourceSystemRecordIdentifier(String sourceSystemRecordIdentifier) {
		this.sourceSystemKey = sourceSystemRecordIdentifier;
	}

	public String getSourceSystemValue() {
		return sourceSystemValue;
	}

	public void setSourceSystemValue(String sourceSystemValue) {
		this.sourceSystemValue = sourceSystemValue;
	}
	/**
	 * this is full contextual informtaion like : 
	 * FIRST_NAME : ( Charles ) || LAST_NAME : (Teague ) || COMPANY : (Hoosier Southern Railroad ) || EMAIL : (hosrr@psci.net ) || PHONE : ()
	 * @return
	 */
	public String getSourceSystemObjectData() {
		return sourceSystemObjectData;
	}
	public Map<String,String> getSourceSystemObjectDataMap() {
		return new SourceSystemInfoParser().parse(sourceSystemObjectData);
	}

	public void setSourceSystemObjectData(String sourceSystemObjectData) {
		this.sourceSystemObjectData = sourceSystemObjectData;
	}


	/**
	 * (ex. 105 )
	 * @return
	 */
	public Long getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(Long ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	/**
	 * (ex. COMPANIES MODIFIED/REJECTED BY DQ(RAPID) )
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * (ex. PERSON )
	 * @return
	 */
	public String getMdmObjectType() {
		return mdmObjectType;
	}

	public void setMdmObjectType(String mdmObjectType) {
		this.mdmObjectType = mdmObjectType;
	}

	/**
	 * Represents the logical attribute name of the MDM object. So, if this were 'COMPANY' and {@link #getMdmObjectType()} were 'PERSON',
	 * it would mean the COMPANY attribute of the PERSON object identified by {@link #getSourceSystemRecordIdentifier()}
	 * (ex COMPANY)
	 * @return
	 */
	public String getMdmObjectAttribute() {
		return mdmObjectAttribute;
	}

	public void setMdmObjectAttribute(String mdmObjectAttribute) {
		this.mdmObjectAttribute = mdmObjectAttribute;
	}

	public String getMdmAttributevalue() {
		return mdmAttributeValue;
	}


	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getBundleName() {
		return bundleName;
	}

	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}

	public InboundMessage getSource() {
		return source;
	}

	public void setSource(InboundMessage source) {
		this.source = source;
	}
	
	public void setResponsiblePerson(Identity responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}
	
	public Identity getResponsiblePerson() {
		return responsiblePerson;
	}

	@Override
	public AuditData getAuditData() {
		this.auditData = Optional.fromNullable(this.auditData).or(new AuditData());
		return this.auditData;
	}

	public void setAuditData(AuditData value) {
		this.auditData = value;
	}
	
	@Override
	public boolean isDeleted() {
		return this.deleted.toBoolean();
	}
	
	@Override
	public void delete() {
		this.deleted = YesNo.Y;
	}

	@Override
	public void undelete() {
		this.deleted = YesNo.N;
	}
	
	@ToJson
	public String toJsonString() {
	    return GsonUtil.toJson(this, "task.exceptions");
    }
	
	@FromJson
	public static DataException fromJsonString(String jsonString){
		Validate.notNull(jsonString);
		 return GsonUtil.fromJson(jsonString, DataException.class);
	}
	
	public ApprovalDisposition getApprovalDisposition() {
		return approvalDisposition;
	}

	public void setApprovalDisposition(ApprovalDisposition disposition) {
		this.approvalDisposition = disposition;
	}

	public void setUserSuggestedValue(String suggestion) {
		this.userSuggestedValue = suggestion;		
	}

	public String getUserSuggestedValue() {
		return userSuggestedValue;
	}

	public ImplementationDisposition getImplementationDisposition() {
		return implementationDisposition;
	}

	public void setImplementationDisposition(ImplementationDisposition implementationDisposition) {
		this.implementationDisposition = implementationDisposition;
	}

	public ImplementationType getImplementationType() {
		return implementationType;
	}

	public void setImplementationType(ImplementationType implementationType) {
		this.implementationType = implementationType;
	}
	
	public boolean isManual(){
		return implementationType!=null && implementationType.isManual();
	}
	
	public boolean isAutomatic(){
		return implementationType!=null &&  implementationType.isAutomatic();
	}
	
	public boolean isApprovedToImplement(){
		return approvalDisposition!=null && (approvalDisposition.isApproved() || approvalDisposition.isAutomaticApproval());
	}
	
	public boolean isPassThrough(){
		return implementationType!=null && implementationType.isPassThrough();
	}
	
	public boolean isAvailableForSourceSystemToImplement(){
		if( (isAutomatic() && isApprovedToImplement()) || isPassThrough()){
			return true;
		}
		return false;
	}
	
	public boolean isApprovalDispositionNeedToNotify(){
		return isManual() || !isAvailableForSourceSystemToImplement();
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		if (isNotBlank(userComment)) {
			this.userComment = trimToNull(userComment);
		}
	}
	
	
}
