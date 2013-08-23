package com.railinc.wembley.legacy.domain.impl.sso;

import java.util.List;

public interface SSO {
	List<String> getRolesForApp(String appName);
	List<SSOUserInfo> getUsersInRole(String roleName);
	List<String> getRolesForUser(String uid);
	List<String> getAppsForUser(String uid);
	List<String> getPropertyForUser( String userId, String property );
	List<String> getModuleIds();
	List<SSOUserInfo> getAllActiveUsers();
}
