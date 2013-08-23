package com.railinc.wembley.legacy.senders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.MqDeliverySpecVo;
import com.railinc.wembley.api.notifications.model.Notification;
import com.railinc.wembley.api.util.ServiceLookupUtil;

public class MqMessageBuilderImpl extends AbstractMessageBuilder {

	private static final Logger log = LoggerFactory.getLogger( MqMessageBuilderImpl.class );

	private static final String MQ_MSG_HEADER = "#RRDC    0001";
	private static final String MQ_DATE_FORMATE = "%1$ty%1$tm%1$td%1$tH%1$tM";
	/* ******** HEADER INFORMATION
	Position 1 #
	Position 2-9 Origin (in this case RRDC)
	Position 10-13 Sequence Number
	Position 14-20 Message Type
	Position 21-26 YYMMDD
	Position 27-30 Time
	Position 31-38 - destination
	Position 39 - /
	******* END *********       */

	private static final int Cp500_hex9C = 0x00e6;
	private static final String MQ_TRAILER = "$0001EOM" + new String(new char[] {(char) Cp500_hex9C });
	/* ********* TRAILER INFROMATION
	Position 1 - $
	Position 2-5 � Group Count
	Position 6-10 � EMOM followed by hex 9c.
	*********** END ********/
	private List<TrainIIFormattingOptions> options = new ArrayList<TrainIIFormattingOptions>();
	
	protected TrainIIFormattingOptions getXmlOptions(String appId) {
		TrainIIFormattingOptions opt = (TrainIIFormattingOptions) ServiceLookupUtil.lookupService(appId, options, false);
		if (null == opt) {
			opt = getDefaultOptions();
		}
		return opt;
	}
	
	protected TrainIIFormattingOptions getDefaultOptions() {
		return new DefaultTrainIIOptions();
	}
	public MqMessageBuilderImpl() {
		log.info( "Instantiating MqMessageBuilderImpl with EventParser" );
	}

	/**
	 * Build a message from the given notification. It will take the body of the notification and
	 * wrap it with a TRAIN II envelope.
	 *
	 * @param Notification
	 */
	public String buildMessage( Notification notification ) {

		String message = null ;
		TrainIIFormattingOptions options = getXmlOptions(notification.getAppId());
		
		if ( log.isDebugEnabled() ) {
			log.debug(String.format( "Building MQ message for notification %s", notification ) );
		}

		if ( notification != null ) {

			// Make sure the delivery spec is an MQ delivery spec so that we get the correct delivery information
			MqDeliverySpecVo deliverySpec = notification.getDeliverySpec() instanceof MqDeliverySpecVo ?
					(MqDeliverySpecVo)notification.getDeliverySpec() : null;

			if ( deliverySpec != null ) {

				String msgBody = getMessageBody( notification , options);

				if ( msgBody != null && msgBody.length() > 0 ) {

					if ( log.isDebugEnabled() ) {
						log.debug( String.format( "Using delivery spect %s to deliver body:\n%s", deliverySpec, msgBody ) );
					}

					message = buildMqMessage( deliverySpec, msgBody, options );
				}
				else {
					if ( log.isDebugEnabled() ) {
						log.debug( String.format( "Message Body was empty or null: '%s'", msgBody ) );
					}
				}
			}
		}

		return message;
	}

	/*
	 * Wrap the given message body with a TRAIN II envelope
	 */
	private String buildMqMessage( MqDeliverySpecVo deliverySpec, String msgBody, TrainIIFormattingOptions opts ) {

		StringBuffer message = new StringBuffer();

		message.append( MQ_MSG_HEADER );
		message.append( String.format( "%-7s", StringUtils.rightPad( deliverySpec.getMessageType(), 7, '0' ) ) );
		message.append( String.format( MQ_DATE_FORMATE, new Date() ) );
		message.append( String.format( "%-8s", deliverySpec.getDestination() ) );
		message.append( "/*" );
		if (opts.insertCRLFAfterHeader()) {
			message.append("\n");
		}
		message.append( msgBody );
		if (opts.insertCRLFBeforeTrailer()) {
			message.append( "\n" );
		}
		message.append( MQ_TRAILER );

		return message.toString();
	}
	
	



	public List<TrainIIFormattingOptions> getOptions() {
		return options;
	}

	public void setOptions(List<TrainIIFormattingOptions> options) {
		this.options = options;
	}

	protected String getMessageBody(Notification not, TrainIIFormattingOptions options) {
		String body = null;
		
		try {
			Event event = not.getEvent();
			Object bodyElement = event != null && event.getEventBody() != null ? event.getEventBody().getBodyRoot() : null;

			if(bodyElement != null) {
				if(bodyElement instanceof Element) {
					Element e = (Element)bodyElement;
					body = new XmlFormatter().formatXml(e, 
							options.addXmlDecl(), 
							options.allowCRLFAfterXmlDecl(), 
							options.indentXml(), 
							options.indentXmlSize(), 
							options.trimXml(),
							options.convertCRLFInXmltoLF(),
							options.xmlEncoding());
				} else {
					body = bodyElement.toString();
				}
			} else {
				log.warn(String.format("The notification %s does not have valid event XML that can be parsed to retrieve the body.  No notification will be sent", not));
			}

		} catch (TransformerException e) {
			log.error(String.format("TransformerException trying to generate the XML for the notification %s.  Notification will not be delivered", not), e);
		} catch (NotificationServiceException e) {
			log.warn(String.format("The notification %s does not have valid event XML that can be parsed to retrieve the body.  No notification will be sent", not));
		}

		return body;
	}
}
