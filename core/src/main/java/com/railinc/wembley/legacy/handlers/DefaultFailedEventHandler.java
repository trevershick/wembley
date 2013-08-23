package com.railinc.wembley.legacy.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.legacy.senders.AdminEmailFailedEventNotifier;

public class DefaultFailedEventHandler implements FailedEventHandler {

	private static final Logger log = LoggerFactory.getLogger(DefaultFailedEventHandler.class);
	private String appId;
	private AdminEmailFailedEventNotifier failedEventNotifier;

	public DefaultFailedEventHandler(String appId) {
		log.info("Instantiating the default failed event handler for app ID " + appId);
		this.appId = appId;
	}

	public void handleFailedEvent(String appId, Event event, FailedEventReason reason, Throwable e) {
		if(log.isDebugEnabled()) {
			log.debug(String.format("Handling failed event %s with reason %s and exception %s using the handler for appId %s", event, reason, e, this.appId));
		}
		if(event != null && appId != null && this.failedEventNotifier != null) {
			this.failedEventNotifier.sendAdminEmailForFailedEvent(appId, event, reason, e);
		}
	}

	public String getAppId() {
		return appId;
	}

	public void setFailedEventNotifier(AdminEmailFailedEventNotifier failedEventNotifier) {
		this.failedEventNotifier = failedEventNotifier;
	}
}
