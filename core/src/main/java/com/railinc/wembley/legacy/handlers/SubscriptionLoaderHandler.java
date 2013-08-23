package com.railinc.wembley.legacy.handlers;

import java.util.List;

import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.legacy.subscriptions.Subscription;

public interface SubscriptionLoaderHandler {

	void handleSubscriptionsLoaded(Class<?> subLoaderType, Event event, List<Subscription> subs, String subscriptionInfo);
}
