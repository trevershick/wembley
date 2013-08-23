package com.railinc.wembley.legacy.services.findusrail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ContactRequestDetail {

	@XmlElement(name="ContactQuery", namespace=FindUsRailContactServiceImpl.REQUEST_NS)
	private ContactQuery contactQuery;

	public ContactQuery getContactQuery() {
		return contactQuery;
	}

	public void setContactQuery(ContactQuery contactQuery) {
		this.contactQuery = contactQuery;
	}
}
