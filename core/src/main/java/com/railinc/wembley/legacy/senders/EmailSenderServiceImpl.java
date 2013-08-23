package com.railinc.wembley.legacy.senders;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationServiceRemoteException;
import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.api.notifications.model.Notification;
import com.railinc.wembley.api.util.ByteArrayDataSource;
import com.railinc.wembley.legacy.mail.NotifServMailSession;

public class EmailSenderServiceImpl implements EmailSenderService {

	private static final Logger log = LoggerFactory.getLogger(EmailSenderServiceImpl.class);

	private String appId;
	
	private Session session;
	private EmailMessageBuilder msgBuilder;

	/*
	 * This whole notion of the notif serv environment key could probably be better 
	 * implemented if we had a StringValue interface that was injected into the osgi context and then
	 * it could be looked up by the other objects... for now this is what we have 
	 */
	public static final String NOTIF_SERV_ENVIRONMENT_KEY = "environmentName";
	private Properties notifServProperties;
	
	public void setNotifServProperties(Properties notifServProperties) {
		this.notifServProperties = notifServProperties;
	}

	public EmailSenderServiceImpl(String appId) {
		this.appId = appId;
		log.info("Initialzing EmailSenderServiceImpl for AppID " + appId);
	}

	public void sendNotification(Notification notification) {
		if(mailSession != null && mailSession.getMailSession() != null && notification != null && msgBuilder != null) {
			MimeMessage msg = msgBuilder.buildEmailMessage(notification, session);
			if(msg != null) {
				try {
					Transport.send(msg);
				} catch (Throwable e) {
					log.error(String.format("Exception sending the notification %s, Notification will not be delivered", notification), e);
					throw new NotificationServiceRemoteException(e);
				}
			}
		}
	}

	public String getAppId() {
		return appId;
	}

	public Class<? extends DeliverySpec> getDeliverySpecType() {
		return EmailDeliverySpecVo.class;
	}

	public void setMailSession(NotifServMailSession mailSession) {
		this.mailSession = mailSession;
	}

	public void setMsgBuilder(EmailMessageBuilder msgBuilder) {
		this.msgBuilder = msgBuilder;
	}
	/**
	 * Fails silently
	 */
	public void sendEmail(String from, String to, String subject, String body,
			byte[] attachment, String contentType, String fileName) {
		Session session = this.mailSession.getMailSession();
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(RecipientType.TO, new InternetAddress(to));
			msg.setSubject(getEnvironmentNamePrefix() + subject);
			
			Multipart multipart = new MimeMultipart();
			//Setup the body
			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setText(body);
			

			bodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain")));
			multipart.addBodyPart(bodyPart);

			if (attachment != null && contentType != null && fileName != null) {
				//Add any attachments
				MimeBodyPart attachPart = new MimeBodyPart();
				attachPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment, contentType)));
				attachPart.setFileName(fileName);
				multipart.addBodyPart(attachPart);
			}
			msg.setContent(multipart);
				
			Transport.send(msg);
		} catch (Throwable t) {
			log.error("Unable to send email", t);
		}
		
	}
	
	public String getEnvironmentNamePrefix() {

		String envName = notifServProperties == null ? "" : (String)this.notifServProperties.get( NOTIF_SERV_ENVIRONMENT_KEY );
		String prefix = "";

		if ( envName != null && envName.length() > 0 ) {
			prefix = envName + ": ";
		}

		return prefix;
	}

}
