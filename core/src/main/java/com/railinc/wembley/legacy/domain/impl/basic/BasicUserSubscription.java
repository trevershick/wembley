package com.railinc.wembley.legacy.domain.impl.basic;

import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptionBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class BasicUserSubscription extends MailingListSubscriptionBaseImpl {
	
	public BasicUserSubscription(String id, Delivery d, String deliveryArg, SubscriptionMode mode, String mailingListKey) {
		super(id, d,deliveryArg,mode,mailingListKey);
		
	}
	
	public void execute(Subscribers subscribers) {
		if (delivery() != null && deliveryArgument() != null) {
			subscribers.add(new BasicUser(delivery(),deliveryArgument()));
		}
	}

	
	public boolean matches(Subscriber member) {
		if (member instanceof BasicUser) {
			BasicUser ssou = (BasicUser) member;
			return toString().equals(ssou.toString());
		}
		return false;
	}

	public boolean explicit() {
		return true;
	}

	public String description() {
		return "Basic User " + deliveryArgument();
	}

	public String subscriptionDetails() {
		return deliveryArgument();
	}

	public String subscriptionType() {
		return "basicuser";
	}
}
