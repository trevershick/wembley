package com.railinc.wembley.legacy.handlers;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.Event;

public interface NoMatchingSubscriptionsHandler extends NotificationService {

	void handleNoMatchingSubscriptions(Event event);
}
