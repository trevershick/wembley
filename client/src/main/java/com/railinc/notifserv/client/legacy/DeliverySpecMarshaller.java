package com.railinc.notifserv.client.legacy;

import com.railinc.notifserv.inbound.legacy.DeliverySpec;


public interface DeliverySpecMarshaller {

	String marshalDeliverySpec(DeliverySpec spec);
}
