package com.railinc.r2dq.configuration;

import com.railinc.common.configuration.ConfigurationServiceImpl;
import com.railinc.r2dq.correspondence.Contact;
import com.railinc.r2dq.correspondence.SimpleContact;

public class R2DQConfigurationServiceImpl extends ConfigurationServiceImpl implements R2DQConfigurationService {
	public static final String APP_NAME = "app.name";
	public static final String APP_NAME_DEFAULT = "R2DQ";

	public static final String RAILINC_URL = "railinc.url";
	public static final String RAILINC_URL_DEFAULT = "http://www.railinc.com";

	public static final String APP_URL = "app.url";
	public static final String APP_URL_DEFAULT = "https://www.railinc.com/r2dq";

	public static final String DEFAULT_EMAIL_REPLYTO_NAME = "default.email.replyto.name";
	public static final String DEFAULT_EMAIL_REPLYTO_NAME_DEFAULT = null;
	
	public static final String DEFAULT_EMAIL_REPLYTO_ADDRESS = "default.email.replyto.address";
	public static final String DEFAULT_EMAIL_REPLYTO_ADDRESS_DEFAULT = "noreply@railinc.com";
	
	public static final String DEFAULT_EMAIL_FROM_NAME = "default.email.from.name";
	public static final String DEFAULT_EMAIL_FROM_NAME_DEFAULT = null;
	
	public static final String DEFAULT_EMAIL_FROM_ADDRESS = "default.email.from.address";
	public static final String DEFAULT_EMAIL_FROM_ADDRESS_DEFAULT = "noreply@railinc.com";
	public static final String NOTIFICATION_SERVICE_APP_ID = "notification.service.appid";
	public static final String DEFAULT_NOTIFICATION_SERVICE_APP_ID = "R2DQ";
	
	public static final int MAXIMUM_TASKS_PER_EMAIL_DEFAULT = 100;
	public static final String MAXIMUM_TASKS_PER_EMAIL = "maximum.tasks.per.email";
	
	
	
	@Override
	public Contact getDefaultReplyTo() {
		String nm = overridable(DEFAULT_EMAIL_REPLYTO_NAME, DEFAULT_EMAIL_REPLYTO_NAME_DEFAULT);
		String em = overridable(DEFAULT_EMAIL_REPLYTO_ADDRESS, DEFAULT_EMAIL_REPLYTO_ADDRESS_DEFAULT);
		return new SimpleContact(nm, em);
	}

	@Override
	public Contact getDefaultFrom() {
		String nm = overridable(DEFAULT_EMAIL_FROM_NAME, DEFAULT_EMAIL_FROM_NAME_DEFAULT);
		String em = overridable(DEFAULT_EMAIL_FROM_ADDRESS, DEFAULT_EMAIL_FROM_ADDRESS_DEFAULT);
		return new SimpleContact(nm, em);
	}

	@Override
	public String getNotificationServiceAppId() {
		return overridable(NOTIFICATION_SERVICE_APP_ID, DEFAULT_NOTIFICATION_SERVICE_APP_ID);
	}

	@Override
	public String getApplicationUrl() {
		return overridable(APP_URL, APP_URL_DEFAULT);
	}

	@Override
	public String getRailincUrl() {
		return overridable(RAILINC_URL, RAILINC_URL_DEFAULT);
	}

	@Override
	public String getApplicationName() {
		return overridable(APP_NAME, APP_NAME_DEFAULT);	
	}

	@Override
	public int getMaximumTasksPerEmail() {
		return overridable(MAXIMUM_TASKS_PER_EMAIL, MAXIMUM_TASKS_PER_EMAIL_DEFAULT);	
	}

	
}
