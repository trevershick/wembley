package com.railinc.wembley.legacy.handlers;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FailedEventReason;

public interface FailedEventHandler extends NotificationService {

	void handleFailedEvent(String appId, Event event, FailedEventReason reason, Throwable e);
}
