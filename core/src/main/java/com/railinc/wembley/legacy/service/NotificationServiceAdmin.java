package com.railinc.wembley.legacy.service;

import java.util.List;
import java.util.Map;

import com.railinc.wembley.legacysvc.domain.ApplicationVo;
import com.railinc.wembley.legacysvc.domain.EventVo;

public interface NotificationServiceAdmin {
	
	
	EventVo getEventByCorrelationId(String appId, String correlationId);
	EventVo getEvent(String uid);

	
	
	/**
	 * @param applicationId
	 * @return null if not found, the app otherwise
	 */
	ApplicationVo getApplication(String applicationId);
	ApplicationVo createApplication(ApplicationVo application);
	ApplicationVo updateApplication(String id, ApplicationVo vo);
	void deleteApplication(String id);
	List<ApplicationVo> getAllApplications();
	
	
	Map<String, Integer> getStatusCountsByEventId(String eventUid);
	NotificationSearchResults findNotificationsByDeliverySpec(
			String eventUid, 
			String deliverySpecPartial, 
			String token,
			int max);
	/**
	 * 
	 * @param eventUid
	 * @return the number of cancelled notifications. this method is really best effort.
	 * it can be called and the records can be updated but they may already be in memory
	 * and processing
	 */
	int cancelNotifications(String eventUid);
}
