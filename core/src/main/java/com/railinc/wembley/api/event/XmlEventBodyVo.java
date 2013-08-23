package com.railinc.wembley.api.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="XmlBodyType", namespace="http://events.notifserv.railinc.com")
public class XmlEventBodyVo implements EventBody {

	private static final long serialVersionUID = -4563870081871710131L;
	private Element bodyRoot;

	@XmlAnyElement
	public Element getBodyRoot() {
		return bodyRoot;
	}

	public void setBodyRoot(Element bodyRoot) {
		this.bodyRoot = bodyRoot;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		XmlEventBodyVo body = (XmlEventBodyVo)super.clone();
		if(bodyRoot != null) {
			Element newBody = (Element)bodyRoot.cloneNode(true);
			body.setBodyRoot(newBody);
		}
		return body;
	}
}
