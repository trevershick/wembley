package com.railinc.wembley.legacy.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

public class DefaultInvalidEventMsgHandler implements InvalidEventMsgHandler {

	Logger log = LoggerFactory.getLogger(DefaultInvalidEventMsgHandler.class);
	private JmsTemplate poisonMsgTemplate;

	public void handleInvalidEventMsg(String eventXml, Throwable e) {
		if(log.isDebugEnabled()) {
			log.debug(String.format("Sending an invalid event to the poison message queue: %s\n%s", e, eventXml));
		}
		if(poisonMsgTemplate != null) {
			if(eventXml == null) {
				poisonMsgTemplate.convertAndSend("Null application event message was received");
			} else {
				poisonMsgTemplate.convertAndSend(eventXml);
			}
		}
	}

	public void setPoisonMsgTemplate(JmsTemplate poisonMsgTemplate) {
		log.info("Setting the JmsTemplate on the DefaultInvalidEventMsgHandler");
		this.poisonMsgTemplate = poisonMsgTemplate;
	}
}
