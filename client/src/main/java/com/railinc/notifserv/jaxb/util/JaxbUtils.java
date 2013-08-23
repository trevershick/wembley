package com.railinc.notifserv.jaxb.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.notifserv.client.legacy.NotificationServiceException;
import com.railinc.notifserv.inbound.legacy.EmailDeliverySpec;
import com.railinc.notifserv.inbound.legacy.Event;
import com.railinc.notifserv.inbound.legacy.EventHeader;
import com.railinc.notifserv.inbound.legacy.EventParameter;
import com.railinc.notifserv.inbound.legacy.FindUsRailDeliverySpec;
import com.railinc.notifserv.inbound.legacy.FtpViaMqDeliverySpec;
import com.railinc.notifserv.inbound.legacy.MqDeliverySpec;
import com.railinc.notifserv.inbound.legacy.SSORoleDeliverySpec;
import com.railinc.notifserv.inbound.legacy.SSOUserDeliverySpec;

public class JaxbUtils {

	private static final Logger log = LoggerFactory.getLogger(JaxbUtils.class);
	private static JAXBContext eventJaxbCtx;

	public synchronized static final JAXBContext getEventJaxbContext() throws NotificationServiceException {
		if (eventJaxbCtx == null) {
			log.info("Creating the Event JAXB Context");
			eventJaxbCtx = getJaxbContext(Event.class, EventHeader.class, EventParameter.class,
					EmailDeliverySpec.class, MqDeliverySpec.class, FtpViaMqDeliverySpec.class,
					SSORoleDeliverySpec.class, SSOUserDeliverySpec.class, FindUsRailDeliverySpec.class);
		}

		return eventJaxbCtx;
	}

	public synchronized static final JAXBContext getJaxbContext(Class<?> ... classes) throws NotificationServiceException {
		try {
			JAXBContext ctx = JAXBContext.newInstance(classes);
			return ctx;
		} catch (JAXBException e) {
			log.error("JAXBException trying to create JAXBContext!", e);
			throw new NotificationServiceException("Error creating the JAXB Context", e);
		}
	}

	public static void marshal(JAXBContext jaxbCtx, JAXBElement<?> element, Writer output) {
		try {
			Marshaller marshaller = jaxbCtx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
			marshaller.marshal(element, output);
		} catch (JAXBException e) {
			log.error("JAXBexception trying to marshal the element " + element, e);
			throw new NotificationServiceException("Error using the JAXB Marshaller", e);
		}
	}

	public static void marshal(JAXBContext jaxbCtx, Object object, Writer output) {
		try {
			Marshaller marshaller = jaxbCtx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
			marshaller.marshal(object, output);
		} catch (JAXBException e) {
			log.error("JAXBexception trying to marshal the object " + object, e);
			throw new NotificationServiceException("Error using the JAXB Marshaller", e);
		}
	}

	public static Object unmarshal(JAXBContext jaxbCtx, String xml) throws NotificationServiceException {
		return unmarshal(jaxbCtx, new BufferedInputStream(new ByteArrayInputStream(xml.getBytes())));
	}

	public static Object unmarshal(JAXBContext jaxbCtx, InputStream xml) throws NotificationServiceException {
		try {
			Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
			Object object = unmarshaller.unmarshal(xml);
			return object;
		} catch (JAXBException e) {
			log.error("JAXBexception trying to unmarshal the xml " + xml, e);
			throw new NotificationServiceException("Error using the JAXB Unmarshaller", e);
		}
	}
}
