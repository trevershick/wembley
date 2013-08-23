package com.railinc.wembley.legacy.services.findusrail;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ControlInformation {

	@XmlElement(name="MessageID", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private Long messageId;
	@XmlElement(name="MessageCreationTimestamp", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private Date messageTimestamp;

	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public Date getMessageTimestamp() {
		return messageTimestamp;
	}
	public void setMessageTimestamp(Date messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}
}
