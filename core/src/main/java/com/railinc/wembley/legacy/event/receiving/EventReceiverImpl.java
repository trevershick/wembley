package com.railinc.wembley.legacy.event.receiving;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.core.NotificationServiceInvalidEventException;
import com.railinc.wembley.api.core.NotificationServiceRemoteException;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.api.event.parser.EventParser;
import com.railinc.wembley.api.util.EventBruteForceParser;
import com.railinc.wembley.api.util.ServiceLookupUtil;
import com.railinc.wembley.legacy.domain.dao.SystemConfigDao;
import com.railinc.wembley.legacy.handlers.FailedEventHandler;
import com.railinc.wembley.legacy.handlers.InvalidEventMsgHandler;
import com.railinc.wembley.legacy.subscriptions.loader.SubscriptionMatcherService;
import com.railinc.wembley.legacysvc.domain.SystemConfig;

public class EventReceiverImpl implements EventReceiver
{

	private static final Logger LOG = LoggerFactory.getLogger( EventReceiverImpl.class );

	public static final int MAX_RETRY_COUNT_DEFAULT = 3;
	public static final String MAX_RETRY_COUNT_KEY = "RETRY_MAX_RECEIVING";

	private String defaultAppId;
	private EventParser eventParser;
	private List<EventPersistor> eventPersistors;
	private List<InvalidEventMsgHandler> invalidEventMsgHandler;
	private List<SubscriptionMatcherService> subscriptionMatchers;
	private List<FailedEventHandler> failedEventHandlers;
	private List<EventFinalizer> eventFinalizers;
	private SystemConfigDao systemConfigDao;

	public EventReceiverImpl( String appId, 
			EventParser eventParser, 
			List<EventPersistor> eventPersistors, 
			List<SubscriptionMatcherService> subscriptionMatchers ) {
		
		this.defaultAppId    = appId;
		this.eventParser     = eventParser;
		this.eventPersistors = eventPersistors;
		this.subscriptionMatchers = subscriptionMatchers;
		LOG.info( String.format( "Initialized EventReceiver with AppId '%s', eventParser '%s', %d eventPersistors, %d subscriptionMatchers",
				appId, eventParser == null ? "null" : eventParser.getClass().getName(), eventPersistors == null ? 0 : eventPersistors.size(),
				subscriptionMatchers == null ? 0 : subscriptionMatchers.size()));
	}

	public void receiveEvent( String eventXml )
	{

		Event event = null;
		String appId = null;

		try {
			if ( eventXml == null || eventXml.length() == 0 ) {
				LOG.error( "Received null event from Queue" );
				throw new NotificationServiceInvalidEventException( "Event XML string is null or empty" );
			}
			
			if(eventParser == null) {
				throw new IllegalStateException("Event Parser has not been defined");
			}
			// Parse event XML
			try {
				event = eventParser.parseEvent( eventXml );
			} catch (NotificationServiceException e) {
				// it will come here if parsing fails.
				e.printStackTrace();
			}
			if(event == null || event.getEventHeader() == null) {
				// so attempt to persist the bad event if we can pull out 
				// an appid and correlation id. if we can then we're ok
				attemptBruteForceParsing(eventXml);
				throw new NotificationServiceInvalidEventException("Event was unparseable");
			}

			// Get event Application ID from header
			appId = event.getEventHeader().getAppId();

			//Get Persistor Service
			EventPersistor eventPersistor = (EventPersistor)ServiceLookupUtil.lookupService( appId, eventPersistors, true );
			if(LOG.isDebugEnabled()) {
				LOG.debug( String.format( "[%s] Retrieved EventPersistor service: %s", appId, eventPersistor ) );
			}

			//Handle no service
			if(eventPersistor == null) {
				throw new IllegalStateException(String.format("No Event Persistor service defined for AppID %s", appId));
			}
			
			if(LOG.isDebugEnabled()) {
				LOG.debug( String.format( "[%s] Attempting to persist Event with AppId '%s'", 
						appId, event.getEventHeader().getAppId() ) );
			}

			//Persist event
			boolean eventPersisted = eventPersistor.persistEvent( event, eventXml );

			if(LOG.isDebugEnabled()) {
				LOG.debug( String.format( "[%s] EventPersistor for AppId '%s' returned '%s'", appId, event.getEventHeader().getAppId(), eventPersisted ) );
			}

			//Send the event to the appropriate subscription matcher service
			SubscriptionMatcherService subMatcher = (SubscriptionMatcherService)ServiceLookupUtil.lookupService(appId, subscriptionMatchers, true);
			if(LOG.isDebugEnabled()) {
				LOG.debug( String.format( "[%s] Retrieved SubscriptionMatcher service: %s", appId, subMatcher ) );
			}

			if (subMatcher == null) {
				throw new NotificationServiceRemoteException(String.format("No Subscription Matcher service defined for AppID %s", appId));
			}

			subMatcher.matchSubscriptions(event);
			finalizeEvent(event, EventConstants.EVENT_STATUS_CLOSED);

		} catch (Throwable e) {
			LOG.error(String.format("Error receiving event: %s", eventXml), e);
			boolean eventMarkedForRetry = false;

			//Send failed event notifications out as configured for the application
			if(event != null && appId != null) {
				if(e instanceof NotificationServiceRemoteException) {
					eventMarkedForRetry = retryEvent(event, e);
				} else {
					handleFailedEvent(event, appId, e);
					finalizeEvent(event, EventConstants.EVENT_STATUS_FAILED);
				}
			}

			//Put the event on the poison message queue - TODO put in a retry count for non invalid event reasons
			if(invalidEventMsgHandler != null && !eventMarkedForRetry) {
				for (InvalidEventMsgHandler handler : invalidEventMsgHandler) {
					try {
						handler.handleInvalidEventMsg(eventXml, e);
					} catch (Throwable e2) {
						LOG.error("Error in InvalidEventMsgHandler", e2);
					}
				}
			} else {
				LOG.warn("No InvalidEventMsgHandler has been defined");
			}
		}
	}


	
	
	/**
	 * Attempt to pull out the app id and correlation id via brute force and
	 * persist the event in a failed state.
	 * @throws NotificationServiceInvalidEventException ALWAYS
	 */
	private void attemptBruteForceParsing(String eventXml) {
		EventBruteForceParser parser = new EventBruteForceParser();
		String appId = parser.extractAppId(eventXml);
		String corrId = parser.extractCorrelationId(eventXml);
		
		if (null == appId || null == corrId) {
			LOG.debug("Unable to extract appId or correlationId from " + eventXml);
			throw new NotificationServiceInvalidEventException("Unable to brute force parse the event xml");
		}
		
		
		EventPersistor eventPersistor = (EventPersistor)ServiceLookupUtil.lookupService( appId, eventPersistors, true );
		if(LOG.isDebugEnabled()) {
			LOG.debug( String.format( "[%s] Retrieved EventPersistor service: %s", appId, eventPersistor ) );
		}

		//Handle no service
		if(eventPersistor == null) {
			throw new IllegalStateException(String.format("No Event Persistor service defined for AppID %s", appId));
		}

		if(LOG.isDebugEnabled()) {
			LOG.debug( String.format( "[%s] Attempting to persist Event with AppId '%s'", appId, appId) );
		}


		// store the event even though it's not parseable.  We still want it to be 
		// stored so we can look it up. this really should store it in a failed state.
		eventPersistor.persistUnparseableEvent(appId, corrId, eventXml);
	}

	
	
	
	private void finalizeEvent(Event event, String finalState) {
		EventFinalizer finalizer = (EventFinalizer)ServiceLookupUtil.lookupService(event.getEventHeader().getAppId(), this.eventFinalizers, true);
		if(finalizer != null) {
			finalizer.finalizeEvent(event, finalState);
		} else {
			LOG.warn("No valid EventFinalizer has been defined, Event will not be finalized");
		}
	}

	private void handleFailedEvent(Event event, String appId, Throwable e) {
		FailedEventHandler failedEventHandler = (FailedEventHandler)ServiceLookupUtil.lookupService(appId, this.failedEventHandlers, true);
		if(failedEventHandler != null) {
			try {
				failedEventHandler.handleFailedEvent(appId, event, FailedEventReason.EXCEPTION_RECEIVING_EVENT, e);
			} catch (Throwable e2) {
				LOG.error("Error in FailedEventHandler", e2);
			}
		}
	}

	private boolean retryEvent(Event event, Throwable e) {
		int maxRetryCount = getMaxRetryCountForReceiving();
		boolean eventMarkedForRetry = false;

		if(LOG.isDebugEnabled()) {
			LOG.debug(String.format("Marking the event for retry (%d): %s", maxRetryCount, event));
		}

		if(event.getRetryCount() > maxRetryCount) {
			LOG.info(String.format("The event %s (%s) has exceeded the maximum retry count and will be marked as failed",
								event.getEventUid(), event.getEventHeader().getAppId()));
			try {
				handleFailedEvent(event, event.getEventHeader().getAppId(), e);
				finalizeEvent(event, EventConstants.EVENT_STATUS_FAILED);
			} catch (Throwable e2) {
				LOG.error("Error trying to marked event as failed", e2);
			}
		} else {
			LOG.info(String.format("Marking the event %s (%s) for retry", event.getEventUid(), event.getEventHeader().getAppId()));
			EventFinalizer finalizer = (EventFinalizer)ServiceLookupUtil.lookupService(event.getEventHeader().getAppId(), this.eventFinalizers, true);
			if(finalizer != null) {
				try {
					finalizer.retryEvent(event);
					eventMarkedForRetry = true;
				} catch (Throwable e2) {
					LOG.error("Error trying to mark event for retry", e2);
				}
			} else {
				LOG.warn("No valid EventFinalizer has been defined, Event retry will not be incremented");
			}
		}
		return eventMarkedForRetry;
	}

	private int getMaxRetryCountForReceiving() {
		return systemConfigValueAsInt(MAX_RETRY_COUNT_KEY, MAX_RETRY_COUNT_DEFAULT);
	}
	
	

	private int systemConfigValueAsInt(String key, int defaultValue) {
		if (null == systemConfigDao) { 
			return defaultValue;
		}
		
		try {
			SystemConfig config = systemConfigDao.getSystemConfig(key);
			
			if(config == null || !StringUtils.isNumeric(config.getValue())) {
				return defaultValue;
			}
			return Integer.parseInt(config.getValue());
		} catch (Throwable e) {
			LOG.error("Exception trying to get " + key + " as an integer, using the default " + defaultValue, e);
			return defaultValue;
		}
	}

	public String getAppId()
	{
		return defaultAppId;
	}

	public void setInvalidEventMsgHandler(List<InvalidEventMsgHandler> invalidEventMsgHandler) {
		this.invalidEventMsgHandler = invalidEventMsgHandler;
	}

	public void setFailedEventHandlers(List<FailedEventHandler> failedEventHandlers) {
		this.failedEventHandlers = failedEventHandlers;
	}

	public void setEventFinalizers(List<EventFinalizer> eventFinalizers) {
		this.eventFinalizers = eventFinalizers;
	}

	public void setSystemConfigDao(SystemConfigDao systemConfigDao) {
		this.systemConfigDao = systemConfigDao;
	}

}
