package com.railinc.r2dq.domain.views;

import java.util.Map;

import com.railinc.r2dq.domain.SourceSystem;

public class DataExceptionView {
	private Long id;

	private String description;
	
	private String mdmObjectType;
	private String mdmObjectAttribute;
	private String mdmAttributeValue;
	
	SourceSystem sourceSystem;
	private String sourceSystemKeyColumn;
	private String sourceSystemKey;
	private String sourceSystemValue;
	private Map<String,String> sourceSystemObjectData;

	private Long ruleNumber;
	

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMdmAttributeValue() {
		return mdmAttributeValue;
	}
	public void setMdmAttributeValue(String mdmAttributeValue) {
		this.mdmAttributeValue = mdmAttributeValue;
	}
	public String getMdmObjectAttribute() {
		return mdmObjectAttribute;
	}
	public void setMdmObjectAttribute(String mdmObjectAttribute) {
		this.mdmObjectAttribute = mdmObjectAttribute;
	}
	public String getMdmObjectType() {
		return mdmObjectType;
	}
	public void setMdmObjectType(String mdmObjectType) {
		this.mdmObjectType = mdmObjectType;
	}
	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getSourceSystemKeyColumn() {
		return sourceSystemKeyColumn;
	}
	public void setSourceSystemKeyColumn(String sourceSystemKeyColumn) {
		this.sourceSystemKeyColumn = sourceSystemKeyColumn;
	}
	public String getSourceSystemKey() {
		return sourceSystemKey;
	}
	public void setSourceSystemKey(String v) {
		this.sourceSystemKey = v;
	}
	


	public Map<String, String> getSourceSystemObjectData() {
		return sourceSystemObjectData;
	}
	public void setSourceSystemObjectData(Map<String, String> sourceSystemObjectData) {
		this.sourceSystemObjectData = sourceSystemObjectData;
	}
	public String getSourceSystemValue() {
		return sourceSystemValue;
	}
	public void setSourceSystemValue(String sourceSystemValue) {
		this.sourceSystemValue = sourceSystemValue;
	}
	public Long getRuleNumber() {
		return this.ruleNumber;
	}
	public void setRuleNumber(Long ruleNumber) {
		this.ruleNumber = ruleNumber;
	}
	
	
}
