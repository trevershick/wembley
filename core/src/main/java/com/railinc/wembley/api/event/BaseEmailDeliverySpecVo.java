package com.railinc.wembley.api.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace="http://events.notifserv.railinc.com", propOrder={"from", "subject", "contentType", "attachments"})
public abstract class BaseEmailDeliverySpecVo extends AbstractDeliverySpecVo {

	private static final long serialVersionUID = 4668871891614210722L;

	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String from;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String subject;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String contentType;
	@XmlElementWrapper(name="attachments", namespace="http://events.notifserv.railinc.com")
	@XmlElement(name="attachment", namespace="http://events.notifserv.railinc.com")
	private List<EmailAttachmentVo> attachments;

	public List<EmailAttachmentVo> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<EmailAttachmentVo> attachments) {
		this.attachments = attachments;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public List<String> validate() {
		List<String> msgs = new ArrayList<String>();
		if(StringUtils.isEmpty(from)) {
			msgs.add("Missing From Address");
		}
		return msgs;
	}
}
