package com.railinc.wembley.legacy.domain.dao;

import java.util.List;
import java.util.Map;

import com.railinc.wembley.legacy.service.NotificationSearchResults;
import com.railinc.wembley.legacysvc.domain.NotificationVo;

public interface NotificationDao {
	NotificationSearchResults findNotificationsByDeliverySpec(String eventUid, String deliverySpecPartial, String token, int max);
	List<NotificationVo> getNotificationsByEvent(String eventUid);
	Map<String,Integer> getStatusCountsByEventId(String eventUid);
	/**
	 * This is a best effort call.  Any NEW notification will be marked CANCELLED
	 * but that doesn't mean the record was already loaded and is being processed.
	 * @param eventUid
	 * @return the number of records updated.
	 */
	int markNotificationsCancelledForEvent(String eventUid);
}
