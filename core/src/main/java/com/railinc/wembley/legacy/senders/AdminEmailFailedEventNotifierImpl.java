package com.railinc.wembley.legacy.senders;

import java.util.ArrayList;

import javax.activation.DataHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.EmailAttachmentVo;
import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.EventMarshaller;
import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.api.notifications.model.AdminFailedEventNotificationVo;
import com.railinc.wembley.api.notifications.model.NotificationVo;
import com.railinc.wembley.api.util.ByteArrayDataSource;
import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class AdminEmailFailedEventNotifierImpl implements AdminEmailFailedEventNotifier {

	private static final Logger log = LoggerFactory.getLogger(AdminEmailFailedEventNotifierImpl.class);
	private ApplicationDao applicationDao;
	private EventMarshaller eventMarshaller;
	private EmailSenderServiceImpl emailSender;


	public AdminEmailFailedEventNotifierImpl() {
		log.info("Instantiating the AdminEmailFailedEventNotifierImpl");
	}

	public void sendAdminEmailForFailedEvent(String appId, Event event, FailedEventReason reason, Throwable e) {

		ApplicationVo app = applicationDao == null ? null : applicationDao.getApplication(appId);
		String appAdminEmail = app == null ? null : app.getAdminEmail();

		if(log.isDebugEnabled()) {
			log.debug(String.format("Sending a failed event for App ID %s with a reason of %s to email %s", appId, reason, appAdminEmail));
		}

		if(appAdminEmail != null && this.emailSender != null) {
			AdminFailedEventNotificationVo not = new AdminFailedEventNotificationVo();
			not.setAppId(appId);
			not.setFailedEventReason(reason);
			not.setException(e);
			not.setDeliveryTiming("0");
			not.setEvent(event);
			not.setDeliverySpec(createEmailDeliverySpec(appAdminEmail, event, not));
			not.setEventUid(event == null ? null : event.getEventUid());

			this.emailSender.sendNotification(not);
		}
	}

	private EmailDeliverySpecVo createEmailDeliverySpec(String adminEmail, Event event, NotificationVo not) {
		EmailDeliverySpecVo spec = new EmailDeliverySpecVo();

		String corrId = event != null && event.getEventHeader() != null ? event.getEventHeader().getCorrelationId() : "";
		spec.setFrom("no-reply-notifserv@railinc.com");
		spec.setSubject(String.format("Notification Service Msg Failure: %s", corrId));
		spec.setContentType("text/plain");
		spec.setTo(adminEmail);
		spec.setAttachments(new ArrayList<EmailAttachmentVo>());
		spec.getAttachments().add(createEmailAttachment(event, not));

		return spec;
	}

	private EmailAttachmentVo createEmailAttachment(Event event, NotificationVo not) {
		EmailAttachmentVo att = new EmailAttachmentVo();
		att.setAttachmentName("event.xml");
		att.setContentType("application/xml");

		try {
			if(this.eventMarshaller != null) {
				String xml = eventMarshaller.marshalEvent(event);

				if(log.isDebugEnabled()) {
					log.debug(String.format("Event XML being attached to admin email for failed event:\n %s", xml));
				}

				not.setEventXml(xml);
				DataHandler dh = new DataHandler(new ByteArrayDataSource(xml, "application/xml"));
				att.setAttachment(dh);
			}
		} catch (Throwable e) {
			log.error("Exception trying to attach the event to the failed event notification, the event will not be attached", e);
		}

		return att;
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public void setEmailSender(EmailSenderServiceImpl emailSender) {
		this.emailSender = emailSender;
	}

	public void setEventMarshaller(EventMarshaller eventMarshaller) {
		this.eventMarshaller = eventMarshaller;
	}
}
