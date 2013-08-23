package com.railinc.r2dq.implementation;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.Implementation;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.RuleNumberType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.CriteriaWithPaging;

public class ImplementationCriteria extends CriteriaWithPaging {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5043736016260052092L;
	
	
	private CriteriaValue<SourceSystem> sourceSystem = CriteriaValue.unspecified();
	private CriteriaValue<RuleNumberType> ruleNumberType = CriteriaValue.unspecified();
	private CriteriaValue<Long> ruleNumberFrom = CriteriaValue.unspecified();
	private CriteriaValue<Long> ruleNumberThru = CriteriaValue.unspecified();
    private CriteriaValue<Long> ruleNumber = CriteriaValue.unspecified();
	private CriteriaValue<ImplementationType> implementationType = CriteriaValue.unspecified();
	private CriteriaValue<String> freeText = CriteriaValue.unspecified();
	
	public ImplementationCriteria() {}
	/**
	 * creates a criteria object from an example.  IF a value is null in the example, it will
	 * use the NULL criteria which will match the property with NULL.
	 * @param s
	 */
	public ImplementationCriteria(Implementation s) {
		sourceSystem = CriteriaValue.orNull(s.getSourceSystem());
		ruleNumberType = CriteriaValue.orNull(s.getRuleNumberType());
		ruleNumberFrom = CriteriaValue.orNull(s.getRuleNumberFrom());
		ruleNumberThru = CriteriaValue.orNull(s.getRuleNumberThru());
		implementationType = CriteriaValue.orNull(s.getType());
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
	
	public void setRuleNumberType(RuleNumberType ruleNumberType) {
		this.ruleNumberType = CriteriaValue.orUnspecified(ruleNumberType);
	}
	
	public void setRuleNumberFrom(Long ruleNumberFrom) {
		this.ruleNumberFrom = CriteriaValue.orUnspecified(ruleNumberFrom);
	}
	
	public void setRuleNumberThru(Long ruleNumberThru) {
		this.ruleNumberThru = CriteriaValue.orUnspecified(ruleNumberThru);
	}
	
	public CriteriaValue<RuleNumberType> getRuleNumberType() {
		return ruleNumberType;
	}
	
	public CriteriaValue<Long> getRuleNumberFrom() {
		return ruleNumberFrom;
	}
	
	public CriteriaValue<Long> getRuleNumberThru() {
		return ruleNumberThru;
	}
	
	public CriteriaValue<ImplementationType> getImplementationType() {
		return implementationType;
	}
	
	public void setImplementationType(ImplementationType value) {
		this.implementationType = CriteriaValue.orUnspecified(value);
	}
	
}
