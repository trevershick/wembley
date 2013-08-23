package com.railinc.r2dq.mdm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.OutboundMessage;
import com.railinc.r2dq.integration.msg.InboundMDMExceptionMessage;
import com.railinc.r2dq.messages.MessageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test-r2dq-h2.xml", "/spring-r2dq-hibernate.xml", "/spring-r2dq-si-mdm-integration.xml", "/spring-test-r2dq-si-mdm-integration.xml","/spring-test-r2dq-services.xml","/spring-test-r2dq-si-jms.xml" })
@TransactionConfiguration
@Transactional
public class SubmitMDMExceptionFlowSITest {
	
	@Autowired
	MessageChannel submitMDMExceptionChannel;
	
	@Autowired
	PollableChannel receiveMDMExceptionChannel;
	
	@Autowired
	PollableChannel receiveMDMExceptionStatusChannel;
	
	@Autowired
	Queue mdmDataExceptionStatusQueue;
	
	@Autowired
	MessageService messageService;
	
	
	@Test
	public void testGenerateDataExceptionFlow() throws JMSException{
		String queueName = "MDMExceptionStatusQueue";
		InboundMDMExceptionMessage mdmException = new InboundMDMExceptionMessage();
		mdmException.setCode(123L);
		mdmException.setMdmExceptionId(1L);
		when(mdmDataExceptionStatusQueue.getQueueName()).thenReturn(queueName);
		submitMDMExceptionChannel.send(MessageBuilder.withPayload(mdmException).build());
		Message<?> mdmExceptionJsonString = receiveMDMExceptionChannel.receive();
		assertEquals(mdmException.toJsonString(), mdmExceptionJsonString.getPayload());
		
		Message<?> mdmExceptionUpdateStatusJsonString = receiveMDMExceptionStatusChannel.receive();
		MDMExceptionStatus status = MDMExceptionStatus.fromJson((String)mdmExceptionUpdateStatusJsonString.getPayload());
		assertEquals(status.getMDMExceptionId(), mdmException.getMdmExceptionId());
		assertEquals(status.getMdmExceptionStatusType(), MDMExceptionStatusType.PULLED);
		
		ArgumentCaptor<OutboundMessage> outboundMessageCaptor = ArgumentCaptor.forClass(OutboundMessage.class);
		verify(messageService).log(outboundMessageCaptor.capture());
		assertEquals(queueName, outboundMessageCaptor.getValue().getOutbound());
		assertTrue(outboundMessageCaptor.getValue().getData().contains(MDMExceptionStatusType.PULLED.toString()) && outboundMessageCaptor.getValue().getData().contains("1"));
		
	}

}
