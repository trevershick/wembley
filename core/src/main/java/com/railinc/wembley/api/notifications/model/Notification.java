package com.railinc.wembley.api.notifications.model;

import java.io.Serializable;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.Event;

public interface Notification extends Serializable, Cloneable {

	String getNotificationUid();
	String getAppId();
	String getState();
	String getDeliveryTiming();
	Event getEvent();
	String getEventXml();
	DeliverySpec getDeliverySpec();
	int getRetryCount();
	Object clone() throws CloneNotSupportedException;
}
