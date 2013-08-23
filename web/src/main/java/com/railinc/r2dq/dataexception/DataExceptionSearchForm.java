package com.railinc.r2dq.dataexception;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.util.PagedSearchForm;

public class DataExceptionSearchForm extends PagedSearchForm<DataExceptionCriteria, DataExceptionForm> {

	public static final String DEFAULT_FORM_NAME = "dataexceptionsearch";

	/**
	 * 
	 */
	private static final long serialVersionUID = -7112438745586765432L;
	
	private String query;
	private SourceSystem sourceSystem;
	private IdentityType personType;

	private Collection<SourceSystem> sourceSystems;
	

	
	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public IdentityType getPersonType() {
		return personType;
	}

	public void setPersonType(IdentityType personType) {
		this.personType = personType;
	}

	@Override
	public DataExceptionCriteria getCriteriaInternal() {
		DataExceptionCriteria criteria = new DataExceptionCriteria();
		criteria.setSourceSystem(sourceSystem);
		criteria.setPersonType(personType);
		criteria.setFreeText(query);
		return criteria;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String in) {
		this.query = StringUtils.trimToNull(in);
	}

	public void setSourceSystems(Collection<SourceSystem> sourceSystems) {
		this.sourceSystems = sourceSystems;
	}

	public Collection<SourceSystem> getSourceSystems() {
		return sourceSystems;
	}


	

}
