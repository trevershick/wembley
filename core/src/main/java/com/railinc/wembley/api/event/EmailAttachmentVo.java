package com.railinc.wembley.api.event;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;

@XmlAccessorType(XmlAccessType.FIELD)
public class EmailAttachmentVo {

	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String attachmentName;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String contentType;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	@XmlMimeType("application/octet-stream")
	private DataHandler attachment;

	public DataHandler getAttachment() {
		return attachment;
	}
	public void setAttachment(DataHandler attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
