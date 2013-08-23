package com.railinc.wembley.api.event.parser;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.EventHeader;
import com.railinc.wembley.api.event.EventParameter;
import com.railinc.wembley.api.event.EventVo;
import com.railinc.wembley.api.util.JaxbUtils;

public class EventParserImpl implements EventParser {

	private static final Logger log = LoggerFactory.getLogger(EventParserImpl.class);
	private JAXBContext jaxbCtx;

	public EventParserImpl() {
		log.info("Instantiating the EventParserImpl");
		this.jaxbCtx = JaxbUtils.getEventJaxbContext();
	}

	public Event parseEvent(String eventXml) {

		if(eventXml == null) {
			log.error("EventParser.parseEvent(String) called with a null String");
			throw new NotificationServiceException("Event Parsing Exception - Event XML cannot be null");
		}

		EventVo event = (EventVo)JaxbUtils.unmarshal(this.jaxbCtx, eventXml);
		checkHeaderValues(event);
		
		return event;
	}

	public Event parseEvent(InputStream eventXml) {
		if(eventXml == null) {
			log.error("EventParser.parseEvent(InputStream) called with a null InputStream");
			throw new NotificationServiceException("Event Parsing Exception - Event XML cannot be null");
		}

		EventVo event = (EventVo)JaxbUtils.unmarshal(this.jaxbCtx, eventXml);
		checkHeaderValues(event);
		return event;
	}
	
	
	protected void checkHeaderValues(Event event) {
		if (null == event) {
			return;
		}
		EventHeader header = event.getEventHeader();
		if (null == header) {
			return;
		}
		List<? extends EventParameter> params = header.getEventParams();
		if (params == null || params.size() == 0) {
			return;
		}
		for (EventParameter p : params) {
			if (Event.SEND_AFTER.equals(p.getParamName())) {
				attemptToParseDate(p.getParamValue());
			}
		}
	}

	protected void attemptToParseDate(String paramValue) {
		try {
			XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(paramValue);
		} catch (Exception e) {
			log.error("unable to parse date from header values (" + paramValue + ")", e);
			throw new NotificationServiceException("Unable to parse " + paramValue + " as a date");
		}
	}
	
	
}
