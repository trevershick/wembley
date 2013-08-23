package com.railinc.wembley.api.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

/**
 * This class will be used to delivery notifications via MQ. JAXB is used (see annotations)
 * to marshal/unmarshal the XML representation.
 *
 *  An example
 *
 *	<ns:deliverySpec xsi:type="ns:MqDeliverySpecType">
 *		<ns:messageType>COT5</ns:messageType>
 *		<ns:destination>BNSF</ns:destination>
 *	</ns:deliverySpec>
 *
 * @author SDWXD09
 *
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType(name = "MqDeliverySpecType", namespace="http://events.notifserv.railinc.com", propOrder={ "messageType", "destination" })
public class MqDeliverySpecVo extends AbstractDeliverySpecVo {

	private static final long serialVersionUID = -4071668794556099214L;

	@XmlElement( namespace="http://events.notifserv.railinc.com" )
	private String messageType;

	@XmlElement( namespace="http://events.notifserv.railinc.com" )
	private String destination;

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String toString() {
		return String.format( "MQ Destination: %s, Message Type: %s", destination, messageType );
	}

	public List<String> validate() {
		List<String> msgs = new ArrayList<String>();
		if(StringUtils.isEmpty(messageType)) {
			msgs.add("Missing Message Type");
		}
		if(StringUtils.isEmpty(destination)) {
			msgs.add("Missing Destination");
		}
		return msgs;
	}
}
