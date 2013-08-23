package com.railinc.membership.sso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserInfo;

public class SSOIHateUsage implements SSO {

	Map<String, List<String>> appRoles = new HashMap<String, List<String>>();
	Map<String, List<String>> roleUsers = new HashMap<String, List<String>>();
	
	
	public SSOIHateUsage() {
		
	}
	

	
	public List<String> getRolesForApp(String appName) {
		throw new RuntimeException("Don't call getRolesForApp");
	}

	public List<SSOUserInfo> getUsersInRole(String roleName) {
		throw new RuntimeException("Don't call getUsersInRole");
	}

	public List<String> getAppsForUser(String uid) {
		throw new RuntimeException("Don't call getAppsForUser");
	}

	public List<String> getRolesForUser(String uid) {
		throw new RuntimeException("Don't call getRolesForUser");
	}

	public List<String> getPropertyForUser(String userId, String property) {
		throw new RuntimeException("Don't call getPropertyForUser");
	}



	public List<String> getModuleIds() {
		throw new RuntimeException("Don't call getModuleIds");
	}



	public List<SSOUserInfo> getAllActiveUsers() {
		return null;
	}

}
