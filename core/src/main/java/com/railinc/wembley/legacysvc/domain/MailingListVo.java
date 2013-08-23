package com.railinc.wembley.legacysvc.domain;

public class MailingListVo {

	private static final String DEFAULT_DESCRIPTION = "No Description";
	public static final String DEFAULT_FROM_ADDRESS = "no-reply@railinc.com";
	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private String key;
	private String type;
	private String application;
	private String shortName;
	private String fromAddress = DEFAULT_FROM_ADDRESS;
	private String title;
	private String description = DEFAULT_DESCRIPTION;
	private boolean active = true;
}
