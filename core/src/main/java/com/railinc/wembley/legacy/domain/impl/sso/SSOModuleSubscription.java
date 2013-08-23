package com.railinc.wembley.legacy.domain.impl.sso;

import java.util.Collection;
import java.util.List;

import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptionBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class SSOModuleSubscription extends MailingListSubscriptionBaseImpl {
	private SSO sso;
	private String moduleId;

	
	public SSOModuleSubscription(SSO sso, 
			String subscriptionId, 
			String ssoModuleId,
			Delivery del, 
			String deliveryArg, 
			SubscriptionMode mode, 
			String mailingListKey) {
		
		super(subscriptionId,del,deliveryArg,mode,mailingListKey);
		if (null == sso) {
			throw new IllegalArgumentException("sso cannot be null");
		}

		this.sso = sso;
		this.moduleId = ssoModuleId;
	}
	
	public void execute(Subscribers subscribers) {
		List<String> rolesForApp = sso.getRolesForApp(moduleId);
		if (rolesForApp == null) {
			return;
		}
		
		for (String role : rolesForApp) {
			// pass in 'my' attributes so that the role memberihp rule
			// will attach them to the users
			new SSORoleSubscription(sso, this.key(), role, delivery(), deliveryArgument()
					, mode(),getMailingListKey()).execute(subscribers);
		}
	}

	public boolean matches(Subscriber member) {
		if (member instanceof SSOUser) {
			SSOUser u = (SSOUser) member;
			Collection<String> apps = u.getSsoModuleIds();
			return apps.contains(moduleId);
		}
		return false;
	}
	public String toString() {
		return new StringBuilder("SSOModuleSubscription for ").append(this.moduleId).append("(").append(super.toString()).append(")").toString();
	}

	public String description() {
		return "User of SSO Application " + moduleId;
	}

	public String subscriptionDetails() {
		return this.moduleId;
	}

	public String subscriptionType() {
		return "ssoapp";
	}

}
