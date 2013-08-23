package com.railinc.wembley.domain.test.mocks;

import java.util.ArrayList;
import java.util.List;

import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserInfo;
import com.railinc.wembley.legacysvc.domain.SubscriptionClass;

public class MockSso implements SSO {

	private String role;
	private String email;
	private SubscriptionClass subClass;
	
	public static String MOCK_USER = "MOCKUSER";
	
	public MockSso( SubscriptionClass subClass, String role, String email ) {
	
		this.role = role;
		this.email = email;
		this.subClass = subClass;
	}
	
	public List<String> getAppsForUser(String uid) {
		return null;
	}

	public List<String> getPropertyForUser(String userId, String property) {

		List<String> users = new ArrayList<String>();
		users.add( email );
		
		return users;
	}

	public List<String> getRolesForApp(String appName) {

		List<String> roles = new ArrayList<String>();
		roles.add( role );
		
		return roles;
	}

	public List<String> getRolesForUser(String uid) {
		
		List<String> users = new ArrayList<String>();
		users.add( role );
		
		return users;
	}

	public List<SSOUserInfo> getUsersInRole(String roleName) {

		List<SSOUserInfo> users = new ArrayList<SSOUserInfo>();
		SSOUserInfo u = new SSOUserInfo();
		u.setUsername(MOCK_USER);
		users.add( u );
		
		return users;
	}

	public List<String> getModuleIds() {
		return new ArrayList<String>();
	}

	public List<SSOUserInfo> getAllActiveUsers() {
		return null;
	}
}
