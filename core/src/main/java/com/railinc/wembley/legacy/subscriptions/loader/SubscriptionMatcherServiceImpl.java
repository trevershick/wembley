package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.core.NotificationServiceRemoteException;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.api.util.ServiceLookupUtil;
import com.railinc.wembley.legacy.handlers.FailedEventHandler;
import com.railinc.wembley.legacy.handlers.NoMatchingSubscriptionsHandler;
import com.railinc.wembley.legacy.subscriptions.Subscription;

public class SubscriptionMatcherServiceImpl implements SubscriptionMatcherService {

	private static final Logger log = LoggerFactory.getLogger(SubscriptionMatcherServiceImpl.class);

	private String appId;
	private List<SubscriptionLoader> subscriptionLoaders;
	private List<NotificationPersistor> notificationPersistors;
	private List<NoMatchingSubscriptionsHandler> noMatchingSubscriptionsHandlers;
	private List<FailedEventHandler> failedEventHandlers;

	public SubscriptionMatcherServiceImpl(String appId) {
		this.appId = appId;
		log.info(String.format("Instantiating the SubscriptionMatcherServiceImpl with AppId %s", appId));
	}

	public void matchSubscriptions(Event event) {

		if(log.isDebugEnabled()) {
			log.debug(String.format("%s: Matching subscriptions with SubscripitonMatcherServiceImpl with AppId %s for Event\n: %s",
					event == null ? null : event.getEventUid(), appId, event));
		}

		if(event != null && event.getEventHeader() != null) {
			String eventAppId = event.getEventHeader().getAppId();

			List<? extends NotificationService> loaders = ServiceLookupUtil.lookupServices(eventAppId, this.subscriptionLoaders);
			
			if(log.isDebugEnabled()) {
				log.debug(String.format("%s: Got %d SubscriptionLoaders", event.getEventUid(), loaders.size()));
			}
			/**
			 * If no loaders were found the normal way, then we're going to use the default subscription loaders.
			 */
			if (loaders.isEmpty()) {
				log.debug("Got no subscription loaders, use the default subscription loaders");
				loaders = ServiceLookupUtil.lookupDefaultServices(this.subscriptionLoaders);
			}

			
			List<Subscription> subs = new ArrayList<Subscription>();
			for(NotificationService loaderService : loaders) {
				if(log.isDebugEnabled()) {
					log.debug(String.format("%s: Loading subscriptions using the loader %s", event.getEventUid(), loaderService.getClass().getName()));
				}
				try {
					if(loaderService != null) {
						SubscriptionLoader loader = (SubscriptionLoader)loaderService;
						List<Subscription> loadedSubs = loader.loadSubscriptions(event);
						if(loadedSubs != null) {
							subs.addAll( loadedSubs );
						}
					}
				} catch (NotificationServiceRemoteException e) {
					throw e;
				} catch (Throwable e) {
					handleFailedEvent(eventAppId, event, FailedEventReason.EXCEPTION_LOADIING_SUBSCRIPTIONS, e);
				}
			}

			if(log.isDebugEnabled()) {
				log.debug(String.format("%s: Found %d Subscriptions", event.getEventUid(), subs.size()));
			}

			List<? extends NotificationService> persistors = ServiceLookupUtil.lookupServices( eventAppId, this.notificationPersistors, true );
			if(log.isDebugEnabled()) {
				log.debug(String.format("%s: Found %d NotificationPersistors for Matched Subscription", event.getEventUid(), persistors.size()));
			}

			if ( !subs.isEmpty() && !persistors.isEmpty() )
			{
				for(Subscription sub : subs)
				{
					if(log.isDebugEnabled()) {
						log.debug(String.format("%s: Matching Subscription %s", event.getEventUid(), sub));
					}
					if(sub.isMatch(event))
					{
						for ( NotificationService persistorService : persistors )
						{
							try {
								if(log.isDebugEnabled()) {
									log.debug(String.format("%s: Sending matched subscription %s to NotificationPersistor %s",
											event.getEventUid(), sub, persistorService.getClass().getName()));
								}
								NotificationPersistor notifPersistor = (NotificationPersistor)persistorService;
								notifPersistor.persistNotification( event, sub );
							} catch (Throwable e) {
								handleFailedEvent(eventAppId, event, FailedEventReason.EXCEPTION_POSTING_NOTIFICATION, e);
							}
						}
					}
				}
			} else {
				NotificationService noMatchingSubHandler = ServiceLookupUtil.lookupService(eventAppId, this.noMatchingSubscriptionsHandlers, true);
				if(noMatchingSubscriptionsHandlers != null) {
					((NoMatchingSubscriptionsHandler)noMatchingSubHandler).handleNoMatchingSubscriptions(event);
				} else {
					log.warn(String.format("The event for App ID %s had no matching subscriptions, but there was  no configured NoMatchingSubscriptionHandler: %s", eventAppId, event));
				}
			}
		}
	}

	public String getAppId() {
		return this.appId;
	}

	public void setSubscriptionLoaders(List<SubscriptionLoader> subscriptionLoaders) {
		this.subscriptionLoaders = subscriptionLoaders;
		log.debug("Setting the SubscriptionLoaders");
	}

	public void setNotificationPersistors( List<NotificationPersistor> notificationPersistors )
	{
		this.notificationPersistors = notificationPersistors;
		log.debug("Setting the Notification Persistors");
	}

	public void setNoMatchingSubscriptionsHandlers(List<NoMatchingSubscriptionsHandler> noMatchingSubscriptionsHandlers) {
		this.noMatchingSubscriptionsHandlers = noMatchingSubscriptionsHandlers;
	}

	public void setFailedEventHandlers(List<FailedEventHandler> failedEventHandlers) {
		this.failedEventHandlers = failedEventHandlers;
	}

	private void handleFailedEvent(String appId, Event event, FailedEventReason reason, Throwable e) {
		NotificationService failedEventHandler = ServiceLookupUtil.lookupService(appId, this.failedEventHandlers, true);
		if(failedEventHandler != null) {
			try {
				((FailedEventHandler)failedEventHandler).handleFailedEvent(appId, event, reason, e);
			} catch (Throwable e2) {
				log.error("Error in FailedEventHandler", e2);
			}
		} else {
			log.warn(String.format("The event %s for app ID %s failed, but there was no configured failed event handler", event, appId));
		}
	}
}
