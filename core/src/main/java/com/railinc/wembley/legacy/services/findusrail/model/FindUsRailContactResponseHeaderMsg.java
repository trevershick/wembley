package com.railinc.wembley.legacy.services.findusrail.model;

import java.io.Serializable;
import java.util.Date;

public class FindUsRailContactResponseHeaderMsg implements FindUsRailContactResponseHeader, Serializable {

	private static final long serialVersionUID = 8544007480035033934L;
	private Long messageId;
	private Date messageTimestamp;
	private String softwareComponentId;
	private String softwareVersion;
	private String resultCode;
	private String resultDescription;

	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public Date getMessageTimestamp() {
		return messageTimestamp;
	}
	public void setMessageTimestamp(Date messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	public String getSoftwareComponentId() {
		return softwareComponentId;
	}
	public void setSoftwareComponentId(String softwareComponentId) {
		this.softwareComponentId = softwareComponentId;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public boolean isSuccess() {
		return "S".equals(this.resultCode);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof FindUsRailContactResponseHeader)) {
			return false;
		}

		FindUsRailContactResponseHeader header = (FindUsRailContactResponseHeader)obj;

		return (this.messageId == null ? header.getMessageId() == null : this.messageId.equals(header.getMessageId()));
	}
	@Override
	public int hashCode() {
		return (this.messageId == null ? 0 : (int)(this.messageId / 10000));
	}
	@Override
	public String toString() {
		return String.format("[Message ID: %s, Result: %s, Desc: %s]", this.messageId, this.resultCode, this.resultDescription);
	}
}
