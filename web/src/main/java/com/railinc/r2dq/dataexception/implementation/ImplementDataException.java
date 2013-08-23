package com.railinc.r2dq.dataexception.implementation;

import java.io.Serializable;

import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.integration.FromJson;
import com.railinc.r2dq.integration.ToJson;
import com.railinc.r2dq.util.GsonUtil;

public abstract class ImplementDataException implements Serializable {
	private static final long serialVersionUID = 1L;
	private SourceSystem sourceSystem;
	private Long ruleNumber;
	private String sourceSystemKeyIdentifier;
	private String sourceSystemEntityColumnName;
	private String mdmValue;
	private String description;
	private String type;
	
	public ImplementDataException(String type){
		this.type = type;
	}

	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public Long getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(Long ruleNumber) {
		this.ruleNumber = ruleNumber;
	}

	public String getSourceSystemKeyIdentifier() {
		return sourceSystemKeyIdentifier;
	}

	public void setSourceSystemKeyIdentifier(String sourceSystemKeyIdentifier) {
		this.sourceSystemKeyIdentifier = sourceSystemKeyIdentifier;
	}

	public String getSourceSystemColumnName() {
		return sourceSystemEntityColumnName;
	}

	public void setSourceSystemColumnName(String sourceSystemColumnName) {
		this.sourceSystemEntityColumnName = sourceSystemColumnName;
	}

	public String getMdmValue() {
		return mdmValue;
	}
	
	public void setMdmValue(String mdmValue) {
		this.mdmValue = mdmValue;
	}

	public String getExceptionDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getType() {
		return type;
	}
	
	protected String toJsonIgnoreFields(){
		return "sourceSystem.audit,sourceSystem.version,sourceSystem.dataSteward,sourceSystem.outboundQueue,sourceSystem.deleted,sourceSystem.name";
	}
	
	@ToJson
	public String toJsonString(){
		return GsonUtil.toJson(this, toJsonIgnoreFields());
	}
	
	@FromJson
	public ImplementDataException fromJson(String jsonString){
		return GsonUtil.fromJson(jsonString, ImplementDataException.class);
	}

}
