package com.railinc.wembley.legacy.services.findusrail.model;

import java.io.Serializable;
import java.util.List;

public class FindUsRailContactResponseMsg implements FindUsRailContactResponse, Serializable {

	private static final long serialVersionUID = 2812893628164170685L;
	private FindUsRailContactResponseHeader responseHeader;
	private List<FindUsRailContact> contacts;

	public List<FindUsRailContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<FindUsRailContact> contacts) {
		this.contacts = contacts;
	}

	public FindUsRailContactResponseHeader getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(FindUsRailContactResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	@Override
	public String toString() {
		return String.format("FindUsRail Response %s, Contacts: %s", responseHeader, contacts);
	}
}
