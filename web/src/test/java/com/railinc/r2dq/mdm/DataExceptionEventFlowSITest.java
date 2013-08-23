package com.railinc.r2dq.mdm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.PollableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.dataexception.event.DataExceptionIgnoredEvent;
import com.railinc.r2dq.dataexception.event.DataExceptionImplementedEvent;
import com.railinc.r2dq.dataexception.event.DataExceptionRejectedEvent;
import com.railinc.r2dq.domain.OutboundMessage;
import com.railinc.r2dq.messages.MessageService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test-r2dq-h2.xml", "/spring-r2dq-hibernate.xml", "/spring-r2dq-si-mdm-integration.xml","/spring-test-r2dq-services.xml","/spring-test-r2dq-si-jms.xml", "/spring-test-r2dq-si-mdm-integration.xml" })
@TransactionConfiguration
@Transactional
public class DataExceptionEventFlowSITest {
	@Autowired
	MessageChannel submitMDMExceptionChannel;
	
	@Autowired
	PollableChannel receiveMDMExceptionChannel;
	
	@Autowired
	PollableChannel receiveMDMExceptionStatusChannel;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	Queue mdmDataExceptionStatusQueue;
	
	@Autowired
	MessageService messageService;
	
	private String queueName = "MDMExceptionStatusQueue";
	
	@Test
	@DirtiesContext
	public void testDataExceptionEventFlow_withImplementedEvent() throws JMSException{
		
		DataExceptionImplementedEvent implementedEvent = new DataExceptionImplementedEvent(this, 123L);
		when(mdmDataExceptionStatusQueue.getQueueName()).thenReturn(queueName);
		applicationContext.publishEvent(implementedEvent);
		Message<?> mdmExceptionStatusJsonString = receiveMDMExceptionStatusChannel.receive();
		assertNotNull(mdmExceptionStatusJsonString.getPayload());
		MDMExceptionStatus exceptionStatus = MDMExceptionStatus.fromJson((String)mdmExceptionStatusJsonString.getPayload());
		assertEquals(MDMExceptionStatusType.ACCEPTED, exceptionStatus.getMdmExceptionStatusType());
		assertEquals(Long.valueOf(123), exceptionStatus.getMDMExceptionId());
		assertNotNull(exceptionStatus.getCreatedDate());
		
		ArgumentCaptor<OutboundMessage> outboundMessageCaptor = ArgumentCaptor.forClass(OutboundMessage.class);
		verify(messageService).log(outboundMessageCaptor.capture());
		assertEquals(queueName, outboundMessageCaptor.getValue().getOutbound());
		assertTrue(outboundMessageCaptor.getValue().getData().contains(MDMExceptionStatusType.ACCEPTED.toString()) && outboundMessageCaptor.getValue().getData().contains("123"));
		
	}
	
	@Test
	@DirtiesContext
	public void testDataExceptionEventFlow_withRejectedEvent() throws JMSException{
		DataExceptionRejectedEvent implementedEvent = new DataExceptionRejectedEvent(this, 125L);
		when(mdmDataExceptionStatusQueue.getQueueName()).thenReturn(queueName);
		applicationContext.publishEvent(implementedEvent);
		Message<?> mdmExceptionStatusJsonString = receiveMDMExceptionStatusChannel.receive();
		assertNotNull(mdmExceptionStatusJsonString.getPayload());
		MDMExceptionStatus exceptionStatus = MDMExceptionStatus.fromJson((String)mdmExceptionStatusJsonString.getPayload());
		assertEquals(MDMExceptionStatusType.REJECTED, exceptionStatus.getMdmExceptionStatusType());
		assertEquals(Long.valueOf(125), exceptionStatus.getMDMExceptionId());
		assertNotNull(exceptionStatus.getCreatedDate());
		
		ArgumentCaptor<OutboundMessage> outboundMessageCaptor = ArgumentCaptor.forClass(OutboundMessage.class);
		verify(messageService).log(outboundMessageCaptor.capture());
		assertEquals(queueName, outboundMessageCaptor.getValue().getOutbound());
		assertTrue(outboundMessageCaptor.getValue().getData().contains(MDMExceptionStatusType.REJECTED.toString()) && outboundMessageCaptor.getValue().getData().contains("125"));
		
	}
	
	@Test
	@DirtiesContext
	public void testDataExceptionEventFlow_withIgnoredEvent() throws JMSException{
		DataExceptionIgnoredEvent implementedEvent = new DataExceptionIgnoredEvent(this, 127L);
		when(mdmDataExceptionStatusQueue.getQueueName()).thenReturn(queueName);
		applicationContext.publishEvent(implementedEvent);
		Message<?> mdmExceptionStatusJsonString = receiveMDMExceptionStatusChannel.receive();
		assertNotNull(mdmExceptionStatusJsonString.getPayload());
		MDMExceptionStatus exceptionStatus = MDMExceptionStatus.fromJson((String)mdmExceptionStatusJsonString.getPayload());
		assertEquals(MDMExceptionStatusType.IGNORED, exceptionStatus.getMdmExceptionStatusType());
		assertEquals(Long.valueOf(127), exceptionStatus.getMDMExceptionId());
		assertNotNull(exceptionStatus.getCreatedDate());
		
		ArgumentCaptor<OutboundMessage> outboundMessageCaptor = ArgumentCaptor.forClass(OutboundMessage.class);
		verify(messageService).log(outboundMessageCaptor.capture());
		assertEquals(queueName, outboundMessageCaptor.getValue().getOutbound());
		assertTrue(outboundMessageCaptor.getValue().getData().contains(MDMExceptionStatusType.IGNORED.toString()) && outboundMessageCaptor.getValue().getData().contains("127"));
	}

}
