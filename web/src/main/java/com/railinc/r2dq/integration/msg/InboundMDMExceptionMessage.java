package com.railinc.r2dq.integration.msg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.Validate;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.railinc.r2dq.integration.ToJson;
import com.railinc.r2dq.util.GsonUtil;

@Table(name = "MDM_EXCPTN_STATUS_VW")
@Entity
public class InboundMDMExceptionMessage {

	@SerializedName("created")
	@Column(name="CREATED_TS")
	private Date created;

	@SerializedName("sourcesystem")
	@Column(name="EDW_SRC_NAME")
	private String sourceSystem;
	
	@SerializedName("sourcekeycol")
	@Column(name="SRC_KEY_COLUMN")
    private String sourceSystemKeyColumn;

	@SerializedName("sourcekeyvalue")
	@Column(name="SRC_KEY")
	private String sourceSystemKeyValue;

	@SerializedName("sourcevalue")
	@Column(name="MDM_EXCPTN_VALUE")
    private String sourceSystemValue; // assumed wrong

	@SerializedName("sourceinfo")
	@Column(name="PARTIAL_SRC_INFO")
    private String sourceSystemObjectData;
	
	@SerializedName("code")
	@Column(name="EXCPTN_CD")
	private Long code;
	
	@SerializedName("description")
	@Column(name="EXCPTN_DESC")
	private String description;

	@SerializedName("id")
	@Id
	@Column(name="EXCPTN_ID")
	private Long mdmExceptionId;

	@SerializedName("type")
	@Column(name="MDM_EXCPTN_TYPE")
	private String mdmObjectType;

	@SerializedName("attr")
	@Column(name="MDM_EXCPTN_COL_NAME")
	private String mdmObjectAttribute;
	
	@SerializedName("value")
	@Column(name="MDM_VALUE")
	private String mdmAttributevalue;
	
	
	@Column(name="EXCPTN_STATUS")
	private String status;
	
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceSystemKeyColumn() {
		return sourceSystemKeyColumn;
	}

	public void setSourceSystemKeyColumn(String sourceSystemKeyColumn) {
		this.sourceSystemKeyColumn = sourceSystemKeyColumn;
	}

	public String getSourceSystemKeyValue() {
		return sourceSystemKeyValue;
	}

	public void setSourceSystemKeyValue(String sourceSystemKeyValue) {
		this.sourceSystemKeyValue = sourceSystemKeyValue;
	}

	public String getSourceSystemValue() {
		return sourceSystemValue;
	}

	public void setSourceSystemValue(String sourceSystemValue) {
		this.sourceSystemValue = sourceSystemValue;
	}

	public String getSourceSystemObjectData() {
		return sourceSystemObjectData;
	}

	public void setSourceSystemObjectData(String sourceSystemObjectData) {
		this.sourceSystemObjectData = sourceSystemObjectData;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMdmObjectType() {
		return mdmObjectType;
	}

	public void setMdmObjectType(String mdmObjectType) {
		this.mdmObjectType = mdmObjectType;
	}

	public String getMdmObjectAttribute() {
		return mdmObjectAttribute;
	}

	public void setMdmObjectAttribute(String mdmObjectAttribute) {
		this.mdmObjectAttribute = mdmObjectAttribute;
	}

	public String getMdmAttributevalue() {
		return mdmAttributevalue;
	}

	public void setMdmAttributevalue(String mdmAttributevalue) {
		this.mdmAttributevalue = mdmAttributevalue;
	}
	
	public Long getMdmExceptionId() {
		return mdmExceptionId;
	}

	public void setMdmExceptionId(Long mdmExceptionId) {
		this.mdmExceptionId = mdmExceptionId;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static InboundMDMExceptionMessage fromJson(String jsonString){
		Validate.notNull(jsonString);
		return new GsonBuilder()
		.setDateFormat("yyyy-mm-dd HH:MM")
		.create()
		.fromJson(jsonString, InboundMDMExceptionMessage.class);
	}
	
	@ToJson
	public String toJsonString() {
	    return GsonUtil.toJson(this, "status");
    }
	
}
