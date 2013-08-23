package com.railinc.wembley.legacy.subscriptions.loader;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.Event;

public interface DeliverySpecValidator {

	boolean isDeliverySpecValid(String appId, Event event, DeliverySpec delSpec);
}
