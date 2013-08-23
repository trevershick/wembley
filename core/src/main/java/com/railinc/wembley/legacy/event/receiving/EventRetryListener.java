package com.railinc.wembley.legacy.event.receiving;

import com.railinc.wembley.api.core.NotificationService;

public interface EventRetryListener extends NotificationService {

	void retryEvents(String retryMsg);
}
