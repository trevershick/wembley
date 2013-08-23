package com.railinc.wembley.api.core;


public class NotificationServiceRemoteException extends NotificationServiceException {

	private static final long serialVersionUID = 7039363304720237921L;

	public NotificationServiceRemoteException() {
		super();
	}

	public NotificationServiceRemoteException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotificationServiceRemoteException(String message) {
		super(message);
	}

	public NotificationServiceRemoteException(Throwable cause) {
		super(cause);
	}
}
