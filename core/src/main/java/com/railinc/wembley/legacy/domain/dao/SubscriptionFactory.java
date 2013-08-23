package com.railinc.wembley.legacy.domain.dao;

import com.railinc.wembley.legacy.domain.impl.basic.BasicUserSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOAllUsersSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSOModuleSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSORoleSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserSubscription;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptionBaseImpl;
import com.railinc.wembley.legacysvc.domain.SubscriptionClass;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class SubscriptionFactory {
	private SSO sso;
	
	
	public SSO getSso() {
		return sso;
	}


	public void setSso(SSO sso) {
		this.sso = sso;
	}


	public MailingListSubscriptionBaseImpl create(String uid,
			String mailingListKey, 
			String subclassType, 
			String argument,
			SubscriptionMode mode, 
			Delivery d, 
			String deliveryarg) {
		
		MailingListSubscriptionBaseImpl mlb = null;
		
		if (SubscriptionClass.SSOUsersInRole.name().equals(subclassType)) {
			mlb = new SSORoleSubscription(getSso(),uid, argument,d,deliveryarg,mode,mailingListKey);
		} else if (SubscriptionClass.SSOUser.name().equals(subclassType)) {
			mlb =  new SSOUserSubscription(getSso(),uid, argument,d,deliveryarg,mode,mailingListKey);
		} else if (SubscriptionClass.SSOAppUsers.name().equals(subclassType)) {
			mlb = new SSOModuleSubscription(getSso(),uid, argument,d,deliveryarg,mode,mailingListKey);
		} else if (SubscriptionClass.SSOAllUsers.name().equals(subclassType)) {
			mlb = new SSOAllUsersSubscription(getSso(),uid, d,deliveryarg,mode,mailingListKey);
		} else if (SubscriptionClass.BasicUser.name().equals(subclassType)) {
			mlb = new BasicUserSubscription(uid, d,deliveryarg,mode,mailingListKey);
		}
		return mlb;
	}
}
