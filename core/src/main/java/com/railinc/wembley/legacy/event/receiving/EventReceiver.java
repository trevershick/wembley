package com.railinc.wembley.legacy.event.receiving;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.core.NotificationServiceException;

public interface EventReceiver extends NotificationService {

	/**
	 * Receives an event from some medium (default is MQ). Basic behavior should
	 * include searching for an event persistor to use for persisting the event (although
	 * that decision is left to the implementors of this interface).
	 *
	 * @param eventXml
	 * @throws NotificationServiceException
	 */
	void receiveEvent(String eventXml);
}
