package com.railinc.wembley.legacy.domain.dao;

import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.SubscriptionClass;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public interface MailingListSubscriptionDao {
	MailingListSubscriptions getSubscriptionsForMailingList(String mailingListId);

	String include(String mailingListId, SubscriptionClass subscriptionClass, String subscriptionTypeArg);
	String exclude(String mailingListId, SubscriptionClass subscriptionClass, String subscriptionTypeArg);

	boolean deleteSubscription(String subscriptionId);

	void updateMode(String subscriptionId, SubscriptionMode inclusion);

	void mailingListKeyChanged(String oldKey, String newKey);

	MailingListSubscription getSubscription(String subscriptionKey);

	MailingListSubscription insert(String mailingListKey, SubscriptionMode mode, SubscriptionClass type,
			String typeArgument, String deliveryArgument);
}
