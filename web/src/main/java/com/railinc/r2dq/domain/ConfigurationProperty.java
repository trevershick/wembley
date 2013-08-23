package com.railinc.r2dq.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import com.google.common.base.Optional;
import com.railinc.common.configuration.hibernate.HibernateConfigurationProperty;

@Entity
@EntityListeners(value = AuditListener.class)
@DiscriminatorValue("R2DQ")
public class ConfigurationProperty extends HibernateConfigurationProperty implements Auditable {

	@Embedded
	private AuditData audit = new AuditData();

	public ConfigurationProperty() {
	}

	public ConfigurationProperty(String id, String value, String name) {
		setCode(id);
		setName(name);
		setValue(value);
	}

	@Override
	public AuditData getAuditData() {
		this.audit = Optional.fromNullable(this.audit).or(new AuditData());
		return this.audit;
	}

	public AuditData getAudit() {
		return audit;
	}

	public void setAudit(AuditData audit) {
		this.audit = audit;
	}
}
