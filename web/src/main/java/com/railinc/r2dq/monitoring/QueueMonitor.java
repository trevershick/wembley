package com.railinc.r2dq.monitoring;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;

public class QueueMonitor {
	private JmsTemplate jmsTemplate;
	
	public boolean ping(String queueName) {
		try {
			this.jmsTemplate.browse(queueName, new BrowserCallback<Void>() {
				@Override
				public Void doInJms(javax.jms.Session session,
						QueueBrowser browser) throws JMSException {
					Enumeration<?> enumeration = browser.getEnumeration();
					if (enumeration.hasMoreElements()) {
						enumeration.nextElement();
					}
					return null;
				}});
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	@Required
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
