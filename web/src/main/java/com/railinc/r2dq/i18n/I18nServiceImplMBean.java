package com.railinc.r2dq.i18n;

import java.util.Date;

public interface I18nServiceImplMBean {
	void reload();
	void start();
	void stop();
	boolean isRunning();
	Date getLastReloaded();
}
