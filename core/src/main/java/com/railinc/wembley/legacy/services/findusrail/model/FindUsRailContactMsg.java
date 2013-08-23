package com.railinc.wembley.legacy.services.findusrail.model;

import java.io.Serializable;

public class FindUsRailContactMsg implements FindUsRailContact, Serializable {

	private static final long serialVersionUID = -5787237840626002827L;
	private String contactId;
	private String emailAddress;

	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	@Override
	public String toString() {
		return String.format("Contact [ID=%s], Email: %s", this.contactId, this.emailAddress);
	}
}
