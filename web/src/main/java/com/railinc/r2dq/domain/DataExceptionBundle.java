package com.railinc.r2dq.domain;

import org.apache.commons.lang.builder.EqualsBuilder;

public class DataExceptionBundle {
	private SourceSystem sourceSystem;
	private Identity responsiblePerson;
	private String sourceSystemColumnName;
	private String sourceSystemIdentifier;
	private ImplementationType implementationType;

	public SourceSystem getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(SourceSystem sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public Identity getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(Identity responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public void setSourceSystemColumnName(String sourceSystemColumnName) {
		this.sourceSystemColumnName = sourceSystemColumnName;
	}
	
	public String getSourceSystemColumnName() {
		return sourceSystemColumnName;
	}

	public String getSourceSystemIdentifier() {
		return sourceSystemIdentifier;
	}

	public void setSourceSystemIdentifier(String sourceSystemIdentifier) {
		this.sourceSystemIdentifier = sourceSystemIdentifier;
	}

	public ImplementationType getImplementationType() {
		return implementationType;
	}

	public void setImplementationType(ImplementationType implementationType) {
		this.implementationType = implementationType;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj ==null || !(obj instanceof DataExceptionBundle)){
			return false;
		}
		
		DataExceptionBundle other = (DataExceptionBundle)obj;
		
		return new EqualsBuilder()
		.append(getSourceSystem(), other.getSourceSystem())
		.append(getSourceSystemColumnName(), other.getSourceSystemColumnName())
		.append(getSourceSystemIdentifier(), other.getSourceSystemIdentifier())
		.append(getResponsiblePerson(), other.getResponsiblePerson())
		.append(getImplementationType(), other.getImplementationType()).isEquals();
	}

}
