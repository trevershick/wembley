package com.railinc.wembley.api.util;

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

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.ApplicationMailinglistVo;
import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.api.event.EventHeaderVo;
import com.railinc.wembley.api.event.EventParameterVo;
import com.railinc.wembley.api.event.EventVo;
import com.railinc.wembley.api.event.FindUsRailDeliverySpecVo;
import com.railinc.wembley.api.event.FtpViaMqDeliverySpecVo;
import com.railinc.wembley.api.event.MailinglistDeliverySpecVo;
import com.railinc.wembley.api.event.MqDeliverySpecVo;
import com.railinc.wembley.api.event.NamedMailinglistVo;
import com.railinc.wembley.api.event.SSORoleDeliverySpecVo;
import com.railinc.wembley.api.event.SSOUserDeliverySpecVo;

public class JaxbUtils {

	private static final Logger log = LoggerFactory.getLogger(JaxbUtils.class);
	private static JAXBContext eventJaxbCtx;

	public synchronized static final JAXBContext getEventJaxbContext() throws NotificationServiceException {
		if (eventJaxbCtx == null) {
			log.info("Creating the Event JAXB Context");
			eventJaxbCtx = getJaxbContext(EventVo.class, EventHeaderVo.class, EventParameterVo.class,
					EmailDeliverySpecVo.class, MqDeliverySpecVo.class, FtpViaMqDeliverySpecVo.class,
					SSORoleDeliverySpecVo.class, SSOUserDeliverySpecVo.class, FindUsRailDeliverySpecVo.class,
					MailinglistDeliverySpecVo.class, ApplicationMailinglistVo.class, NamedMailinglistVo.class);
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
			marshaller.marshal(element, output);
		} catch (JAXBException e) {
			log.error("JAXBexception trying to marshal the element " + element, e);
			throw new NotificationServiceException("Error using the JAXB Marshaller", e);
		}
	}

	public static void marshal(JAXBContext jaxbCtx, Object object, Writer output) {
		try {
			Marshaller marshaller = jaxbCtx.createMarshaller();
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
