package com.railinc.wembley.legacy.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.EventVo;
import com.railinc.wembley.api.notifications.model.Notification;
import com.railinc.wembley.api.notifications.model.NotificationVo;
import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacy.senders.EmailSenderService;
import com.railinc.wembley.legacy.subscriptions.Subscription;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class SubscriptionLoaderNoSubsAdminEmailHandlerImpl implements SubscriptionLoaderHandler {

	private static final Logger log = LoggerFactory.getLogger(SubscriptionLoaderNoSubsAdminEmailHandlerImpl.class);
	private ApplicationDao applicationDao;
	private EmailSenderService emailSender;

	public void handleSubscriptionsLoaded(Class<?> subLoaderType, Event event, List<Subscription> subs, String subscriptionInfo) {
		if(emailSender == null) {
			throw new IllegalStateException("No email sender has been defined");
		}
		if(event != null && event.getEventHeader() != null) {
			if(subs == null || subs.isEmpty()) {
				ApplicationVo app = this.applicationDao == null ? null : this.applicationDao.getApplication(event.getEventHeader().getAppId());
				if(app != null) {
					Notification not = createAdminNotification(subLoaderType, app, event, subscriptionInfo);
					emailSender.sendNotification(not);
				} else {
					log.warn(String.format("SubscriptionLoaderNoSubsAdminEmailHandlerImpl called with a non-existant APP ID %s", event.getEventHeader().getAppId()));
				}
			}
		} else {
			log.warn("SubscriptionLoaderNoSubsAdminEmailHandlerImpl called with a null event or an event with a null header");
		}
	}

	private Notification createAdminNotification(Class<?> subLoaderType, ApplicationVo app, Event event, String subscriptionInfo) {
		NotificationVo not = new NotificationVo();
		not.setAppId(event.getEventHeader().getAppId());
		Event adminEvent = createAdminEvent(subLoaderType, app, event, subscriptionInfo);
		not.setDeliverySpec(adminEvent.getEventHeader().getDeliverySpecs().getDeliverySpecs().get(0));
		not.setEvent(adminEvent);
		return not;
	}

	private Event createAdminEvent(Class<?> subLoaderType, ApplicationVo app, Event event, String subscriptionInfo) {
		EventVo adminEvent = new EventVo(app.getAppId(), event.getEventHeader().getCorrelationId());

		StringBuffer body = new StringBuffer();
		body.append(String.format("The event with correlation ID %s did not match any of the requested subscriptions using the Subscription Loader %s\n",
				event.getEventHeader().getCorrelationId(), subLoaderType));
		body.append(String.format("Details: %s", subscriptionInfo));
		adminEvent.setTextBody(body.toString());

		EmailDeliverySpecVo spec = new EmailDeliverySpecVo();
		spec.setFrom("no-reply-notifserv@railinc.com");
		spec.setContentType("text/plain");
		spec.setSubject(String.format("Event for Correlation ID %s did not match one of the requested subscripitons", event.getEventHeader().getCorrelationId()));
		spec.setTo(app.getAdminEmail());
		adminEvent.getEventHeader().addDeliverySpec(spec);

		return adminEvent;
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public void setEmailSender(EmailSenderService emailSender) {
		this.emailSender = emailSender;
	}
}

