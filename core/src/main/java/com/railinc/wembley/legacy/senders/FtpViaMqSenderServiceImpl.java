package com.railinc.wembley.legacy.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.FtpViaMqDeliverySpecVo;

/**
 * Because Railinc has no 'good' way of doing FTP, this class will be used to 
 * FTP notifications using MQ. The bulk of the functionality is in the parent class.
 * 
 * @author SDWXD09
 *
 */
public class FtpViaMqSenderServiceImpl extends MqSenderServiceImpl {

	private static final Logger log = LoggerFactory.getLogger( FtpViaMqSenderServiceImpl.class );

	private String appId;

	public FtpViaMqSenderServiceImpl( String appId, JmsTemplate mqMsgTemplate, AbstractMessageBuilder messageBuilder ) {
		
		super( appId, mqMsgTemplate, messageBuilder );
		this.appId = appId;
		
		log.info( String.format( "Initialized FtpViaMqSenderServiceImpl with AppId '%s' and a %s JmsTemplate",
				appId, mqMsgTemplate == null ? "null" : "non-null" ) );
	}
	
	/**
	 * This method is used to identify this class as an FTP Via MQ sender. The lookup service will
	 * call this method when attempting to find specific senders.
	 */
	public Class<? extends DeliverySpec> getDeliverySpecType() {
		return FtpViaMqDeliverySpecVo.class;
	}

	public String getAppId() {
		return appId;
	}
}
