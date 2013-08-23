package com.railinc.wembley.legacy.senders;

import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FailedEventReason;

public interface AdminEmailFailedEventNotifier {

	void sendAdminEmailForFailedEvent(String appId, Event event, FailedEventReason reason, Throwable e);
}
