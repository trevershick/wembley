package com.railinc.wembley.api.event;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.railinc.wembley.api.util.JaxbUtils;

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
		JAXBElement<AbstractDeliverySpecVo> e = new JAXBElement<AbstractDeliverySpecVo>(qName, AbstractDeliverySpecVo.class, (AbstractDeliverySpecVo)spec);
		JaxbUtils.marshal(this.jaxbCtx, e, xmlWriter);

		xmlStr = xmlWriter.toString();

		return xmlStr;
	}

}

