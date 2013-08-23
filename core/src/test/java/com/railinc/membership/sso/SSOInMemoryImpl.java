package com.railinc.membership.sso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserInfo;

public class SSOInMemoryImpl implements SSO {

	Map<String, List<String>> appRoles = new HashMap<String, List<String>>();
	Map<String, List<SSOUserInfo>> roleUsers = new HashMap<String, List<SSOUserInfo>>();
	
	public SSOInMemoryImpl() {
		
	}
	
	public void add(String app, String role, String ... userIds) {
		List<String> roles = appRoles.get(app);
		if (roles == null) {
			roles = new ArrayList<String>();
			appRoles.put(app, roles);
		}
		roles.add(role);
		
		List<SSOUserInfo> users = roleUsers.get(app);
		if (users == null) {
			users = new ArrayList<SSOUserInfo>();
			roleUsers.put(role, users);
		}
		
		for (String uid : userIds) {
			SSOUserInfo u = new SSOUserInfo();
			u.setUsername(uid);
			users.add(u);
		}
	}
	

	public List<String> getRolesForApp(String appName) {
		//System.out.println("SSO::getRolesForApp");
		return appRoles.get(appName);
	}

	public List<SSOUserInfo> getUsersInRole(String roleName) {
		//System.out.println("SSO::getUsersInRole");
		return roleUsers.get(roleName);
	}

	public List<String> getAppsForUser(String uid) {
		//System.out.println("SSO::getAppsForUser");
		List<String> rolesForUser = getRolesForUser(uid);
		List<String> userRoles = rolesForUser;
		
		List<String> apps = new ArrayList<String>();
		
		for (String userRole : userRoles) {
			Set<Entry<String, List<String>>> entrySet = appRoles.entrySet();
			
			for (Entry<String,List<String>> entry : entrySet) {
				String app = entry.getKey();
				List<String> appRoles = entry.getValue();
				if (appRoles.contains(userRole) && !apps.contains(app)) {
					apps.add(app);
				}
			}
		}
		return apps;
	}

	public List<String> getRolesForUser(String uid) {
		//System.out.println("SSO::getRolesForUser");
		Set<Entry<String, List<SSOUserInfo>>> entrySet = roleUsers.entrySet();
		List<String> roles = new ArrayList<String>();
		
		for (Entry<String,List<SSOUserInfo>> entry : entrySet) {
			String role = entry.getKey();
			List<SSOUserInfo> users = entry.getValue();
			for (SSOUserInfo u : users) {
				if (u.getUsername().equals(uid)) {
					roles.add(role);
				}
			}
		}
		return roles;

	}

	public List<String> getPropertyForUser(String userId, String property) {
		return null;
	}

	public List<String> getModuleIds() {
		throw new RuntimeException("Not implemented");
	}

	public List<SSOUserInfo> getAllActiveUsers() {
		return null;
	}

}
