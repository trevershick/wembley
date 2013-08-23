package com.railinc.wembley.legacy.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.legacy.senders.AdminEmailFailedEventNotifier;

public class DefaultNoMatchingSubscriptionsHandler implements NoMatchingSubscriptionsHandler {

	private static final Logger log = LoggerFactory.getLogger(DefaultNoMatchingSubscriptionsHandler.class);
	private String appId;
	private AdminEmailFailedEventNotifier failedEventNotifier;

	public DefaultNoMatchingSubscriptionsHandler(String appId) {
		this.appId = appId;
		log.info("Instantiating the DefaultNoMatchingSubscriptionHandler for AppId: " + appId);
	}

	public void handleNoMatchingSubscriptions(Event event) {
		if(log.isDebugEnabled()) {
			log.debug("Handling a NoMatchingSubscription with the handler with App ID " + appId);
		}
		if(event != null && event.getEventHeader() != null && event.getEventHeader().getAppId() != null && failedEventNotifier != null) {
			try {
				this.failedEventNotifier.sendAdminEmailForFailedEvent(event.getEventHeader().getAppId(), event, FailedEventReason.NO_MATCHING_SUBSCRIPTIONS, null);
			} catch (Throwable e) {
				log.error("Exception trying to send admin email for event with no matching subscriptions", e);
			}
		}
		log.warn(String.format("No matching subscriptions were found for the event %s", event));
	}

	public String getAppId() {
		return appId;
	}

	public void setFailedEventNotifier(AdminEmailFailedEventNotifier failedEventNotifier) {
		this.failedEventNotifier = failedEventNotifier;
	}
}
