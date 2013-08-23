package com.railinc.r2dq.messages;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.railinc.r2dq.domain.GenericMessage;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.OutboundMessage;
import com.railinc.r2dq.domain.YesNo;
import com.railinc.r2dq.domain.views.RawInboundMessageToView;
import com.railinc.r2dq.domain.views.RawInboundMessageView;
import com.railinc.r2dq.integration.Queue;
import com.railinc.r2dq.log.SystemAuditLogTraceEvent;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.QueryHelper;

@Service
@Transactional
public class MessageServiceImpl implements MessageService, ApplicationContextAware {

	private SessionFactory sessionFactory;
	
	private Queue queue;
	
	private ApplicationContext eventPublisher;
	
	private JmsTemplate jmsTemplate;

	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Required
	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	@Transactional
	public PagedCollection<RawInboundMessageView> all(MessageSearchCriteria criteria) {
		criteria = Optional.fromNullable(criteria).or(new MessageSearchCriteria());
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(GenericMessage.class);
		
		if(InboundMessage.isInbound(criteria.getType().value())){
			c.add(Restrictions.eq("class", InboundMessage.class));
		}
		
		if(OutboundMessage.isOutbound(criteria.getType().value())){
			c.add(Restrictions.eq("class", OutboundMessage.class));
		}
		
		QueryHelper.inOrIsNull(c, criteria.getSources(), GenericMessage.PROPERTY_INBOUND_SOURCE);
		QueryHelper.eqOrIsNull(c, criteria.getProcessed(), GenericMessage.PROPERTY_PROCESSED);
		QueryHelper.freeTextSearchFor(c, criteria.getData(), GenericMessage.PROPERTY_DATA);
		
		return QueryHelper.query(criteria.getPagingParameters(), c,Order.desc(GenericMessage.PROPERTY_CREATED), new RawInboundMessageToView());
	}

	@Override
	@Transactional
	public void save(InboundMessage s) {
		boolean create = s.getIdentifier() == null;
		sessionFactory.getCurrentSession().saveOrUpdate(s);
		if(create){
			publishSystemAuditLogTraceEvent("CREATE_INBOUND_MESSAGE", s);
		}else {
			publishSystemAuditLogTraceEvent("UPDATE_INBOUND_MESSAGE", s);
		}
		
	}
	
	@Override
	@Transactional
	public void save(OutboundMessage s) {
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	@Override
	@Transactional
	public void delete(InboundMessage s) {
		sessionFactory.getCurrentSession().delete(s);
	}

	@Override
	@Transactional
	public GenericMessage get(Long id) {
		return (GenericMessage) sessionFactory.getCurrentSession().get(GenericMessage.class, id);
	}

	@Override
	public void sendMessage(String data) {
		this.queue.sendMessage(data);
	}
	
	@Transactional
	@Override
	public void markAsProcessed(InboundMessage inboundMessage){
		inboundMessage.setProcessed(YesNo.Y);
		sessionFactory.getCurrentSession().update(inboundMessage);
		publishSystemAuditLogTraceEvent("MARK_SOURCE_AS_PROCESSED", inboundMessage);
	}
	
	@Transactional
	@Override
	public void resend(OutboundMessage outboundMessage){
		outboundMessage.setIdentifier(null);
		send(outboundMessage);
	}
	
	@Transactional
	@Override
	public void send(OutboundMessage outboundMessage){
		final String text = outboundMessage.getData();
		jmsTemplate.send(outboundMessage.getOutbound(), new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(text);
                return message;
            }
        });
		
		log(outboundMessage);
	}
	
	@Transactional
	@Override
	public void log(OutboundMessage outboundMessage){
		outboundMessage.markAsProcessed();
		save(outboundMessage);
		publishSystemAuditLogTraceEvent("SEND_OUTBOUND_MESSAGE", outboundMessage);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.eventPublisher = applicationContext;
		
	}
	
	@Required
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	private void publishSystemAuditLogTraceEvent(String action, InboundMessage inboundMessage){
		eventPublisher.publishEvent(new SystemAuditLogTraceEvent(this, action, inboundMessage.getClass().getSimpleName(), inboundMessage.getIdentifier().toString(), inboundMessage.toJsonString()));
	}
	
	private void publishSystemAuditLogTraceEvent(String action, OutboundMessage message){
		eventPublisher.publishEvent(new SystemAuditLogTraceEvent(this, action, message.getClass().getSimpleName(), message.getIdentifier().toString(),message.getSourceEntity(), message.getEntityId(), message.toJsonString()));
	}

}
