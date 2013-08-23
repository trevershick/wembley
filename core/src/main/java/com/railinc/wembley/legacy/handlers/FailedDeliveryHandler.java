package com.railinc.wembley.legacy.handlers;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.notifications.model.Notification;

public interface FailedDeliveryHandler extends NotificationService {

	void handleFailedDelivery(Notification notification, Throwable e);
}
