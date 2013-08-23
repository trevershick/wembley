package com.railinc.wembley.legacy.event.receiving;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.mail.Message;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.SimpleMessageConverter;


public class JmsMessageConverter extends SimpleMessageConverter {

	@Override
	public Object fromMessage(Message msg) throws JMSException, MessageConversionException {
		if(msg instanceof TextMessage) {
			return super.fromMessage(msg);
		} else {
			return String.format("Invalid Message Type: %s", msg == null ? "null" : msg.getClass().getName());
		}
	}
}
