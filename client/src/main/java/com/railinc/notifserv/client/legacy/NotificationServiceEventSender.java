package com.railinc.notifserv.client.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import com.railinc.notifserv.inbound.legacy.Event;

public class NotificationServiceEventSender {

	private static final Logger log = LoggerFactory.getLogger( NotificationServiceEventSender.class );

	private JmsTemplate eventMsgTemplate;

	public NotificationServiceEventSender( JmsTemplate eventMsgTemplate ) {

		this.eventMsgTemplate = eventMsgTemplate;

		log.info(String.format("Initialized NotificationServiceEventSender with a %s JmsTemplate", eventMsgTemplate == null ? "null" : "non-null"));
	}

	public void sendEvent( Event event ) {

		if ( log.isDebugEnabled() ) {
			log.debug( String.format( "Sending event %s ", event ) );
		}

		try {

			// Make sure we are in a valid state before attempting to send
			// notification
			if ( eventMsgTemplate == null ) {
				throw new NotificationServiceException( String.format("EventMsgTemplate is undefined in sendEvent method!" ) );
			}

			if ( event != null ) {

				EventMarshallerImpl marshaller = new EventMarshallerImpl();
				String eventXml = marshaller.marshalEvent( event );

				eventMsgTemplate.convertAndSend( eventXml );

				if ( log.isDebugEnabled() ) {
					log.debug(String.format( "Event sent: %s ", event ) );
				}
			}
		}
		catch ( JmsException je ) {
			throw new NotificationServiceException( String.format( "JMS Exception occurred in NotificationServiceEventSender.sendEvent!" ), je);
		}
	}
}
