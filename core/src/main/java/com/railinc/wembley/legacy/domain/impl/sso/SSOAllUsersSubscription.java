package com.railinc.wembley.legacy.domain.impl.sso;

import java.util.List;

import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptionBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class SSOAllUsersSubscription extends MailingListSubscriptionBaseImpl {

	
	private SSO sso;
	
	public SSOAllUsersSubscription(SSO sso, String subscriptionId, 
			Delivery del, 
			String deliveryArg, 
			SubscriptionMode mode, String mailingListKey) {
		super(subscriptionId,del,deliveryArg,mode,mailingListKey);
		if (null == sso) {
			throw new IllegalArgumentException("sso cannot be null");
		}

		this.sso = sso;
	}
	
	public void execute(Subscribers subscribers) {
		//List<String> moduleIds = sso.getModuleIds();
		//change to get all users 
		
		List<SSOUserInfo> imlicitUsers = sso.getAllActiveUsers();
		
		
		for (SSOUserInfo user : imlicitUsers) {
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
//		for (String moduleId : moduleIds) {
//			// pass in 'my' attributes so that the role memberihp rule
//			// will attach them to the users
//			new SSOModuleSubscription(sso, this.key(), moduleId, delivery, deliveryArgument, mode(),getMailingListKey()).execute(subscribers);
//		}
	}

	public boolean matches(Subscriber member) {
		if (member instanceof SSOUser) {
			return true;
		}
		return false;
	}
	public String toString() {
		return new StringBuilder("SSOAllUsersSubscription for ").append("(").append(super.toString()).append(")").toString();
	}

	public String description() {
		return "All SSO Users";
	}

	public String subscriptionDetails() {
		return null;
	}

	public String subscriptionType() {
		return "ssousers";
	}

}
