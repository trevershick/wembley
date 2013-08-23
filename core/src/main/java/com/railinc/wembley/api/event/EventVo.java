package com.railinc.wembley.api.event;

import java.util.Date;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "event", namespace = "http://events.notifserv.railinc.com")
@XmlType(name = "EventType", namespace = "http://events.notifserv.railinc.com", propOrder = { "eventHeader", "body" })
public class EventVo implements Event {

	private static final Logger log = LoggerFactory.getLogger(EventVo.class);
	private static final long serialVersionUID = 4632552326371373702L;

	private String eventUid;
	private EventHeaderVo eventHeader;
	private JAXBElement<? extends EventBody> eventBody;
	private int retryCount;
	private String state;
	private Date stateTimestamp;

	public EventVo() {
		// Empty
	}

	public EventVo(String appId) {
		this(appId, null);
	}

	public EventVo(String appId, String correlationId) {
		this.eventHeader = new EventHeaderVo();
		this.eventHeader.setAppId(appId);
		this.eventHeader.setCorrelationId(correlationId);
	}

	public EventBody getEventBody() {
		if (log.isDebugEnabled()) {
			log.debug(String.format("Getting event body with type of %s", eventBody));
		}
		EventBody body = null;
		if (this.eventBody != null && this.eventBody.getValue() != null
				&& this.eventBody.getValue() instanceof EventBody) {
			body = this.eventBody.getValue();
		}
		return body;
	}

	@XmlElementRef(name = "body", type = JAXBElement.class, namespace = "http://events.notifserv.railinc.com")
	public JAXBElement<? extends EventBody> getBody() {
		return eventBody;
	}
	public void setBody(JAXBElement<? extends EventBody> eventBody) {
		this.eventBody = eventBody;
	}

	@XmlElement(name = "header", namespace = "http://events.notifserv.railinc.com")
	public EventHeaderVo getEventHeader() {
		return eventHeader;
	}
	public void setEventHeader(EventHeaderVo eventHeader) {
		this.eventHeader = eventHeader;
	}

	@XmlTransient
	public String getEventUid() {
		return eventUid;
	}
	public void setEventUid(String eventUid) {
		this.eventUid = eventUid;
	}
	@XmlTransient
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	@XmlTransient
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@XmlTransient
	public Date getStateTimestamp() {
		return stateTimestamp;
	}
	public void setStateTimestamp(Date stateTimestamp) {
		this.stateTimestamp = stateTimestamp;
	}

	public void addEventParam(String paramName, String paramValue) {
		getHeader().addEventParam(paramName, paramValue);
	}

	private EventHeaderVo getHeader() {
		if(this.eventHeader == null) {
			this.eventHeader = new EventHeaderVo();
		}
		return this.eventHeader;
	}

	public void setTextBody(String body) {
		TextEventBodyVo textBody = new TextEventBodyVo();
		textBody.setBodyRoot(body);
		QName qName = new QName("http://events.notifserv.railinc.com", "body");
		JAXBElement<TextEventBodyVo> e = new JAXBElement<TextEventBodyVo>(qName, TextEventBodyVo.class, textBody);
		this.eventBody = e;
	}

	public void setXmlBody(Element body) {
		XmlEventBodyVo xmlBody = new XmlEventBodyVo();
		xmlBody.setBodyRoot(body);
		QName qName = new QName("http://events.notifserv.railinc.com", "body");
		JAXBElement<XmlEventBodyVo> e = new JAXBElement<XmlEventBodyVo>(qName, XmlEventBodyVo.class, xmlBody);
		this.eventBody = e;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (obj == null || !(obj instanceof Event)) {
			return false;
		}

		Event event = (Event) obj;

		return this.eventHeader == null ? event.getEventHeader() == null : this.eventHeader.equals(event
				.getEventHeader());
	}

	@Override
	public int hashCode() {
		return this.eventHeader == null ? 0 : this.eventHeader.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s\n%s", this.eventHeader, this.eventBody);
	}

	/**
	 * Deep clone except for the delivery specs in the header
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		EventVo event = (EventVo)super.clone();
		event.setEventHeader(eventHeader == null ? null : (EventHeaderVo)eventHeader.clone());
		event.setBody(null);
		if(eventBody != null && eventBody.getValue() != null) {
			EventBody body = (EventBody)eventBody.getValue().clone();
			if(body instanceof XmlEventBodyVo) {
				event.setXmlBody(((XmlEventBodyVo)body).getBodyRoot());
			} else if(body instanceof TextEventBodyVo) {
				event.setTextBody(((TextEventBodyVo)body).getBodyRoot());
			}
		}
		return event;
	}
}
