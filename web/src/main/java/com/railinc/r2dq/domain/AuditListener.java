package com.railinc.r2dq.domain;

import java.io.Serializable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditListener extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7929827740140517226L;

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		if (entity instanceof Auditable) {
			updateLastModified((Auditable) entity);
		}
		return true;

	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		if (entity instanceof Auditable) {
			updateLastModified((Auditable) entity);
		}
		return true;
	}

	@PreUpdate
	@PrePersist
	public void updateLastModified(Auditable auditable) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String principalName = authentication != null ? authentication.getName() : "unknown";
		auditable.getAuditData().touchLastModified(principalName);
	}
}
