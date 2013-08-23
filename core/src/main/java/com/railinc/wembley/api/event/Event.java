package com.railinc.wembley.api.event;

import java.io.Serializable;

public interface Event extends Serializable, Cloneable {
	String SEND_AFTER = "{http://events.notifserv.railinc.com}SendAfter";
	String getEventUid();
	void setEventUid( String eventUid );
	EventHeader getEventHeader();
	EventBody getEventBody();
	int getRetryCount();
	Object clone() throws CloneNotSupportedException;
}
