package com.railinc.r2dq.i18n;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.util.PagedSearchForm;

public class I18nSearchForm extends PagedSearchForm<I18nCriteria, I18nForm> {
	public static final String DEFAULT_FORM_NAME = "i18nsearch";
	/**
	 * 
	 */
	private static final long serialVersionUID = -7112438745586765432L;
	
	private String query;
	

	

	@Override
	public I18nCriteria getCriteriaInternal() {
		I18nCriteria criteria = new I18nCriteria();
		criteria.setFreeText(query);
		return criteria;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String in) {
		this.query = StringUtils.trimToNull(in);
	}


	

}
