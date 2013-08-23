package com.railinc.r2dq.domain.views;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.SystemAuditLog;

public class SystemAuditLogToView implements Function<SystemAuditLog,SystemAuditLogView> {

	@Override
	public SystemAuditLogView apply(SystemAuditLog in) {
		SystemAuditLogView v = new SystemAuditLogView();
		v.setId(in.getId());
		v.setAction(in.getAction());
		v.setCause(in.getCause());
		v.setEntityName(in.getEntityName());
		v.setEntityId(in.getEntityId());
		v.setSourceEntityName(in.getSourceEntityName());
		v.setSourceEntityId(in.getSourceEntityId());
		v.setDetails(in.getDetails());
		v.setCreatedDate(in.getCreatedDate());
		v.setType(in.getType());
		return v;
	}

	

}
