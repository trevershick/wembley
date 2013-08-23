package com.railinc.wembley.api.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "EventHeaderType",
		namespace="http://events.notifserv.railinc.com",
		propOrder={"appId", "correlationId", "eventParams", "deliverySpecs"})
public class EventHeaderVo implements EventHeader {

	private static final long serialVersionUID = -1581915236493180957L;
	private String appId;
	private String correlationId;
	private List<EventParameterVo> eventParams;
	private DeliverySpecs deliverySpecs;

	@XmlElement(name="appId", namespace="http://events.notifserv.railinc.com")
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	@XmlElement(name="correlationId", namespace="http://events.notifserv.railinc.com")
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	@XmlElementWrapper(name="eventParams", namespace="http://events.notifserv.railinc.com")
	@XmlElement(name="param", namespace="http://events.notifserv.railinc.com")
	public List<EventParameterVo> getEventParams() {
		return eventParams;
	}
	public void setEventParams(List<EventParameterVo> eventParams) {
		this.eventParams = eventParams;
	}
	public void addEventParam(String paramName, String paramValue) {
		if(this.eventParams == null) {
			this.eventParams = new ArrayList<EventParameterVo>();
		}
		EventParameterVo param = new EventParameterVo();
		param.setParamName(paramName);
		param.setParamValue(paramValue);
		this.eventParams.add(param);
	}

	@XmlElement(name="deliverySpecs", namespace="http://events.notifserv.railinc.com")
	public DeliverySpecs getDeliverySpecs() {
		return this.deliverySpecs;
	}
	public void setDeliverySpecs(DeliverySpecs deliverySpecs) {
		this.deliverySpecs = deliverySpecs;
	}
	public void addDeliverySpec(AbstractDeliverySpecVo deliverySpec) {
		if(this.deliverySpecs == null) {
			this.deliverySpecs = new DeliverySpecsVo();
		}
		this.deliverySpecs.addDeliverySpec(deliverySpec);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof EventHeader)) {
			return false;
		}

		EventHeader header = (EventHeader)obj;

		return (this.appId == null ? header.getAppId() == null :
				this.appId.equals(header.getAppId())) &&
				(this.correlationId == null ? header.getCorrelationId() == null :
				this.correlationId.equals(header.getCorrelationId()));
	}

	@Override
	public int hashCode() {
		return (this.appId == null ? 0 : this.appId.hashCode()) +
				(29 * (this.correlationId == null ? 0 : this.correlationId.hashCode()));
	}

	@Override
	public String toString() {
		return String.format("%s=%s: %s", this.appId, this.correlationId, this.eventParams);
	}
	
	/**
	 * Does not do a deep copy of delivery specs 
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		EventHeaderVo header = (EventHeaderVo)super.clone();
		header.setEventParams(null);
		if(eventParams != null) {
			for(EventParameterVo param : this.eventParams) {
				if(param != null) {
					header.addEventParam(param.getParamName(), param.getParamValue());
				}
			}
		}
		return header;
	}
}
