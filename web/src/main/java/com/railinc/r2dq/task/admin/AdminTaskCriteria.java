package com.railinc.r2dq.task.admin;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.task.TaskCriteria;
import com.railinc.r2dq.util.CriteriaValue;

public class AdminTaskCriteria extends TaskCriteria {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5043736016260052092L;
	
	private CriteriaValue<IdentityType> personType = CriteriaValue.unspecified();
	private CriteriaValue<String> person = CriteriaValue.unspecified();

	public CriteriaValue<IdentityType> getPersonType() {
		return personType;
	}
	
	public void setPersonType(IdentityType value) {
		this.personType = CriteriaValue.orUnspecified(value);
	}
	
	public CriteriaValue<String> getPerson() {
		return person;
	}
	public void setPerson(String value) {
		this.person = CriteriaValue.orUnspecified(StringUtils.trimToNull(value));
	}

	

}
