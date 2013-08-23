package com.railinc.wembley.legacy.domain.dao;

import java.util.List;

import com.railinc.wembley.legacysvc.domain.SystemConfig;

public interface SystemConfigDao {

	SystemConfig getSystemConfig(String key);
	List<SystemConfig> getAllSystemConfig();
}
