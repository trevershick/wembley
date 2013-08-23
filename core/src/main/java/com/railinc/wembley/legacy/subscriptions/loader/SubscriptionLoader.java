package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.List;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.legacy.subscriptions.Subscription;

public interface SubscriptionLoader extends NotificationService {

	List<Subscription> loadSubscriptions(Event event);
}
