package com.railinc.wembley.legacysvc.domain;

import java.util.Date;

public class EventVo {

	private static final long serialVersionUID = 4632552326371373702L;

	private String eventUid;

	private int retryCount;
	private String state;
	private Date stateTimestamp;
	private String correlationId;
	private String appId;
	private byte[] contents;
	private Date sendAfter;

	
	public Date getSendAfter() {
		return sendAfter;
	}
	public void setSendAfter(Date sendAfter) {
		this.sendAfter = sendAfter;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	


	public byte[] getContents() {
		return contents;
	}
	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public String getEventUid() {
		return eventUid;
	}
	public void setEventUid(String eventUid) {
		this.eventUid = eventUid;
	}

	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public Date getStateTimestamp() {
		return stateTimestamp;
	}
	public void setStateTimestamp(Date stateTimestamp) {
		this.stateTimestamp = stateTimestamp;
	}

}
