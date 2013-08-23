package com.railinc.r2dq.integration;


import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;

import com.railinc.r2dq.domain.OutboundMessage;
import com.railinc.r2dq.messages.MessageService;

public class OutboundMessageInterceptor extends ChannelInterceptorAdapter {
	
	private Destination destination;
	private boolean pubSubdomain;
	private MessageService messageService;
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		OutboundMessage outboundMessage = new OutboundMessage();
		outboundMessage.setData((String)message.getPayload());
		try {
			String outbound ="";
			if(pubSubdomain){
				outbound = ((Topic)destination).getTopicName();
				outbound = outbound.replace("topic:///", "");
			}else{
				outbound = ((Queue)destination).getQueueName();
				outbound = outbound.replace("queue:///", "");
			}
			outboundMessage.setOutbound(outbound);
		} catch (JMSException ex) {
			ex.printStackTrace();
		}
		messageService.log(outboundMessage);
		return message;
	}
	
	@Required
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	
	public void setPubSubdomain(boolean pubSubdomain) {
		this.pubSubdomain = pubSubdomain;
	}
	
	@Required
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
}
