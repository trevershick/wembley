package com.railinc.wembley.api.event.parser;

import java.io.InputStream;

import com.railinc.wembley.api.event.Event;

public interface EventParser {

	Event parseEvent(String eventXml);
	Event parseEvent(InputStream eventXml);
}
