package com.railinc.wembley.legacy.senders;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.notifications.model.Notification;


public interface SenderService extends NotificationService
{
	/**
	 * Sends the given notification via a particular (implemented) transport
	 * 
	 * @param notification
	 */
	void sendNotification(Notification notification);
	
	/**
	 * This method is used to identify this class as a particular sender. The lookup service will
	 * call this method when attempting to find specific senders.
	 */
	Class<? extends DeliverySpec> getDeliverySpecType();
}
