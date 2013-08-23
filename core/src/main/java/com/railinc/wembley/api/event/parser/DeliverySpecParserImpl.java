package com.railinc.wembley.api.event.parser;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.util.JaxbUtils;

public class DeliverySpecParserImpl implements DeliverySpecParser {

	private static final Logger log = LoggerFactory.getLogger(DeliverySpecParserImpl.class);
	private JAXBContext jaxbCtx;

	public DeliverySpecParserImpl() {
		log.info("Instantiating the DeliverySpecParserImpl");
		this.jaxbCtx = JaxbUtils.getEventJaxbContext();
	}

	public DeliverySpec parseDeliverySpec(String xml) {

		if(xml == null) {
			log.error("DeliverySpec.parseDeliverySpec(String) called with a null String");
			throw new NotificationServiceException("Delivery Spec Parsing Exception - Delivery Spec XML cannot be null");
		}

		return parseDeliverySpec(new BufferedInputStream(new ByteArrayInputStream(xml.getBytes())));
	}

	public DeliverySpec parseDeliverySpec(InputStream xml) {
		if(xml == null) {
			log.error("DeliverySpec.parseDeliverySpec(InputStream) called with a null InputStream");
			throw new NotificationServiceException("Delivery Spec Parsing Exception - Delivery Spec XML cannot be null");
		}

		DeliverySpec deliverySpec = (DeliverySpec)JaxbUtils.unmarshal(this.jaxbCtx, xml);
		return deliverySpec;
	}
}
