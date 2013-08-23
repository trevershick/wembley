package com.railinc.wembley.api.core;

/**
 * The primary interface for key Notification Service classes. The APP ID that is return
 * drives much of the functionality in the Notification Service. For instance, what
 * subscription loader to use. 
 *
 */
public interface NotificationService {

	public static final String DEFAULT_APP_ID = "DEFAULT";

	String getAppId();
}
