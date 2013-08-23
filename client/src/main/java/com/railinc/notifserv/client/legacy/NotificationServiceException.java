package com.railinc.notifserv.client.legacy;

public class NotificationServiceException extends RuntimeException {

	private static final long serialVersionUID = -4378520015796302630L;

	public NotificationServiceException(String message) {
		super(message);
	}

	public NotificationServiceException() {
		super();
	}

	public NotificationServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotificationServiceException(Throwable cause) {
		super(cause);
	}
}
