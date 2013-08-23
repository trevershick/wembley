package com.railinc.r2dq.log;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;

import com.railinc.r2dq.domain.views.SystemAuditLogView;
import com.railinc.r2dq.util.PagedSearchForm;
import com.railinc.r2dq.util.WebFormConstants;

public class SystemAuditLogSearchForm extends PagedSearchForm<SystemAuditLogSearchCriteria, SystemAuditLogView> {

	public static final String DEFAULT_FORM_NAME = "systemauditlogsearch";

	/**
	 * 
	 */
	private static final long serialVersionUID = 304639385271754558L;
	
	private String entityName;
	private String entityId;
	private boolean findExceptions;
	

	@Override
	public SystemAuditLogSearchCriteria getCriteriaInternal() {
		SystemAuditLogSearchCriteria c = new SystemAuditLogSearchCriteria();
		// build up the criteria from the fields
		if (entityName != null) {
			c.setEntityName(entityName);
		}
		if (entityId != null) {
			c.setEntityId(entityId);
		}
		return c;
	}
	
	public static final void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, WebFormConstants.timestampPropertyEditor());
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setFindExceptions(boolean findExceptions) {
		this.findExceptions = findExceptions;
	}

	public boolean isFindExceptions() {
		return findExceptions;
	}

	

}
