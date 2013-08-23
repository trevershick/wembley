package com.railinc.wembley.legacy.event.receiving;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.Event;

public interface EventFinalizer extends NotificationService {

	boolean finalizeEvent(Event event, String finalState);
	boolean retryEvent(Event event);
}
