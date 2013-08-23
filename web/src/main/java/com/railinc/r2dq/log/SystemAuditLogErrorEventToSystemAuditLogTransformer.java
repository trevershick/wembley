package com.railinc.r2dq.log;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.SystemAuditLog;

public class SystemAuditLogErrorEventToSystemAuditLogTransformer implements Function<SystemAuditLogErrorEvent, SystemAuditLog> {
	
	@Override
	public SystemAuditLog apply(SystemAuditLogErrorEvent errorEvent) {
		SystemAuditLog trace = new SystemAuditLog();
		trace.setAction(errorEvent.getAction());
		trace.setType(SystemAuditLog.AuditLogType.ERROR);
		trace.setEntityName(errorEvent.getEntityName());
		trace.setEntityId(errorEvent.getEntityId());
		trace.setCause(errorEvent.getCause());
		trace.setCreatedDate(errorEvent.getEventDate());
		trace.setDetails(errorEvent.getDetails());
		return trace;
	}
	

}
