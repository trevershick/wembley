package com.railinc.wembley.api.event;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.util.JaxbUtils;

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
		JAXBElement<EventVo> e = new JAXBElement<EventVo>(qName, EventVo.class, (EventVo) event);
		JaxbUtils.marshal(jaxbCtx, e, xmlWriter);
		xmlStr = xmlWriter.toString();
		return xmlStr;
	}
}
