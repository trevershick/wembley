package com.railinc.r2dq.configuration;

import com.railinc.common.configuration.ConfigurationService;
import com.railinc.r2dq.correspondence.Contact;


public interface R2DQConfigurationService extends ConfigurationService {
	Contact getDefaultReplyTo();
	Contact getDefaultFrom();
	String getNotificationServiceAppId();
	String getApplicationUrl();
	String getRailincUrl();
	String getApplicationName();
	/**
	 * When sending task notification email, what is the maximum number of tasks that we can represent in an email.
	 * 
	 * @return
	 */
	int getMaximumTasksPerEmail();
}
