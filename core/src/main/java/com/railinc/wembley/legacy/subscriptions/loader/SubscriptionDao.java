package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.List;

import com.railinc.wembley.legacy.subscriptions.Subscription;

public interface SubscriptionDao {

	List<Subscription> getActiveSubscriptionsForApp(String appId);
}
