package com.railinc.wembley.legacy.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.api.notifications.model.Notification;
import com.railinc.wembley.legacy.senders.AdminEmailFailedEventNotifier;

public class DefaultFailedDeliveryHandler implements FailedDeliveryHandler{

	private Logger log = LoggerFactory.getLogger(DefaultFailedDeliveryHandler.class);
	private AdminEmailFailedEventNotifier failedDeliveryNotifier;
	private String appId;

	public DefaultFailedDeliveryHandler(String appId) {
		this.appId = appId;
		log.info(String.format("Instantiating the DefaultFailedDeliveryHandlerImpl with AppID %s", appId));
	}

	public void handleFailedDelivery(Notification notification, Throwable e) {
		if(notification != null && this.failedDeliveryNotifier != null) {
			try {
				this.failedDeliveryNotifier.sendAdminEmailForFailedEvent(notification.getAppId(), notification.getEvent(), FailedEventReason.EXCEPTION_DELIVERING_NOTIFICATION, e);
			} catch (Throwable ex) {
				log.error("Exception trying to send admin email for event with no matching subscriptions", ex);
			}
		}
		log.warn(String.format("Error trying to deliver the notification %s", notification), e);
	}

	public void setFailedDeliveryNotifier(AdminEmailFailedEventNotifier failedDeliveryNotifier) {
		this.failedDeliveryNotifier = failedDeliveryNotifier;
	}

	public String getAppId() {
		return this.appId;
	}
}
