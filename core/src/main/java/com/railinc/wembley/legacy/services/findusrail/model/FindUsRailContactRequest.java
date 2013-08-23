package com.railinc.wembley.legacy.services.findusrail.model;

import com.railinc.wembley.api.findusrail.FindUsRailContactCategoryRole;
import com.railinc.wembley.api.findusrail.FindUsRailContactCompanyType;

public interface FindUsRailContactRequest {

	FindUsRailContactCompanyType getCompanyType();
	String getCompanyId();
	String getCategory();
	FindUsRailContactCategoryRole getCategoryRole();
	String getCategoryFunction();
	boolean isIncludeAgents();
	boolean isIncludeChildren();
	boolean isIncludeParent();
}
