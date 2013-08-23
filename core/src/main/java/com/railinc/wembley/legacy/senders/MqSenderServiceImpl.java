package com.railinc.wembley.legacy.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.core.NotificationServiceRemoteException;
import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.MqDeliverySpecVo;
import com.railinc.wembley.api.notifications.model.Notification;

public class MqSenderServiceImpl implements SenderService {

	private static final Logger log = LoggerFactory.getLogger(MqSenderServiceImpl.class);

	private String appId;

	private JmsTemplate mqMsgTemplate;

	private AbstractMessageBuilder messageBuilder;

	public MqSenderServiceImpl(String appId, JmsTemplate mqMsgTemplate, AbstractMessageBuilder messageBuilder) {

		this.appId = appId;
		this.mqMsgTemplate = mqMsgTemplate;
		this.messageBuilder = messageBuilder;

		log.info(String.format("Initialized MqSenderServiceImpl with AppId '%s' and a %s JmsTemplate", appId,
				mqMsgTemplate == null ? "null" : "non-null"));
	}

	/**
	 * This method is used to identify this class as an MQ sender. The lookup
	 * service will call this method when attempting to find specific senders.
	 */
	public Class<? extends DeliverySpec> getDeliverySpecType() {
		return MqDeliverySpecVo.class;
	}

	/**
	 * Sends the given notification via MQ. A message is built from the
	 * notification using the MqMessageBuilder.
	 *
	 * @param Notificaiton
	 */
	public void sendNotification(Notification notification) {

		if (log.isDebugEnabled()) {
			log.debug(String.format("[%s] Sending notifcation %s ", getAppId(), notification));
		}

		try {

			// Make sure we are in a valid state before attempting to send
			// notification
			if (mqMsgTemplate == null) {
				throw new NotificationServiceException(String.format("[%s] MqMsgTemplate is undefined in sendNotificaiton method!", getAppId()));
			}

			if (messageBuilder == null) {
				throw new NotificationServiceException(String.format("[%s] MqMessageBuilder is undefined in sendNotificaiton method!", getAppId()));
			}

			if (notification != null) {

				String message = messageBuilder.buildMessage(notification);

				// Send the message using the injected JMS Template
				mqMsgTemplate.convertAndSend(message);

				if (log.isDebugEnabled()) {
					log.debug(String.format("[%s] Notificaiton sent: %s ", getAppId(), notification));
				}
			}

		} catch (JmsException je) {
			log.error("JmsException trying to delivery notification via MQ", je);
			throw new NotificationServiceRemoteException(String.format("[%s] JMS Exception occurred in MqMsgSenderService.sendNotification!", getAppId()), je);
		}
	}

	public String getAppId() {
		return appId;
	}
}
