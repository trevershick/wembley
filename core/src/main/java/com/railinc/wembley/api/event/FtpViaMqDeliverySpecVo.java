package com.railinc.wembley.api.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Because Railinc has no 'good' way of doing FTP, this class will be used to 
 * hold destination and message type information for delivery FTP notifications using MQ.
 * The bulk of the functionality is in the parent class.
 * 
 * An Example
 *	<ns:deliverySpec xsi:type="ns:FtpViaMqDeliverySpecType">
 *		<ns:messageType>COT5</ns:messageType>
 *		<ns:destination>BNSF</ns:destination>
 *	</ns:deliverySpec>
 *
 * @author SDWXD09
 *
 */

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType(name = "FtpViaMqDeliverySpecType", namespace="http://events.notifserv.railinc.com" )
public class FtpViaMqDeliverySpecVo extends MqDeliverySpecVo {

	private static final long serialVersionUID = -6367926951138570025L;
	
	public String toString() {
		return String.format( "FTP VIA MQ Destination: %s, Message Type: %s", getDestination(), getMessageType() );
	}
}
