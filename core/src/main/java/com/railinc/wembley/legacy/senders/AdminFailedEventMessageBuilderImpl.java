package com.railinc.wembley.legacy.senders;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.api.notifications.model.AdminFailedEventNotificationVo;
import com.railinc.wembley.api.notifications.model.Notification;

public class AdminFailedEventMessageBuilderImpl extends EmailMessageBuilderImpl {

	public AdminFailedEventMessageBuilderImpl() {
		super();
	}

	@Override
	protected String getMessageBody(Notification not) {
		String body = null;
		if(not != null && not instanceof AdminFailedEventNotificationVo) {
			AdminFailedEventNotificationVo adminNot = (AdminFailedEventNotificationVo)not;

			if(adminNot.getFailedEventReason() == FailedEventReason.NO_MATCHING_SUBSCRIPTIONS) {
				body = createBody(adminNot, "could not be matched against any valid destination end points.", false,
									"No notifications were delivered for this event.");
			} else if(adminNot.getFailedEventReason() == FailedEventReason.EXCEPTION_RECEIVING_EVENT) {
				body = createBody(adminNot, "failed due to a system exception while receiving, parsing, or persisting the event", true,
									"No notifications were delivered for this event.");
			} else if(adminNot.getFailedEventReason() == FailedEventReason.EXCEPTION_DELIVERING_NOTIFICATION) {
				body = createBody(adminNot, "failed due to a system exception while delivering the notification", true,
									"One or more notifications for this event were not successfully delivered.");
			} else if(adminNot.getFailedEventReason() == FailedEventReason.EXCEPTION_LOADIING_SUBSCRIPTIONS) {
				body = createBody(adminNot, "failed due to a system exception while loading subscriptions", true,
									"One or more potential notifications for this event will not be delivered.");
			} else if(adminNot.getFailedEventReason() == FailedEventReason.EXCEPTION_POSTING_NOTIFICATION) {
				body = createBody(adminNot, "failed due to a system exception while posting notifications", true,
									"One or more notifications for this event will not be delivered.");
			} else if(adminNot.getFailedEventReason() == FailedEventReason.INVALID_DELIVERY_SPEC) {
				body = createBody(adminNot, "contained an invalid delivery spec", true,
									"No notifications will be delivered for the end points specified in this delivery spec.");
			} else {
				body = createBody(adminNot, "failed for an unknown reason", true,
									"This event may or may not have generated any notifications.");
			}
		}
		return body;
	}

	private String createBody(AdminFailedEventNotificationVo adminNot, String msg, boolean includeException, String trailer) {
		StringWriter bodyStr = new StringWriter();

		bodyStr.append("The attached event ");
		bodyStr.append(msg);

		if(includeException) {
			bodyStr.append(":\n");
			writeException(adminNot, bodyStr);
		}

		bodyStr.append("\n");
		bodyStr.append(trailer);

		return bodyStr.getBuffer().toString();
	}

	private void writeException(AdminFailedEventNotificationVo adminNot, StringWriter bodyStr) {
		if(adminNot.getException() != null) {
			PrintWriter stack = new PrintWriter(bodyStr);
			adminNot.getException().printStackTrace(stack);
		} else {
			bodyStr.append("Unknown System Exception");
		}
	}
}
