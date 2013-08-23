package com.railinc.r2dq.monitoring;

import javax.jms.JMSException;
import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Required;

import com.railinc.serviceability.monitoring.ComponentStatus;
import com.railinc.serviceability.monitoring.MonitoredComponent;
import com.railinc.serviceability.servlet.REMServlet;

/**
 * This monitored component, when in the spring context will be found by the {@link REMServlet} and 
 * browse given queue for status.
 * 
 * @see REMServlet
 * @author viswa mothukuri
 *
 */
public class MQConnectionFactoryMonitoredComponent implements MonitoredComponent {
	private String name;
	private Queue queueName;
	private QueueMonitor queueMonitor;
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public ComponentStatus check() {
		try {
			String outbound = ((Queue)queueName).getQueueName();
			outbound = outbound.replace("queue:///", "");
			boolean status =queueMonitor.ping(outbound);
			
			if(!status){
				return ComponentStatus.error(getName() + " unable to browse queue '" +queueName.getQueueName() + "'");
			}
			
		} catch (JMSException e) {
			return ComponentStatus.error(getName() + "In valid Queue Name");
		}
		return ComponentStatus.ok();
	}
	
	@Required
	public void setName(String name) {
		this.name = name;
	}
	
	@Required
	public void setQueueName(Queue queueName) {
		this.queueName = queueName;
	}
	
	@Required
	public void setQueueMonitor(QueueMonitor queueMonitor) {
		this.queueMonitor = queueMonitor;
	}

}

