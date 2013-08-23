package com.railinc.r2dq.dataexception;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.CriteriaWithPaging;

public class DataExceptionCriteria extends CriteriaWithPaging {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5043736016260052092L;

	private CriteriaValue<SourceSystem> sourceSystem = CriteriaValue.unspecified();
	private CriteriaValue<Long> ruleNumber = CriteriaValue.unspecified();
	private CriteriaValue<IdentityType> personType = CriteriaValue.unspecified();
	private CriteriaValue<String> person = CriteriaValue.unspecified();

	private CriteriaValue<String> mdmAttributevalue = CriteriaValue.unspecified();
	private CriteriaValue<String> mdmObjectAttribute = CriteriaValue.unspecified();
	private CriteriaValue<String> mdmObjectType = CriteriaValue.unspecified();

	private CriteriaValue<String> sourceSystemKeyColumn = CriteriaValue.unspecified();
	private CriteriaValue<String> sourceSystemKey = CriteriaValue.unspecified();
	private CriteriaValue<String> sourceSystemObjectData = CriteriaValue.unspecified();
	private CriteriaValue<String> sourceSystemValue = CriteriaValue.unspecified();

	private CriteriaValue<String> freeText = CriteriaValue.unspecified();

	public DataExceptionCriteria() {
	}

	public CriteriaValue<String> setMdmAttributevalue(String mdmAttributevalue) {
		return this.mdmAttributevalue = CriteriaValue.orUnspecified(StringUtils.trimToNull(mdmAttributevalue));
	}

	public CriteriaValue<String> setMdmObjectAttribute(String mdmObjectAttribute) {
		return this.mdmObjectAttribute = CriteriaValue.orUnspecified(StringUtils.trimToNull(mdmObjectAttribute));
	}

	public CriteriaValue<String> setMdmObjectType(String mdmObjectType) {
		return this.mdmObjectType = CriteriaValue.orUnspecified(StringUtils.trimToNull(mdmObjectType));
	}

	public CriteriaValue<String> setSourceSystemKeyColumn(String sourceSystemKeyColumn) {
		return this.sourceSystemKeyColumn = CriteriaValue.orUnspecified(StringUtils.trimToNull(sourceSystemKeyColumn));
	}

	public CriteriaValue<String> setSourceSystemKey(String v) {
		return this.sourceSystemKey = CriteriaValue.orUnspecified(StringUtils.trimToNull(v));
	}

	public CriteriaValue<String> setSourceSystemObjectData(String sourceSystemObjectData) {
		return this.sourceSystemObjectData = CriteriaValue.orUnspecified(StringUtils.trimToNull(sourceSystemObjectData));
	}


	public CriteriaValue<String> setSourceSystemValue(String sourceSystemValue) {
		return this.sourceSystemValue = CriteriaValue.orUnspecified(StringUtils.trimToNull(sourceSystemValue));
	}

	public CriteriaValue<String> getMdmAttributevalue() {
		return mdmAttributevalue;
	}

	public CriteriaValue<String> getMdmObjectAttribute() {
		return mdmObjectAttribute;
	}

	public CriteriaValue<String> getMdmObjectType() {
		return mdmObjectType;
	}

	public CriteriaValue<String> getSourceSystemKeyColumn() {
		return sourceSystemKeyColumn;
	}

	public CriteriaValue<String> getSourceSystemKey() {
		return sourceSystemKey;
	}

	public CriteriaValue<String> getSourceSystemObjectData() {
		return sourceSystemObjectData;
	}


	public CriteriaValue<String> getSourceSystemValue() {
		return sourceSystemValue;
	}

	public CriteriaValue<SourceSystem> getSourceSystem() {
		return sourceSystem;
	}

	public CriteriaValue<String> getFreeText() {
		return freeText;
	}

	public void setFreeText(String value) {
		this.freeText = CriteriaValue.orUnspecified(StringUtils.trimToNull(value));
	}

	public void setSourceSystem(SourceSystem value) {
		this.sourceSystem = CriteriaValue.orUnspecified(value);
	}

	public CriteriaValue<Long> getRuleNumber() {
		return ruleNumber;
	}

	public void setRuleNumber(Long value) {
		this.ruleNumber = CriteriaValue.orUnspecified(value);
	}

	public CriteriaValue<IdentityType> getPersonType() {
		return personType;
	}

	public void setPersonType(IdentityType value) {
		this.personType = CriteriaValue.orUnspecified(value);
	}

	public CriteriaValue<String> getPerson() {
		return person;
	}

	public CriteriaValue<String> setPerson(String value) {
		return this.person = CriteriaValue.orUnspecified(StringUtils.trimToNull(value));
	}

}
