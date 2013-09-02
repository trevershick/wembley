package com.railinc.wembley.api;

import com.railinc.wembley.domain.Message;

public interface NotificationRepository {
	Long store(Notification notif);
}
