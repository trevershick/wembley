package com.railinc.r2dq.correspondence;

import static com.google.common.collect.Collections2.transform;

import java.util.Collection;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.railinc.notifserv.inbound.legacy.Event;
import com.railinc.notifserv.inbound.legacy.EventHeader;
import com.railinc.r2dq.configuration.R2DQConfigurationService;

public class CorrespondenceToNotifServTransformer implements Function<Correspondence, Event> {
	
	private R2DQConfigurationService configuration;
	
	
	@Override
	public Event apply(Correspondence input) {
		// Taken from - http://wikitrain.railinc.com/x/iQAUAw
		
		
		
	    // Create the Event
	    Event event = new Event();
	 
	    // Create and set the Event Header
	    EventHeader eventHeader = new EventHeader();
	     
	    //APP ID is your notification service defined APP ID (should come from application property)
	    eventHeader.setAppId(configuration.getNotificationServiceAppId());
	     
	    //Optional correlation ID that will be stored with your event in Notification Service
	    //Useful for auditing and debugging, any string value is good)
	    eventHeader.setCorrelationId(input.getUuid());
	 
	    //Add optional parameters (name/value pairs) to the header
	    //Useful for auditing if more information than a correlation ID is needed
	    //Also useful for custom functionality defined in the Notification Service
	    //To pass meta data to the service in the event header
	    //Values probably would be derived from application logic or properities
	    //Any length strings are valid for the name and value), 0-many params can be added
	    //eventHeader.addEventParam("myName", "myValue");
	     
	    // Create your delivery spec (Email in this case)
	    // Actual from address & subject probably should come from some 
	    // application property or application logic value
	    // Actual to address probably should from a value derived from
	    // application logic, but may come from application property in some cases.
	    EmailDeliverySpec emailDS = new EmailDeliverySpec();
	     
	    //Must be a single address
	    Contact from = input.getFrom();
	    if (from == null) {
	    	from = configuration.getDefaultFrom();
	    }
	    emailDS.setFrom(from.getEmailAddress());
	     
	    //Can be semi-colon separated list of multiple addresses
	    Collection<Contact> recipients = input.getRecipients();
	    Collection<String> recips = transform(recipients, new Function<Contact,String>(){
			@Override
			public String apply(Contact input) {
				return input.getEmailAddress();
			}});
	    String to = Joiner.on(';').skipNulls().join(recips);
	    emailDS.setTo(to);
	     
	    //Any string value is valid here (optional, but results in email with no subject)
	    emailDS.setSubject(input.getSubject());
	     
	    //Optional Email Delivery Spec Options
	     
	    //MIME type of the email body to send 
	    //Use standard MIME types, 
	    //Default is text/plain
	    //Set to text/html if active hyperlinks in email are desired
	    emailDS.setContentType(contentType(input)); 
	     
	    //Optional cc and bcc options to send to CC and BCC recipients
	    //Both can be semi-colon separated list of multiple addresses
	    //emailDS.setCc("copy@railinc.com");
	    //emailDS.setBcc("bcc@railinc.com");
	     
	    //See below for example on adding email attachements
	     
	    //Add the delivery spec to the event header 
	    //Multiple delivery specs can be added to the same event for different
	    //delivery options (e.g. Both an EmailDeliverySpecVo and a FindUsRailDeliverySpecVo
	    //can be added to the same EventVo)
	    eventHeader.addDeliverySpec(emailDS);
	    event.setHeader(eventHeader);
	    event.setBody(value);
	     
	    // Set your message body (Text in this case, but an XML version is available - see below)
	    JAXBElement<String> x = new JAXBElement<String>(name, String.class, content(input));
	    event.setTextBody(content(input));
	    return event;
	}


	private String content(Correspondence input) {
		return input.isHtml() ? input.getTextHtml() : input.getTextPlain();
	}


	private String contentType(Correspondence input) {
		return input.isHtml() ? "text/html" : "text/plain";
	}


	public R2DQConfigurationService getConfiguration() {
		return configuration;
	}

	@Required
	public void setConfiguration(R2DQConfigurationService configuration) {
		this.configuration = configuration;
	}

}
