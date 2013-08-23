package com.railinc.notifserv.client.legacy;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.notifserv.inbound.legacy.Event;
import com.railinc.notifserv.jaxb.util.JaxbUtils;

public class EventMarshallerImpl implements EventMarshaller {

	private static final Logger log = LoggerFactory.getLogger(EventMarshallerImpl.class);

	private JAXBContext jaxbCtx;

	public EventMarshallerImpl() {
		log.info("Instantiating the EventMarshallerImpl");
		this.jaxbCtx = JaxbUtils.getEventJaxbContext();
	}

	public String marshalEvent(Event event) {
		String xmlStr = null;
		Writer xmlWriter = new StringWriter();
		QName qName = new QName("http://events.notifserv.railinc.com", "event");
		JAXBElement<Event> e = new JAXBElement<Event>(qName, Event.class, (Event) event);
		JaxbUtils.marshal(jaxbCtx, e, xmlWriter);
		xmlStr = xmlWriter.toString();
		return xmlStr;
	}
}
