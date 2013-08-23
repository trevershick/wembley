package com.railinc.r2dq.mdm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import javax.jms.JMSException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test-r2dq-services.xml", "/spring-test-r2dq-h2.xml", "/spring-r2dq-hibernate.xml", "/spring-r2dq-si-mdm-integration.xml", "/spring-test-r2dq-si-mdm-integration.xml", "/spring-test-r2dq-services.xml","/spring-test-r2dq-si-jms.xml" })
@TransactionConfiguration
@Transactional
public class SaveMDMExceptionStatusFlowSITest {
	
	@Autowired
	MessageChannel mdmExceptionStatusChannel;
	
	@Autowired
	MDMExceptionService mdmExceptionService;
	
	@Test
	public void testSaveMDMExceptionStatus() throws JMSException{
		MDMExceptionStatus status = new MDMExceptionStatus();
		status.setMDMExceptionId(123L);
		status.setMdmExceptionStatusType(MDMExceptionStatusType.ACCEPTED);
		String mdmExceptionStatusJsonString = status.toJson();
		mdmExceptionStatusChannel.send(MessageBuilder.withPayload(mdmExceptionStatusJsonString).build());
		ArgumentCaptor<MDMExceptionStatus> statusCaptor = ArgumentCaptor.forClass(MDMExceptionStatus.class);
		verify(mdmExceptionService).saveMDMExceptionStatus(statusCaptor.capture());
		assertEquals(status.getMDMExceptionId(), statusCaptor.getValue().getMDMExceptionId());
		assertEquals(status.getMdmExceptionStatusType(), statusCaptor.getValue().getMdmExceptionStatusType());
	}

}
