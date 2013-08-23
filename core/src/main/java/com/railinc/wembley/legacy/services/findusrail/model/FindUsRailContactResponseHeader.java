package com.railinc.wembley.legacy.services.findusrail.model;

import java.util.Date;

public interface FindUsRailContactResponseHeader {

	Long getMessageId();
	Date getMessageTimestamp();
	String getSoftwareComponentId();
	String getSoftwareVersion();
	String getResultCode();
	boolean isSuccess();
	String getResultDescription();
}
