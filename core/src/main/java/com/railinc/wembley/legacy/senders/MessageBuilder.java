package com.railinc.wembley.legacy.senders;

import com.railinc.wembley.api.notifications.model.Notification;


public interface MessageBuilder {

	String buildMessage( Notification notification );
}
