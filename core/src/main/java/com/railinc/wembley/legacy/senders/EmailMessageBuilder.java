package com.railinc.wembley.legacy.senders;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.railinc.wembley.api.notifications.model.Notification;


public interface EmailMessageBuilder {

	MimeMessage buildEmailMessage(Notification not, Session mailSession);
}
