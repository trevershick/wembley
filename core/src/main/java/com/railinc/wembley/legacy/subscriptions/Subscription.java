package com.railinc.wembley.legacy.subscriptions;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.Event;

public interface Subscription extends NotificationService {

	boolean isMatch(Event event);
	DeliverySpec getDeliverySpec();
	String getDeliveryTiming();
}
