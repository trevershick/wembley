package com.railinc.wembley.api.core;

public class NotificationServiceInvalidEventException extends NotificationServiceException {

	private static final long serialVersionUID = -993421155407761877L;

	public NotificationServiceInvalidEventException() {
		super();
	}

	public NotificationServiceInvalidEventException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotificationServiceInvalidEventException(String message) {
		super(message);
	}

	public NotificationServiceInvalidEventException(Throwable cause) {
		super(cause);
	}
}
