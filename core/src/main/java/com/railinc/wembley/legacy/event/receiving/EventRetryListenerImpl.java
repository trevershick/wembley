package com.railinc.wembley.legacy.event.receiving;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationServiceRemoteException;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.api.util.ServiceLookupUtil;
import com.railinc.wembley.legacy.domain.dao.SystemConfigDao;
import com.railinc.wembley.legacy.handlers.FailedEventHandler;
import com.railinc.wembley.legacy.subscriptions.loader.SubscriptionMatcherService;
import com.railinc.wembley.legacysvc.domain.SystemConfig;

public class EventRetryListenerImpl implements EventRetryListener {

	private static final Logger log = LoggerFactory.getLogger(EventRetryListenerImpl.class);
	private String appId;
	private EventDao eventDao;
	private List<SubscriptionMatcherService> subscriptionMatchers;
	private List<FailedEventHandler> failedEventHandlers;
	private List<EventFinalizer> eventFinalizers;
	private SystemConfigDao systemConfigDao;

	public EventRetryListenerImpl(String appId) {
		this.appId = appId;
		log.info(String.format("Instantiated EventRetryListener for App ID %s", appId));
	}

	public void retryEvents(String retryMsg) {
		log.info("Retrying previously failed events");

		if(eventDao != null) {
			List<Event> events = eventDao.getEventsForRetry();
			if(events != null) {
				for(Event event : events) {
					log.info(String.format("Retrying the event: %s", event));
					String appId = event != null && event.getEventHeader() != null ? event.getEventHeader().getAppId() : null;
					try {
						//Send the event to the appropriate subscription matcher service
						SubscriptionMatcherService subMatcher = (SubscriptionMatcherService)ServiceLookupUtil.lookupService(appId, subscriptionMatchers, true);
						if(log.isDebugEnabled()) {
							log.debug( String.format( "[%s] Retrieved SubscriptionMatcher service: %s", appId, subMatcher ) );
						}

						if(subMatcher == null) {
							throw new NotificationServiceRemoteException(String.format("No Subscription Matcher service defined for AppID %s", appId));
						}

						subMatcher.matchSubscriptions(event);
						finalizeEvent(event, appId, "CLOSED");

					} catch (Throwable e) {
						log.error(String.format("Error retrying event: %s", event), e);

						//Send failed event notifications out as configured for the application
						if(event != null && appId != null) {
							if(e instanceof NotificationServiceRemoteException) {
								retryEvent(event, appId, e);
							} else {
								handleFailedEvent(event, appId, e);
								finalizeEvent(event, appId, "FAILED");
							}
						} else {
							finalizeEvent(event, appId, "FAILED");
						}
					}
				}
			}
		}
	}

	private void finalizeEvent(Event event, String appId, String finalState) {
		EventFinalizer finalizer = (EventFinalizer)ServiceLookupUtil.lookupService(appId, this.eventFinalizers, true);
		if(finalizer != null) {
			finalizer.finalizeEvent(event, finalState);
		} else {
			log.warn("No valid EventFinalizer has been defined, Event will not be finalized");
		}
	}

	private void handleFailedEvent(Event event, String appId, Throwable e) {
		FailedEventHandler failedEventHandler = (FailedEventHandler)ServiceLookupUtil.lookupService(appId, this.failedEventHandlers, true);
		if(failedEventHandler != null) {
			try {
				failedEventHandler.handleFailedEvent(appId, event, FailedEventReason.EXCEPTION_RECEIVING_EVENT, e);
			} catch (Throwable e2) {
				log.error("Error in FailedEventHandler", e2);
			}
		}
	}

	private void retryEvent(Event event, String appId, Throwable e) {
		int maxRetryCount = getMaxRetryCountForReceiving();

		if(log.isDebugEnabled()) {
			log.debug(String.format("Marking the event for retry (%d): %s", maxRetryCount, event));
		}

		if(event.getRetryCount() > maxRetryCount) {
			log.info(String.format("The event %s (%s) has exceeded the maximum retry count and will be marked as failed",
								event.getEventUid(), event.getEventHeader().getAppId()));
			try {
				handleFailedEvent(event, event.getEventHeader().getAppId(), e);
				finalizeEvent(event, appId, "FAILED");
			} catch (Throwable e2) {
				log.error("Error trying to marked event as failed", e2);
			}
		} else {
			log.info(String.format("Marking the event %s (%s) for retry", event.getEventUid(), event.getEventHeader().getAppId()));
			EventFinalizer finalizer = (EventFinalizer)ServiceLookupUtil.lookupService(event.getEventHeader().getAppId(), this.eventFinalizers, true);
			if(finalizer != null) {
				try {
					finalizer.retryEvent(event);
				} catch (Throwable e2) {
					log.error("Error trying to mark event for retry", e2);
				}
			} else {
				log.warn("No valid EventFinalizer has been defined, Event retry will not be incremented");
			}
		}
	}

	private int getMaxRetryCountForReceiving() {
		int maxRetryCount = EventReceiverImpl.MAX_RETRY_COUNT_DEFAULT;
		if(systemConfigDao != null) {
			try {
				SystemConfig config = systemConfigDao.getSystemConfig(EventReceiverImpl.MAX_RETRY_COUNT_KEY);
				if(config != null && StringUtils.isNumeric(config.getValue())) {
					maxRetryCount = Integer.parseInt(config.getValue());
				}
			} catch (Throwable e) {
				log.error("Exception trying to get the maximum retry count for delivery, using the default maximum of 3", e);
			}
		}
		return maxRetryCount;
	}

	public String getAppId() {
		return appId;
	}

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	public void setSubscriptionMatchers(List<SubscriptionMatcherService> subscriptionMatchers) {
		this.subscriptionMatchers = subscriptionMatchers;
	}

	public void setEventFinalizers(List<EventFinalizer> eventFinalizers) {
		this.eventFinalizers = eventFinalizers;
	}

	public void setFailedEventHandlers(List<FailedEventHandler> failedEventHandlers) {
		this.failedEventHandlers = failedEventHandlers;
	}

	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}
}
