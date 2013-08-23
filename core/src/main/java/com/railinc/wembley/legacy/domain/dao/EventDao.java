package com.railinc.wembley.legacy.domain.dao;

import com.railinc.wembley.legacysvc.domain.EventVo;

public interface EventDao {

	EventVo getEvent(String uid);

	EventVo getEventByCorrelationId(String appId, String correlationId);
	
}
