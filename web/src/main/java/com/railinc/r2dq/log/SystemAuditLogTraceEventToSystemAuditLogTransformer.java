package com.railinc.r2dq.log;

import com.google.common.base.Function;
import com.railinc.r2dq.domain.SystemAuditLog;

public class SystemAuditLogTraceEventToSystemAuditLogTransformer implements Function<SystemAuditLogTraceEvent, SystemAuditLog> {
	
	@Override
	public SystemAuditLog apply(SystemAuditLogTraceEvent traceEvent) {
		SystemAuditLog trace = new SystemAuditLog();
		trace.setAction(traceEvent.getAction());
		trace.setType(SystemAuditLog.AuditLogType.TRACE);
		trace.setEntityName(traceEvent.getEntityName());
		trace.setEntityId(traceEvent.getEntityId().toString());
		trace.setSourceEntityName(traceEvent.getSourceEntityName());
		trace.setSourceEntityId(traceEvent.getSourceEntityId()!=null ? traceEvent.getSourceEntityId().toString():null);
		trace.setCreatedDate(traceEvent.getEventDate());
		trace.setDetails(traceEvent.getDetails());
		return trace;
	}
	

}
