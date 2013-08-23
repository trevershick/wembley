package com.railinc.wembley.api.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "EventParameterType",
		namespace="http://events.notifserv.railinc.com", propOrder={"paramName", "paramValue"})
public class EventParameterVo implements EventParameter {

	private static final long serialVersionUID = -6295850037725225345L;
	private String paramName;
	private String paramValue;

	@XmlElement(name="paramName", namespace="http://events.notifserv.railinc.com")
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	@XmlElement(name="paramValue", namespace="http://events.notifserv.railinc.com")
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof EventParameter)) {
			return false;
		}

		EventParameter param = (EventParameter)obj;

		return this.paramName == null ? param.getParamName() == null :
				this.paramName.equals(param.getParamName());
	}

	@Override
	public int hashCode() {
		return this.paramName == null ? 0 : this.paramName.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s=%s", this.paramName, this.paramValue);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
