package com.railinc.r2dq.sourcesystem;

import static org.apache.commons.lang.StringUtils.trimToNull;
import static org.apache.commons.lang.StringUtils.upperCase;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.railinc.r2dq.domain.AuditData;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.SourceSystem;

public class SourceSystemForm {

	public static final String DEFAULT_FORM_NAME = "sourcesystem";

	@NotNull
	@Size(min=1,max=SourceSystem.MAX_LENGTH_ID)
	String id;
	
	@NotNull
	@Size(min=1,max=SourceSystem.MAX_LENGTH_NAME)
	String name;

	@Size(min=1,max=SourceSystem.MAX_LENGTH_OUTBOUND_QUEUE)
	//@Pattern(regexp="[A-Z\\.]+", message="OutboundQueue name is should contain only alpha numeric.")
	@Pattern(regexp="^[0-9a-zA-Z\\.]+$")
	String outboundQueue;

	// TODO - build custom javax.validation validator that validates these two things together
	@NotNull
	private IdentityType personType = SourceSystem.DEFAULT_DATA_STEWARD_TYPE;

	@NotNull
	private String person = SourceSystem.DEFAULT_DATA_STEWARD_ID;

	
	@NotNull
	private Integer version;
	
	private AuditData auditData;
	
	public AuditData getAuditData() {
		return auditData;
	}

	public void setAuditData(AuditData auditData) {
		this.auditData = auditData;
	}

	public SourceSystemForm() {
		version = 0;
	}
	
	public SourceSystemForm(SourceSystem ss) {
		this.id = ss.getIdentifier();
		this.name = ss.getName();
		this.outboundQueue = ss.getOutboundQueue();
		this.version = ss.getVersion();
		this.auditData = (AuditData) ss.getAuditData().clone();
		Identity ds = ss.getDataSteward();
		if (ds != null) {
			this.person = ds.getId();
			this.personType =ds.getType();
		}
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = upperCase(id);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = trimToNull(name);
	}

	public String getOutboundQueue() {
		return outboundQueue;
	}

	public void setOutboundQueue(String value) {
		this.outboundQueue = trimToNull(value);
	}
	public IdentityType getPersonType() {
		return personType;
	}

	public void setPersonType(IdentityType personType) {
		this.personType = personType;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = trimToNull(person);
	}

}
