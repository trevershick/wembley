package com.railinc.wembley.legacy.domain.impl.sso;

import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptionBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class SSOUserSubscription extends MailingListSubscriptionBaseImpl {
	private SSO sso;
	private String ssoUserId;
	
	public SSOUserSubscription(SSO sso, String subscriptionId,String uid, Delivery del, String deliveryArg, SubscriptionMode mode, String mailingListKey) {
		super(subscriptionId, del,deliveryArg,mode,mailingListKey);
		if (null == sso) {
			throw new IllegalArgumentException("sso cannot be null");
		}

		this.sso = sso;
		this.ssoUserId = uid;
	}
	
	public String toString() {
		return new StringBuilder("SSOUserSubscription for ").append(ssoUserId).append("(").append(super.toString()).append(")").toString();
	}
	
	public void execute(Subscribers subscribers) {
		SSOUser explicit = new SSOUser(sso,ssoUserId,delivery(),deliveryArgument());
		// remove any sso users that match this user because they may
		// be implicit, since tis is explicit we want to override any
		// that are already there. groups and app rules shoulnd't 
		// override any
		if (mode().equals(SubscriptionMode.INCLUSION)) {
			subscribers.remove(explicit);
			subscribers.add(explicit);
		} else if (SubscriptionMode.EXCLUSION.equals(mode())) {
			subscribers.remove(explicit);
		}
	}

	
	public boolean matches(Subscriber member) {
		if (member instanceof SSOUser) {
			SSOUser ssou = (SSOUser) member;
			return ssoUserId.equals(ssou.uid()) && SSOUser.REALM.equals(ssou.realm());
		}
		return false;
	}
	public boolean explicit() {
		return true;
	}


	public String description() {
		return "SSO User " + this.ssoUserId;
	}

	public String subscriptionDetails() {
		return this.ssoUserId;
	}

	public String subscriptionType() {
		return "ssouser";
	}
}
