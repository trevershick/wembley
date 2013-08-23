package com.railinc.r2dq.log;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessagingException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.SystemAuditLog;
import com.railinc.r2dq.domain.SystemAuditLog.AuditLogType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test-r2dq-h2.xml", "/spring-r2dq-hibernate.xml", "/spring-r2dq-si-audit-log.xml", "/spring-test-r2dq-services.xml" })
public class SystemAuditLogEventFlowSITest {
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	SystemAuditLogService systemAuditLogService;
	
	@Autowired
	MessageChannel handleExceptionsChannel;
	
	@Test
	@DirtiesContext
	public void testSystemAuditLogTraceEventFlow_withoutSourceEntity() throws InterruptedException{
		SystemAuditLogEvent systemAuditLogEvent = new SystemAuditLogTraceEvent(this, "TEST", "Entity", "123", "{userId:itvxm01}");
		applicationContext.publishEvent(systemAuditLogEvent );
		Thread.sleep(1100);
		ArgumentCaptor<SystemAuditLog> auditLogCaptor = ArgumentCaptor.forClass(SystemAuditLog.class);
		verify(systemAuditLogService).save(auditLogCaptor.capture());
		assertEquals(AuditLogType.TRACE, auditLogCaptor.getValue().getType());
		assertEquals("TEST", auditLogCaptor.getValue().getAction());
		assertEquals("Entity", auditLogCaptor.getValue().getEntityName());
		assertEquals("123", auditLogCaptor.getValue().getEntityId());
		assertEquals("{userId:itvxm01}", auditLogCaptor.getValue().getDetails());
		
	}
	
	@Test
	@DirtiesContext
	public void testSystemAuditLogTraceEventFlow_withSourceEntity() throws InterruptedException{
		SystemAuditLogEvent systemAuditLogEvent = new SystemAuditLogTraceEvent(this, "TEST", "Entity", "123","sourceEntity", "test@railinc.com", "{userId:itvxm01}");
		applicationContext.publishEvent(systemAuditLogEvent );
		Thread.sleep(1100);
		ArgumentCaptor<SystemAuditLog> auditLogCaptor = ArgumentCaptor.forClass(SystemAuditLog.class);
		verify(systemAuditLogService).save(auditLogCaptor.capture());
		assertEquals(AuditLogType.TRACE, auditLogCaptor.getValue().getType());
		assertEquals("TEST", auditLogCaptor.getValue().getAction());
		assertEquals("Entity", auditLogCaptor.getValue().getEntityName());
		assertEquals("123", auditLogCaptor.getValue().getEntityId());
		assertEquals("sourceEntity", auditLogCaptor.getValue().getSourceEntityName());
		assertEquals("test@railinc.com", auditLogCaptor.getValue().getSourceEntityId());
		assertEquals("{userId:itvxm01}", auditLogCaptor.getValue().getDetails());
		
	}
	
	@Test
	@DirtiesContext
	public void testSystemAuditLogErrorEventFlow_withoutFlowName_doExceptSystemAuditLogActionAsNONE_AVAILABLE() throws InterruptedException{
		InboundMessage rawInboundMessage = new InboundMessage();
		Message<InboundMessage> message = MessageBuilder.withPayload(rawInboundMessage).setHeader("FLOW_ENTITY", "ERROR_LOG").setHeader("FLOW_ENTITY_ID", "3").build(); 
		MessagingException messagingException = new MessagingException(message);
		handleExceptionsChannel.send(MessageBuilder.withPayload(messagingException).build());
		Thread.sleep(1100);
		ArgumentCaptor<SystemAuditLog> auditLogCaptor = ArgumentCaptor.forClass(SystemAuditLog.class);
		verify(systemAuditLogService).save(auditLogCaptor.capture());
		assertEquals(AuditLogType.ERROR, auditLogCaptor.getValue().getType());
		assertEquals("NONE_AVAILABLE", auditLogCaptor.getValue().getAction());
		assertEquals("ERROR_LOG", auditLogCaptor.getValue().getEntityName());
		assertEquals("3", auditLogCaptor.getValue().getEntityId());
	}
	
	@Test
	@DirtiesContext
	public void testSystemAuditLogErrorEventFlow_withFlowName() throws InterruptedException{
		InboundMessage rawInboundMessage = new InboundMessage();
		Message<InboundMessage> message = MessageBuilder.withPayload(rawInboundMessage).setHeader("FLOW_NAME", "ERROR_TRACE").setHeader("FLOW_ENTITY", "ERROR_LOG").setHeader("FLOW_ENTITY_ID", "3").build(); 
		MessagingException messagingException = new MessagingException(message);
		handleExceptionsChannel.send(MessageBuilder.withPayload(messagingException).build());
		Thread.sleep(1100);
		ArgumentCaptor<SystemAuditLog> auditLogCaptor = ArgumentCaptor.forClass(SystemAuditLog.class);
		verify(systemAuditLogService).save(auditLogCaptor.capture());
		assertEquals(AuditLogType.ERROR, auditLogCaptor.getValue().getType());
		assertEquals("ERROR_TRACE", auditLogCaptor.getValue().getAction());
		assertEquals("ERROR_LOG", auditLogCaptor.getValue().getEntityName());
		assertEquals("3", auditLogCaptor.getValue().getEntityId());
	}

}
