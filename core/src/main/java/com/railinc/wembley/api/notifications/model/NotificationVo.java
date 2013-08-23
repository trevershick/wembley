package com.railinc.wembley.api.notifications.model;

import java.util.Date;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.Event;

public class NotificationVo implements Notification {

	private static final long serialVersionUID = 3459135769246777225L;
	private String notificationUid;
	private String appId;
	private String state;
	private String deliveryTiming;
	private String eventUid;
	private Event event;
	private String eventXml;
	private String deliverySpecString;
	private DeliverySpec deliverySpec;
	private Date createdTimestamp;
	private Date stateTimestamp;
	private int retryCount;
	
	public NotificationVo() {
		super();
	}
	
	public int getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
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
	public DeliverySpec getDeliverySpec() {
		return deliverySpec;
	}
	public void setDeliverySpec(DeliverySpec deliverySpec) {
		this.deliverySpec = deliverySpec;
	}
	public String getDeliveryTiming() {
		return deliveryTiming;
	}
	public void setDeliveryTiming(String deliveryTiming) {
		this.deliveryTiming = deliveryTiming;
	}
	public String getEventXml() {
		return eventXml;
	}
	public void setEventXml(String eventXml) {
		this.eventXml = eventXml;
	}
	public String getEventUid() {
		return eventUid;
	}
	public void setEventUid(String eventUid) {
		this.eventUid = eventUid;
	}
	public String getNotificationUid() {
		return notificationUid;
	}
	public void setNotificationUid(String notificationUid) {
		this.notificationUid = notificationUid;
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
	public String getDeliverySpecString() {
		return deliverySpecString;
	}
	public void setDeliverySpecString(String deliverySpecString) {
		this.deliverySpecString = deliverySpecString;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof NotificationVo)) {
			return false;
		}

		NotificationVo not = (NotificationVo)obj;
		return this.notificationUid == null ? not.getNotificationUid() == null :
					this.notificationUid.equals(not.getNotificationUid());
	}

	@Override
	public int hashCode() {
		return this.notificationUid == null ? 0 : this.notificationUid.hashCode();
	}

	@Override
	public String toString() {
		return String.format("Notification [%s]: Event Uid: %s, Timing: %s, State: %s (%d), Not. Uid: %s", this.appId,
				getEventUid(), this.deliveryTiming, this.state, this.retryCount, this.notificationUid);
	}

	/**
	 * Deep clone except for the delivery spec and any delivery specs on the event's header
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		NotificationVo not = (NotificationVo)super.clone();
		not.setEvent(event == null ? null : (Event)event.clone());
		return not;
	}
}
