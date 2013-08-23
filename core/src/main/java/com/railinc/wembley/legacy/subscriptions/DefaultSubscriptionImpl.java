package com.railinc.wembley.legacy.subscriptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.Event;

public class DefaultSubscriptionImpl implements Subscription {

	private static final Logger log = LoggerFactory.getLogger(DefaultSubscriptionImpl.class);

	private String appId;
	private DeliverySpec deliverySpec;
	private String deliveryTiming;

	public DefaultSubscriptionImpl(String appId) {
		this.appId = appId;
		if(log.isDebugEnabled()) {
			log.debug(String.format("Instantiating DefaultSubscriptionImpl with AppId %s", appId));
		}
	}

	public boolean isMatch(Event event) {
		return event != null;
	}

	public String getAppId() {
		return appId;
	}

	public DeliverySpec getDeliverySpec() {
		return deliverySpec;
	}

	public void setDeliverySpec(DeliverySpec deliverySpec) {
		this.deliverySpec = deliverySpec;
	}

	public String getDeliveryTiming() {
		return deliveryTiming;
	}

	public void setDeliveryTiming(String deliveryTiming) {
		this.deliveryTiming = deliveryTiming;
	}

	public String toString() {
		return String.format("Sub [%s:%s]: %s", appId, deliveryTiming, deliverySpec);
	}
}
