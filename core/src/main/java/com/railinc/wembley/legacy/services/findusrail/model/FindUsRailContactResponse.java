package com.railinc.wembley.legacy.services.findusrail.model;

import java.util.List;

public interface FindUsRailContactResponse {

	FindUsRailContactResponseHeader getResponseHeader();
	List<FindUsRailContact> getContacts();
}
