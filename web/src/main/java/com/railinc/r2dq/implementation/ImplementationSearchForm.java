package com.railinc.r2dq.implementation;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.util.PagedSearchForm;

public class ImplementationSearchForm extends PagedSearchForm<ImplementationCriteria, ImplementationForm> {

	public static final String DEFAULT_FORM_NAME = "implementationsearch";

	private static final long serialVersionUID = -7112438745586765432L;
	
	private String query;
	private SourceSystem sourceSystem;
	private ImplementationType implementationType;
	

	
	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public ImplementationType getImplementationType() {
		return implementationType;
	}

	public void setImplementationType(ImplementationType implementationType) {
		this.implementationType = implementationType;
	}

	@Override
	public ImplementationCriteria getCriteriaInternal() {
		ImplementationCriteria criteria = new ImplementationCriteria();
		criteria.setSourceSystem(sourceSystem);
		criteria.setImplementationType(implementationType);
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
