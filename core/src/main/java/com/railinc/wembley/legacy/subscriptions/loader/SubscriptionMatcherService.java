package com.railinc.wembley.legacy.subscriptions.loader;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.Event;

public interface SubscriptionMatcherService extends NotificationService {

	void matchSubscriptions(Event event);
}
