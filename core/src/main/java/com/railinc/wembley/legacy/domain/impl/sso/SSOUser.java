package com.railinc.wembley.legacy.domain.impl.sso;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.SubscriberBaseImpl;

public class SSOUser extends SubscriberBaseImpl { //implements Comparable<Subscriber> {
	private static final String ERROR_SSO_CANNOT_BE_NULL = "sso cannot be null";
	public static final String REALM = "SSO";
	private Set<String> ssoModuleIds;
	private Set<String> ssoRoles;
	private SSO sso;

	public SSOUser(SSO sso, String ssoUserId, Delivery delivery, String deliveryArg) {
		super(REALM,ssoUserId, delivery,deliveryArg);
		if (null == sso) {
			throw new IllegalArgumentException(ERROR_SSO_CANNOT_BE_NULL);
		}
		this.sso = sso;
	}
	
	public SSOUser(SSO sso, String ssoUserId) {
		super(REALM,ssoUserId);
		if (null == sso) {
			throw new IllegalArgumentException(ERROR_SSO_CANNOT_BE_NULL);
		}
		this.sso = sso;
	}
	
	public boolean areRolesPopulated() {
		return ssoRoles != null;
	}
	public boolean areModulesPopulated() {
		return ssoModuleIds != null;
	}
	public void addSsoRoles(List<String> roles) {
		if (this.ssoRoles == null) {
			this.ssoRoles = new HashSet<String>();
		}
		if (null != roles) {
			this.ssoRoles.addAll(roles);
		}
	}
	public void addSsoRole(String role) {
		if (this.ssoRoles == null) {
			this.ssoRoles = new HashSet<String>();
		}
		this.ssoRoles.add(role);
	}
	
	public void addSsoModuleIds(List<String> moduleIds) {
		if (this.ssoModuleIds == null) {
			this.ssoModuleIds = new HashSet<String>();
		}
		if (null != moduleIds) {
			this.ssoModuleIds.addAll(moduleIds);
		}
	}
	public void addSsoModuleId(String moduleId) {
		if (this.ssoModuleIds == null) {
			this.ssoModuleIds = new HashSet<String>();
		}
		this.ssoModuleIds.add(moduleId);
	}

	public Collection<String> getSsoRoles() {
		if (this.ssoRoles == null) {
			List<String> rolesForUser = sso.getRolesForUser(uid());
			this.ssoRoles = Collections.unmodifiableSet(new HashSet<String>(rolesForUser));
		}
		return this.ssoRoles;
	}
	public Collection<String> getSsoModuleIds() {
		if (ssoModuleIds == null) {
			List<String> appsForUser = sso.getAppsForUser(uid());
			this.ssoModuleIds = Collections.unmodifiableSet(new HashSet<String>(appsForUser));
		}
		return this.ssoModuleIds;
	}

	@Override
	public String deliveryArgument() {		
		return getDeliveryArgumentFromSso();
	}
	
	private String getDeliveryArgumentFromSso( ) {

		String deliveryStr = Delivery.EMAIL.equals( delivery() ) ? "emailAddress" : null;
		String deliveryArg = super.deliveryArgument();
		
		if ( deliveryStr != null && ( deliveryArg == null || deliveryArg.length() == 0 ) ) {
			
			List<String> props = sso.getPropertyForUser( uid(), deliveryStr );
			deliveryArg = props.size() > 0 ? props.get( 0 ) : null;
		}
		
		return deliveryArg;
	}
}
