package com.railinc.wembley.legacy.domain.impl.sso;

import java.util.Collection;
import java.util.List;

import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptionBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class SSORoleSubscription extends MailingListSubscriptionBaseImpl {

	private SSO sso;
	private String roleName;
	/**
	 * this is only used by the SSOModuleSubscription to support nesting, leave this as package
	 * level protection (mode() should never be called on this)
	 * @param s
	 * @param ssoRoleName
	 * @param d
	 * @param deliveryArg
	 * @param attributes
	 */
	public SSORoleSubscription(SSO sso, String subscriptionId,String ssoRoleName, Delivery d, String deliveryArg, SubscriptionMode mode,String mailingListKey) {
		super(subscriptionId, d,deliveryArg,mode,mailingListKey);
		
		if (null == sso) {
			throw new IllegalArgumentException("sso cannot be null");
		}
	
		this.sso = sso;
		this.roleName = ssoRoleName;
		

	}
	
	
	public void execute(Subscribers subscribers) {
		List<SSOUserInfo> usersInRole = sso.getUsersInRole(roleName);
		if (usersInRole.isEmpty()) {
			return;
		}
		
		for (SSOUserInfo user : usersInRole) {
			// this is 'implicit' because it comes from an
			// expansion
			
			String dArg = deliveryArgument();
			if (Delivery.EMAIL.equals(delivery()) && (null == deliveryArgument() || 0 == deliveryArgument().trim().length()) ) {
				 dArg = user.getEmail();
			}
			
			SSOUser implicitUser = new SSOUser(sso,user.getUsername(),delivery(),dArg);
			
			if (mode().equals(SubscriptionMode.INCLUSION)) {
				subscribers.add(implicitUser);
			} else if (SubscriptionMode.EXCLUSION.equals(mode())) {
				subscribers.remove(implicitUser);
			}
			
		}
	}

	public boolean matches(Subscriber member) {
		if (member instanceof SSOUser) {
			SSOUser u = (SSOUser) member;
			Collection<String> roles = u.getSsoRoles();
			
			return roles.contains(roleName);
		}
		return false;
	}
	public String toString() {
		return new StringBuilder("SSORoleSubscription for ").append(this.roleName).append("(").append(super.toString()).append(")").toString();
	}
	

	public String description() {
		return "User in SSO Role " + roleName;
	}

	public String subscriptionDetails() {
		return this.roleName;
	}

	public String subscriptionType() {
		return "ssorole";
	}
}
