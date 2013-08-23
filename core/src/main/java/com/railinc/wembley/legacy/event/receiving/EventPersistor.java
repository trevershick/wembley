package com.railinc.wembley.legacy.event.receiving;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.Event;

public interface EventPersistor extends NotificationService
{
	/**
	 * Persists an Application Event and the raw message XML into the Notification Service's
	 * own data store. The event should contain an Application ID which is a unique identifier
	 * for an application.
	 *  
	 *  @param event
	 *  @param eventXml
	 *  @return boolean
	 */
	boolean persistEvent( Event event, String eventXml );
	
	/**
	 * persist the xml from an event into the database in a FAILED state.
	 * @param appId
	 * @param correlationId
	 * @param eventXml
	 * @return
	 */
	boolean persistUnparseableEvent(String appId, String correlationId, String eventXml);
}
