package com.railinc.stc.notifserv.senders.impl;



import java.util.ArrayList;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.railinc.wembley.app.Application;
import com.railinc.wembley.app.dao.ApplicationDao;

import com.railinc.wembley.event.FailedEventReason;
import com.railinc.wembley.event.model.DeliverySpec;
import com.railinc.wembley.event.model.DeliverySpecs;
import com.railinc.wembley.event.model.Event;
import com.railinc.wembley.event.model.impl.EmailAttachmentVo;
import com.railinc.wembley.event.model.impl.EmailDeliverySpecVo;
import com.railinc.wembley.event.model.impl.FindUsRailContactVo;
import com.railinc.wembley.event.model.impl.FindUsRailDeliverySpecVo;
import com.railinc.wembley.event.model.impl.SSOUserDeliverySpecVo;

import com.railinc.wembley.notifications.model.impl.AdminFailedEventNotificationVo;
import com.railinc.wembley.senders.AdminEmailFailedEventNotifier;
import com.railinc.wembley.senders.EmailSenderService;

public class AdminEmailFailedEventNotifierImpl implements AdminEmailFailedEventNotifier{
	
	private static final Logger log = LoggerFactory.getLogger(AdminEmailFailedEventNotifierImpl.class);
	private ApplicationDao applicationDao;	
	private EmailSenderService emailSender;
	
	public AdminEmailFailedEventNotifierImpl() {
		log.info("Instantiating the STC - AdminEmailFailedEventNotifierImpl");
	}
	
	public void sendAdminEmailForFailedEvent(String appId, Event event, FailedEventReason reason, Throwable e) {

		Application app = applicationDao == null ? null : applicationDao.getApplication(appId);
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
	
	
		private EmailDeliverySpecVo createEmailDeliverySpec(String adminEmail, Event event, AdminFailedEventNotificationVo not) {
			EmailDeliverySpecVo spec = new EmailDeliverySpecVo();

			String corrId = event != null && event.getEventHeader() != null ? event.getEventHeader().getCorrelationId() : "";			
			spec.setFrom("no-reply-notifserv@railinc.com");
			spec.setSubject(String.format("Notification Service Msg Failure: %s : Reason : %s", corrId , not.getFailedEventReason()));
			spec.setContentType("text/plain");
			spec.setTo(adminEmail);
			spec.setAttachments(new ArrayList<EmailAttachmentVo>());
			extractAndAttachAttachments(event,spec);
			return spec;
		}
		
	
		
		private void extractAndAttachAttachments(Event event,EmailDeliverySpecVo adminEmailSpec)
		{
			
			if(event != null && event.getEventHeader() != null && event.getEventHeader().getDeliverySpecs() != null) {
				DeliverySpecs delSpecs = event.getEventHeader().getDeliverySpecs();
				if(delSpecs.getDeliverySpecs() != null) {					
					for(DeliverySpec spec : delSpecs.getDeliverySpecs()) {
						if ( spec != null && (spec instanceof EmailDeliverySpecVo || spec instanceof FindUsRailDeliverySpecVo || spec instanceof SSOUserDeliverySpecVo ) ) {
							if (spec instanceof EmailDeliverySpecVo)
							{
								EmailDeliverySpecVo emailSpec = (EmailDeliverySpecVo)spec;
								if (emailSpec.getAttachments() != null)
								{
									for(EmailAttachmentVo attach : emailSpec.getAttachments()) {
										if(attach != null && attach.getAttachmentName() != null && attach.getAttachment() != null) {
											adminEmailSpec.getAttachments().add(attach);
										}
									}
								}
							}	
							if (spec instanceof FindUsRailDeliverySpecVo)
							{
								FindUsRailDeliverySpecVo findUsRailSpec = (FindUsRailDeliverySpecVo)spec;
								// If it is FindUsRail failure - Lets get the company Id Out and put in subject
								adminEmailSpec.setSubject(String.format("Notification Failure For Company(s): %s", getCompanyForFindUsRailFailure(findUsRailSpec)));
								if (findUsRailSpec.getAttachments() != null)
								{
									for(EmailAttachmentVo attach : findUsRailSpec.getAttachments()) {
										if(attach != null && attach.getAttachmentName() != null && attach.getAttachment() != null) {											
											adminEmailSpec.getAttachments().add(attach);
										}
									}
								}
							}	
							
							if (spec instanceof SSOUserDeliverySpecVo)
							{
								SSOUserDeliverySpecVo ssoSpec = (SSOUserDeliverySpecVo)spec;
								if (ssoSpec.getAttachments() != null)
								{
									for(EmailAttachmentVo attach : ssoSpec.getAttachments()) {
										if(attach != null && attach.getAttachmentName() != null && attach.getAttachment() != null) {
											adminEmailSpec.getAttachments().add(attach);
										}
									}
								}
							}	
						}
					}		
				}
			}
		}
		
		private String getCompanyForFindUsRailFailure(FindUsRailDeliverySpecVo findUsRailSpec)
		{
			StringBuffer sb = new StringBuffer();
			if (findUsRailSpec != null){			
				for(FindUsRailContactVo findUsRailContact : findUsRailSpec.getContacts())
				{
					sb.append(findUsRailContact.getCompanyId());
				}			
			}
			return sb.toString();
		}
		
	
		public void setApplicationDao(ApplicationDao applicationDao) {
			this.applicationDao = applicationDao;
		}

		public void setEmailSender(EmailSenderService emailSender) {
			this.emailSender = emailSender;
		}
}
