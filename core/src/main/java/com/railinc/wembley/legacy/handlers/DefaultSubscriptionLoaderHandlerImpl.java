package com.railinc.wembley.legacy.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.legacy.subscriptions.Subscription;

public class DefaultSubscriptionLoaderHandlerImpl implements SubscriptionLoaderHandler {

	private static final Logger log = LoggerFactory.getLogger(DefaultSubscriptionLoaderHandlerImpl.class);

	public void handleSubscriptionsLoaded(Class<?> subLoaderType, Event event, List<Subscription> subs, String subscriptionInfo) {
		if(event != null) {
			if(subs == null || subs.isEmpty()) {
				log.info(String.format("The event %s matched no subscriptions for subscription loader %s\nSubscription Info: %s",
								event, subLoaderType, subscriptionInfo));
			} else {
				if(log.isDebugEnabled()) {
					log.debug(String.format("The event %s matched the folloiwng subscriptions using the subscription loader %s:\n%s\nSubscription Info: %s",
							event, subLoaderType, subs, subscriptionInfo));
				}
			}
		} else {
			log.warn(String.format("Null Event Was Given to the Subscription Loader %s\nSubscription Info: %s", subLoaderType, subscriptionInfo));
		}
	}
}
