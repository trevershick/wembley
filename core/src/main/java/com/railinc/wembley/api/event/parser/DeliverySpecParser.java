package com.railinc.wembley.api.event.parser;

import java.io.InputStream;

import com.railinc.wembley.api.event.DeliverySpec;

public interface DeliverySpecParser {

	DeliverySpec parseDeliverySpec(String xml);
	DeliverySpec parseDeliverySpec(InputStream in);
}
