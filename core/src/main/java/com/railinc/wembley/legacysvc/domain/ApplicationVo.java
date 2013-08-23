package com.railinc.wembley.legacysvc.domain;

import java.util.Date;
import java.util.Map;

public class ApplicationVo  {
	private String appId;
	private Map<String, String> supportedNamespaces;
	private Date createdTimestamp;
	private String defaultDeliveryTiming = "0";
	private String adminEmail;

	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public Map<String, String> getSupportedNamespaces() {
		return supportedNamespaces;
	}
	public void setSupportedNamespaces(Map<String, String> supportedNamespaces) {
		this.supportedNamespaces = supportedNamespaces;
	}
	public String getDefaultDeliveryTiming() {
		return defaultDeliveryTiming;
	}
	public void setDefaultDeliveryTiming(String defaultDeliveryTiming) {
		this.defaultDeliveryTiming = defaultDeliveryTiming;
	}
	public String getAdminEmail() {
		return adminEmail;
	}
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

}
