package com.railinc.wembley.api.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name="TextBodyType", namespace="http://events.notifserv.railinc.com")
@XmlAccessorType(XmlAccessType.NONE)
public class TextEventBodyVo implements EventBody {

	private static final long serialVersionUID = -6585420226264764753L;
	private String bodyRoot;

	@XmlValue
	public String getBodyRoot() {
		return bodyRoot;
	}

	public void setBodyRoot(String bodyRoot) {
		this.bodyRoot = bodyRoot;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
