package com.railinc.r2dq.sso;

import java.util.Collection;


public interface SingleSignOnV2ImplMBean {
	boolean isDown();
	void setDataServicesUri(String value);
	String getDataServicesUri();
	String getTestResourcePath();
	void setTestResourcePath(String testResourcePath);
	String getRecheckUnit();
	void setRecheckUnit(String value);
	long getRecheckAfterDuration();
	void setRecheckAfterDuration(long recheckAfterDuration);
	
	Collection<String> usersInGroup(String groupId);
	SingleSignOnUser getUser(String loginId, boolean returnPermissions);
	Collection<String> groupsForUser(String userLogin);
	Collection<String> userLoginsByEmail(String email);

}
