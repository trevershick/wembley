package com.github.trevershick.wembley.api;

import com.github.trevershick.wembley.domain.Message;

public interface NotificationRepository {
	Long store(Notification notif);
}
