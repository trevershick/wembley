package com.railinc.wembley.legacy.services.sso;

import java.io.Serializable;
import java.util.List;

public class SsoContactResponseMsg implements SsoContactResponse, Serializable {

	private static final long serialVersionUID = 1L;
	
	List<SsoContact> contacts;

	public List<SsoContact> getContacts() {
		return contacts;
	}
	
	public void setContacts( List<SsoContact> contacts ) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return String.format( "SSO Contacts: %s", contacts );
	}
}
