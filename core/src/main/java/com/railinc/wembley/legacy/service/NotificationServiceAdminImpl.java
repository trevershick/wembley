package com.railinc.wembley.legacy.service;

import java.util.List;
import java.util.Map;

import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacy.domain.dao.EventDao;
import com.railinc.wembley.legacy.domain.dao.NotificationDao;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;
import com.railinc.wembley.legacysvc.domain.EventVo;
import com.railinc.wembley.legacysvc.domain.NotificationVo;

public class NotificationServiceAdminImpl implements NotificationServiceAdmin {
	public static final int DEFAULT_MAX_RECORDS = 5;
	private static final String ERROR_ID_CANNOT_BE_NULL = "id cannot be null";
	private ApplicationDao applicationDao;
	private EventDao eventDao;
	private NotificationDao notificationDao;

	public List<ApplicationVo> getAllApplications() {
		return getApplicationDao().getAllApplications();
	}

	public ApplicationVo getApplication(String applicationId) {
		if (null == applicationId) {
			throw new IllegalArgumentException("applicationId cannot be null");
		}
		return getApplicationDao().getApplication(applicationId);
	}

	public Map<String, Integer> getStatusCountsByEventId(String eventUid) {
		return notificationDao.getStatusCountsByEventId(eventUid);
	}

	public NotificationSearchResults findNotificationsByDeliverySpec(
			String eventUid, String deliverySpecPartial, String token, int max) {
		if (eventUid == null || deliverySpecPartial == null) {
			throw new IllegalArgumentException("eventUid and deliverySpecPartial cannot be null");
		}
		int maximum = max;
		if (max < 1) {
			maximum = DEFAULT_MAX_RECORDS;
		}
		return getNotificationDao().findNotificationsByDeliverySpec(eventUid,
				deliverySpecPartial, token, maximum);
	}

	public EventVo getEventByCorrelationId(String appId,
			String correlationId) {
		return getEventDao().getEventByCorrelationId(appId, correlationId);
	}

	public NotificationDao getNotificationDao() {
		return notificationDao;
	}

	public void setNotificationDao(NotificationDao notificationDao) {
		this.notificationDao = notificationDao;
	}

	public ApplicationDao getApplicationDao() {
		return applicationDao;
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public EventDao getEventDao() {
		return eventDao;
	}

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	public EventVo getEvent(String uid) {
		if (null == uid) {
			throw new IllegalArgumentException("uid cannot be null");
		}
		return getEventDao().getEvent(uid);
	}

	public List<NotificationVo> getNotificationsForEvent(String eventUid) {
		if (null == eventUid) {
			throw new IllegalArgumentException(ERROR_ID_CANNOT_BE_NULL);
		}

		return getNotificationDao().getNotificationsByEvent(eventUid);
	}

	public ApplicationVo createApplication(ApplicationVo application) {
		getApplicationDao().insertApplication(application);
		return getApplication(application.getAppId());
	}

	public ApplicationVo updateApplication(String id, ApplicationVo vo) {
		// if the app id changes, there's perhaps other stuff to update?
		if (id == null || vo == null) {
			throw new IllegalArgumentException("id and vo cannot be null");
		}
		return getApplicationDao().updateApplication(id, vo);
	}

	public void deleteApplication(String id) {
		if (id == null) {
			throw new IllegalArgumentException(ERROR_ID_CANNOT_BE_NULL);
		}
		getApplicationDao().deleteApplication(id);
	}

	public int cancelNotifications(String eventUid) {
		if (eventUid == null) {return 0;}
		
		int markedCancelled = getNotificationDao().markNotificationsCancelledForEvent(eventUid);
		return markedCancelled;
	}

}
