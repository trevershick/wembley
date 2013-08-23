package com.railinc.wembley.api.notifications.model;

import com.railinc.wembley.api.event.FailedEventReason;

public class AdminFailedEventNotificationVo extends NotificationVo {

	private static final long serialVersionUID = 5597386293102242603L;

	private FailedEventReason failedEventReason;
	private Throwable exception;

	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	public FailedEventReason getFailedEventReason() {
		return failedEventReason;
	}
	public void setFailedEventReason(FailedEventReason failedEventReason) {
		this.failedEventReason = failedEventReason;
	}
}
