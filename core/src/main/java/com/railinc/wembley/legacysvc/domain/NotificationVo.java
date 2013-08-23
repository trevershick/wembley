package com.railinc.wembley.legacysvc.domain;

import java.util.Date;

public class NotificationVo {

	private static final long serialVersionUID = 4632552326371373702L;

	private String notificationUid;
	private String eventUid;
	private String appId;
	private String state;

	private int retryCount;
	private int deliveryTiming;
	private Date stateTimestamp;
	private Date createdTimestamp;
	private byte[] deliverySpecification;
	
	public byte[] getDeliverySpecification() {
		return deliverySpecification;
	}
	public void setDeliverySpecification(byte[] deliverySpecification) {
		this.deliverySpecification = deliverySpecification;
	}
	public String getNotificationUid() {
		return notificationUid;
	}
	public void setNotificationUid(String notificationUid) {
		this.notificationUid = notificationUid;
	}
	public String getEventUid() {
		return eventUid;
	}
	public void setEventUid(String eventUid) {
		this.eventUid = eventUid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	public int getDeliveryTiming() {
		return deliveryTiming;
	}
	public void setDeliveryTiming(int deliveryTiming) {
		this.deliveryTiming = deliveryTiming;
	}
	public Date getStateTimestamp() {
		return stateTimestamp;
	}
	public void setStateTimestamp(Date stateTimestamp) {
		this.stateTimestamp = stateTimestamp;
	}
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	
}
