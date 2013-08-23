package com.railinc.notifserv.client.legacy;

import com.railinc.notifserv.inbound.legacy.Event;

public interface EventMarshaller {

	String marshalEvent(Event spec);
}
