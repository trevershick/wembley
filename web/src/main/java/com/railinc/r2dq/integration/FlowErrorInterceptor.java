package com.railinc.r2dq.integration;

import org.apache.commons.lang.StringUtils;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;
import org.springframework.integration.support.MessageBuilder;

public class FlowErrorInterceptor extends ChannelInterceptorAdapter {
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		MessagingException messagingException = (MessagingException)message.getPayload();
		Message<?> failedMessage = messagingException.getFailedMessage();
		
		if(StringUtils.isBlank(failedMessage.getHeaders().get("FLOW_NAME", String.class))){
			messagingException.setFailedMessage(MessageBuilder.fromMessage(failedMessage).setHeader("FLOW_NAME", "NONE_AVAILABLE").build());
			
		}
		
		return MessageBuilder.fromMessage(message).build();
	}
}
