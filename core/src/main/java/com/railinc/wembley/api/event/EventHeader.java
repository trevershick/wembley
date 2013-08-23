package com.railinc.wembley.api.event;

import java.io.Serializable;
import java.util.List;

public interface EventHeader extends Serializable, Cloneable {

	String getAppId();
	String getCorrelationId();
	List<? extends EventParameter> getEventParams();
	DeliverySpecs getDeliverySpecs();
}
