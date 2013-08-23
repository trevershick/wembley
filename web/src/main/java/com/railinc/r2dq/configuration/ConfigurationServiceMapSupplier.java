package com.railinc.r2dq.configuration;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Supplier;

public class ConfigurationServiceMapSupplier implements Supplier<Map<String, Object>> {

	R2DQConfigurationService service;
	
	public ConfigurationServiceMapSupplier() {}
	
	public ConfigurationServiceMapSupplier(R2DQConfigurationService s) {
		this.service = s;
	}

	@Override
	public Map<String, Object> get() {
		Map<String,Object> p = newHashMap();
		p.put("defaultFrom", service.getDefaultFrom());
		p.put("defaultReplyTo", service.getDefaultReplyTo());
		p.put("notificationServiceAppId", service.getNotificationServiceAppId());
		p.put("applicationUrl", service.getApplicationUrl());
		p.put("railincUrl", service.getRailincUrl());
		return p;
	}

	public R2DQConfigurationService getService() {
		return service;
	}
	
	@Required
	public void setService(R2DQConfigurationService service) {
		this.service = service;
	}

}
