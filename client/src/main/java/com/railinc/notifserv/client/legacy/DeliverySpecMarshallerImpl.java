package com.railinc.notifserv.client.legacy;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.railinc.notifserv.inbound.legacy.DeliverySpec;
import com.railinc.notifserv.jaxb.util.JaxbUtils;

public class DeliverySpecMarshallerImpl implements DeliverySpecMarshaller {

	private JAXBContext jaxbCtx;

	public DeliverySpecMarshallerImpl() {
		this.jaxbCtx = JaxbUtils.getEventJaxbContext();
	}

	public String marshalDeliverySpec(DeliverySpec spec)
	{
		String xmlStr = null;
		Writer xmlWriter = new StringWriter();
		QName qName = new QName("http://events.notifserv.railinc.com", "deliverySpec");
		JAXBElement<DeliverySpec> e = new JAXBElement<DeliverySpec>(qName, DeliverySpec.class, (DeliverySpec)spec);
		JaxbUtils.marshal(this.jaxbCtx, e, xmlWriter);

		xmlStr = xmlWriter.toString();

		return xmlStr;
	}

}

