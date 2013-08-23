package com.railinc.wembley.legacy.senders;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.EmailAttachmentVo;
import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.notifications.model.Notification;
import com.railinc.wembley.api.util.ByteArrayDataSource;

public class EmailMessageBuilderImpl implements EmailMessageBuilder {

	public static final String DEFAULT_EMAIL_XML_ENCODING = "ISO-8859-1";


	private static final String NOTIF_SERV_ENVIRONMENT_KEY = "environmentName";

	
	private static final Logger log = LoggerFactory.getLogger(EmailMessageBuilderImpl.class);

	private Properties notifServProperties;

	public EmailMessageBuilderImpl() {
		log.info("Instantiating EmailMessageBuilderImpl with EventParser");
		MailcapCommandMap map = new MailcapCommandMap();
		map.addMailcap("multipart/*;; x-java-content-handler=com.railinc.wembley.senders.impl.internal.MultipartMixedContentHandler\n");
		CommandMap.setDefaultCommandMap(map);
		log.info("Setup the multipart content handler for emails");
	}

	public MimeMessage buildEmailMessage(Notification not, Session mailSession) {

		if(log.isDebugEnabled()) {
			log.debug(String.format("Sending notifcation %s with mailSession %s", not, mailSession));
		}

		MimeMessage msg = null;
		EmailDeliverySpecVo delSpec = not != null && not.getDeliverySpec() != null &&
			not.getDeliverySpec() instanceof EmailDeliverySpecVo ? (EmailDeliverySpecVo)not.getDeliverySpec() : null;
		String body = not != null ? getMessageBody(not) : null;

		if(log.isDebugEnabled()) {
			log.debug(String.format("Sending Using Delivery Spec %s\nBody: %s", delSpec, body));
		}

		if(delSpec != null && body != null && mailSession != null) {
			try {
				msg = initializeMsg(delSpec, mailSession);
				if(msg != null) {
					//Create the multipart content for the message
					Multipart multipart = new MimeMultipart();

					//Setup the body
					MimeBodyPart bodyPart = new MimeBodyPart();
					bodyPart.setText(body);
					String contentType = delSpec.getContentType() == null ? "text/plain" : delSpec.getContentType();

					bodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(body, contentType)));
					multipart.addBodyPart(bodyPart);

					//Add any attachments
					if(delSpec.getAttachments() != null) {
						for(EmailAttachmentVo attach : delSpec.getAttachments()) {
							if(attach != null && attach.getAttachmentName() != null && attach.getAttachment() != null) {
								MimeBodyPart attachPart = new MimeBodyPart();
								attachPart.setDataHandler(attach.getAttachment());
								attachPart.setFileName(attach.getAttachmentName());
								multipart.addBodyPart(attachPart);
							}
						}
					}

					msg.setContent(multipart);
				}
			} catch (MessagingException e) {
				log.error(String.format("Exception trying to create email message for Notification %s, Notification will not be sent", not));
				msg = null;
			}
		} else {
			if(mailSession == null) {
				log.error("MailSession in EmailMessageBuilderImpl was null!  Cannot send email notifications!");
			} else {
				if(delSpec == null) {
					log.info(String.format("The notifcation %s did not have a valid delivery spec or itself was null", not));
				}
				if(body == null) {
					log.info(String.format("The notifcation %s did not have valid body or itself was null", not));
				}
			}
		}

		return msg;
	}

	protected MimeMessage initializeMsg(EmailDeliverySpecVo delSpec, Session mailSession) throws MessagingException {

		String envPrefix = getEnvironmentNamePrefix();

		MimeMessage msg = null;
		Address[] fromAddr = parseAddresses(delSpec.getFrom());
		Address[] toAddr = parseAddresses(delSpec.getTo());
		Address[] ccAddr = parseAddresses(delSpec.getCc());
		Address[] bccAddr = parseAddresses(delSpec.getBcc());

		String mailDebug = System.getProperty("notifserv.mail.session.debug");
		if("true".equals(mailDebug)) {
			mailSession.setDebug(true);
		}

		if(fromAddr.length > 0 && toAddr.length > 0) {
			msg = new MimeMessage(mailSession);
			msg.addFrom(fromAddr);
			msg.addRecipients(RecipientType.TO, toAddr);
			if(ccAddr.length > 0) {
				msg.addRecipients(RecipientType.CC, ccAddr);
			}
			if(bccAddr.length > 0) {
				msg.addRecipients(RecipientType.BCC, bccAddr);
			}

			if(delSpec.getSubject() != null) {
				msg.setSubject( envPrefix + delSpec.getSubject().trim() );
			}
			else if ( delSpec.getSubject() == null && envPrefix != null && envPrefix.length() > 0 ) {
				msg.setSubject( envPrefix );
			}
		} else {
			log.info(String.format("The delSpec %s does not have a from or to address and no notification can be sent", delSpec));
		}
		return msg;
	}

	protected Address[] parseAddresses(String address) {
		List<Address> addrList = new ArrayList<Address>();
		if(address  != null) {
			String[] addresses = address.split(";");
			for(String a : addresses) {
				try {
					addrList.add(new InternetAddress(a));
				} catch (AddressException e) {
					log.warn(String.format("The email address %s could not be successfully parsed into a valid email " +
							"address so no notification will be sent from/to this sender/recipient", a));
				}
			}
		}
		return addrList.toArray(new Address[addrList.size()]);
	}

	protected String getMessageBody(Notification not) {
		String body = null;
		try {
			Event event = not.getEvent();
			Object bodyElement = event != null && event.getEventBody() != null ? event.getEventBody().getBodyRoot() : null;

			if(bodyElement == null) {
				log.warn(String.format("The notification %s does not have valid event XML that can be parsed to retrieve the body.  No notification will be sent", not));
				return body;
			}
			body = bodyElement.toString();
			
			if(bodyElement instanceof Element) {
				boolean addXmlDecl = true;
				boolean allowCRLFAfterXmlDecl = true;
				boolean indentXml = true;
				boolean trimXml = false;
				boolean convertCRLFtoLF = false;
				
				int indentSize = 1;
				body = new XmlFormatter().formatXml((Element) bodyElement, 
						addXmlDecl, 
						allowCRLFAfterXmlDecl, 
						indentXml,
						indentSize, 
						trimXml,
						convertCRLFtoLF, 
						DEFAULT_EMAIL_XML_ENCODING); 
			}
		} catch (TransformerException e) {
			log.error(String.format("TransformerException trying to generate the XML for the notification %s.  Notification will not be delivered", not), e);
		} catch (NotificationServiceException e) {
			log.warn(String.format("The notification %s does not have valid event XML that can be parsed to retrieve the body.  No notification will be sent", not));
		}

		return body;
	}



	public void setNotifServProperties( Properties notifServProperties ) {
		this.notifServProperties = notifServProperties;
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
