package com.railinc.r2dq.i18n;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.CriteriaWithPaging;

public class I18nCriteria extends CriteriaWithPaging {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5043736016260052092L;



	private CriteriaValue<String> freeText = CriteriaValue.unspecified();

	public I18nCriteria() {
	}



	public CriteriaValue<String> getFreeText() {
		return freeText;
	}

	public void setFreeText(String value) {
		this.freeText = CriteriaValue.orUnspecified(StringUtils.trimToNull(value));
	}


}
