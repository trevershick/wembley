package com.railinc.wembley.api.event;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmailDeliverySpecType", namespace="http://events.notifserv.railinc.com", propOrder={"to", "cc", "bcc"})
public class EmailDeliverySpecVo extends BaseEmailDeliverySpecVo {

	private static final long serialVersionUID = 5849708897189106203L;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String to;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String cc;
	@XmlElement(namespace="http://events.notifserv.railinc.com")
	private String bcc;

	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public List<String> validate() {
		List<String> msgs = super.validate();
		if(StringUtils.isEmpty(to)) {
			msgs.add("Missing To Address");
		}
		return msgs;
	}

	public String toString() {
		return String.format("Email: From=%s, To=%s, cc=%s, bcc=%s, subject=%s, contentType=%s", getFrom(), to, cc, bcc, getSubject(), getContentType());
	}
}
