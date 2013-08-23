package com.railinc.r2dq.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

import com.google.common.base.Optional;
import com.railinc.r2dq.integration.FlowEntity;
import com.railinc.r2dq.integration.FlowEntityId;
import com.railinc.r2dq.integration.FromJson;
import com.railinc.r2dq.integration.ToJson;
import com.railinc.r2dq.util.GsonUtil;

@Table(name="MESSAGE")
@Entity(name="message")
@DiscriminatorColumn(length=1,discriminatorType=DiscriminatorType.STRING, name="MESSAGE_TYPE_FLAG", columnDefinition="char")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@FlowEntity
public abstract class GenericMessage implements Auditable{
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_NAME = "name";
	public static final String DEFAULT_ORDER_BY_PROPERTY = PROPERTY_ID;
	public static final String PROPERTY_MESSAGE_TYPE = "type";
	public static final String PROPERTY_INBOUND_SOURCE = "source";
	public static final String PROPERTY_OUTBOUND = "outbound";
	public static final String PROPERTY_PROCESSED = "processed";
	public static final String PROPERTY_DATA = "data";
	public static final String PROPERTY_CREATED = "audit."+AuditData.PROPERTY_CREATED;
	public static final int MAX_LENGTH_MSG_DATA = 4000; 
	
	@Id
	@SequenceGenerator(name="MESSAGE_SEQ", sequenceName="MESSAGE_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="MESSAGE_SEQ")
    @Column(name="MESSAGE_ID")
	@FlowEntityId
	private Long id;

	@Basic
	@Enumerated(value=EnumType.STRING)
	@Column(name="PROCESSED_IND", nullable=false, updatable=true, length=YesNo.MAX_LENGTH, columnDefinition="char")
	private YesNo processed = YesNo.N;
	
	
	@Basic
	@Column(name="MSG_DATA", nullable=false, updatable=true, length=MAX_LENGTH_MSG_DATA)
	private String data;
	
	@Enumerated(value=EnumType.STRING)
	@Basic(optional=true)
	@Column(name="MSG_SOURCE", nullable=true, updatable=false, length=15)
	protected InboundSource source = null;
	
	@Basic(optional = true)
	@Column(name="OUTBOUND_DEST_QUEUE_NAME", nullable=true, updatable=false, length=256)
	protected String outbound;

	@Embedded
	private AuditData audit = new AuditData();
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public Long getIdentifier() {
		return id;
	}

	public void setIdentifier(Long identifier) {
		this.id = identifier;
	}

	public YesNo getProcessed() {
		return processed;
	}

	public void setProcessed(YesNo processed) {
		if (processed != null) {
			this.processed = processed;
		}
	}

	@Override
	public AuditData getAuditData() {
		this.audit = Optional.fromNullable(this.audit).or(new AuditData());
		return this.audit;
	}

	public AuditData getAudit() {
		return audit;
	}

	public void setAudit(AuditData audit) {
		this.audit = audit;
	}

	public boolean isProcessed() {
		return this.processed.toBoolean();
	}
	
	public void markAsProcessed(){
		this.processed=YesNo.Y;
	}
	
	@ToJson
	public String toJsonString() {
	    return GsonUtil.toJson(this);
    }
	
	@FromJson
	public static GenericMessage fromJsonString(String jsonString){
		Validate.notNull(jsonString);
		 return GsonUtil.fromJson(jsonString, GenericMessage.class);
	}

}
