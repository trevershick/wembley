package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.legacy.subscriptions.Subscription;

public class DefaultSubscriptionLoaderImpl implements SubscriptionLoader {

	private static final Logger log = LoggerFactory.getLogger(DefaultSubscriptionLoaderImpl.class);
	private String appId;
	private SubscriptionDao subscriptionDao;

	public DefaultSubscriptionLoaderImpl(String appId) {
		this.appId = appId;
		log.info(String.format("Instantiating DefaultSubscriptionLoaderImpl with AppId %s", appId));
	}

	public List<Subscription> loadSubscriptions(Event event) {
		List<Subscription> subs = null;

		if(log.isDebugEnabled()) {
			log.debug(String.format("Loading subscriptions using the DefaultSubscriptionLoader with AppId %s for an event with AppId of %s",
					this.appId, event != null && event.getEventHeader() != null ? event.getEventHeader().getAppId() : null));
			log.debug("SubscriptionDao=" + this.subscriptionDao);
		}

		if(subscriptionDao != null && event != null && event.getEventHeader() != null) {
			subs = this.subscriptionDao.getActiveSubscriptionsForApp(event.getEventHeader().getAppId());
			if(subs == null) {
				subs = new ArrayList<Subscription>();
			}
		}

		return subs;
	}

	public String getAppId() {
		return appId;
	}

	public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
		this.subscriptionDao = subscriptionDao;
		log.debug("Setting the SubscriptionDao");
	}
}
