package com.railinc.wembley.api.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="deliverySpec", namespace="http://events.notifserv.railinc.com")
@XmlType(name="DeliverySpecType", namespace="http://events.notifserv.railinc.com")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractDeliverySpecVo implements DeliverySpec
{

}
