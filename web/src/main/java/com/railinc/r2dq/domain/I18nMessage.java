package com.railinc.r2dq.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import com.google.common.base.Optional;
import com.railinc.common.messages.hibernate.HibernateI18nMessage;

@Entity
@EntityListeners(value = AuditListener.class)
@DiscriminatorValue("R2DQ")
public class I18nMessage extends HibernateI18nMessage implements Auditable {

	public static final String PROPERTY_AUDIT = "audit";
	public static final String PROPERTY_UPDATED = PROPERTY_AUDIT + "." + AuditData.PROPERTY_UPDATED;
	
	@Embedded
	private AuditData audit = new AuditData();

	public I18nMessage() {
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
