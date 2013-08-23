package com.railinc.wembley.legacy.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.util.EventBruteForceParser;
import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacy.senders.EmailSenderService;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class InvalidEventMsgHandlerImpl implements InvalidEventMsgHandler {

	Logger log = LoggerFactory.getLogger(InvalidEventMsgHandlerImpl.class);
	private ApplicationDao applicationDao;

	private EmailSenderService emailSender;



	public void handleInvalidEventMsg(String eventXml, Throwable e) {
		if(log.isDebugEnabled()) {
			log.debug(String.format("Sending an invalid event to the poison message queue: %s\n%s", e, eventXml));
		}
		if (null == applicationDao) {
			return;
		}
		if (null == emailSender) {
			return;
		}
		
		EventBruteForceParser parser = new EventBruteForceParser();
		String appId = parser.extractAppId(eventXml);
		if (null == appId) {
			return;
		}
		String correlationId = parser.extractCorrelationId(eventXml);

		ApplicationVo application = applicationDao.getApplication(appId);
		if (null == application) {
			return;
		}
		String appAdminEmail = application.getAdminEmail();
		if (null == appAdminEmail) {
			return;
		}

		
		String from = "no-reply-notifserv@railinc.com";
		String subject = String.format("Notification Service Msg Failure: %s", correlationId);

		this.emailSender.sendEmail(from,appAdminEmail,subject, "Unable to parse the attached message, it has been sent to the DLQ :\n\n\n\n" + eventXml, eventXml.getBytes(), "text/plain","unparseable_event.txt");
	}


	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public void setEmailSender(EmailSenderService emailSender) {
		this.emailSender = emailSender;
	}

	
}
