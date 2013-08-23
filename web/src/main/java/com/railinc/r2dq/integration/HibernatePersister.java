package com.railinc.r2dq.integration;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class HibernatePersister extends ChannelInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(HibernatePersister.class);
	
	public HibernatePersister() {
		logger.debug("New Instance : " + this);
	}
	
	private SessionFactory sessionFactory;

	@SuppressWarnings("rawtypes")
	@Transactional
	public Message save(Message<?> message) throws MessagingException {
		if (message == null) { return null; }
		Object payload = message.getPayload();
		sessionFactory.getCurrentSession().saveOrUpdate(payload);
		return message;
	}
	
	@Required
	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		return save(message);
	}

	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel channel) {
		return save(message);
	}



}
