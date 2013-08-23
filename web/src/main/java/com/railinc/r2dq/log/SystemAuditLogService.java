package com.railinc.r2dq.log;

import java.util.List;

import com.railinc.r2dq.domain.SystemAuditLog;
import com.railinc.r2dq.domain.views.SystemAuditLogView;
import com.railinc.r2dq.util.PagedCollection;

public interface SystemAuditLogService {
	void save(SystemAuditLog systemError);
	
	

	PagedCollection<SystemAuditLogView> linkLogHistory(SystemAuditLogSearchCriteria criteria);


	SystemAuditLogView findLogByMasterId(Long masterId);



	PagedCollection<SystemAuditLogView> findAllLogExceptions();



	PagedCollection<SystemAuditLogView> findLogsByNameAndId(SystemAuditLogSearchCriteria criteria);


}
