package com.railinc.r2dq.dataexception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.Queue;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.InboundSource;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.OutboundMessage;
import com.railinc.r2dq.domain.Responsibility;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.domain.tasks.ExceptionRemediationTask;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.implementation.ImplementationService;
import com.railinc.r2dq.messages.MessageService;
import com.railinc.r2dq.responsibility.ResponsibilityService;
import com.railinc.r2dq.sourcesystem.SourceSystemService;
import com.railinc.r2dq.task.TaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test-r2dq-services.xml","/spring-test-r2dq-h2.xml", "/spring-r2dq-hibernate.xml", "/spring-r2dq-si-create-data-exception.xml", "/spring-test-r2dq-si-audit-log.xml","/spring-test-r2dq-services.xml","/spring-test-r2dq-si-jms.xml" })
@TransactionConfiguration
@Transactional
public class CreateDataExceptionFlowSITest {
	@Autowired
	MessageChannel channelRawInboundMessageSink;
	
	@Autowired
	SourceSystemService sourceSystemService;
	
	@Autowired
	ResponsibilityService responsibilityService;
	
	@Autowired
	DataExceptionService dataExceptionService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	PollableChannel receiveExceptionsChannel;
	
	@Autowired
	ImplementationService implementationService;
	
	@Autowired
	Queue mdmDataExceptionStatusQueue;
	
	@Autowired
	MessageService messageService;
	
	
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionFlow_withManualImplementationType_doSaveDataExceptionAndCreateTask() throws InterruptedException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();
		Responsibility responsibility = mock(Responsibility.class);
		Identity identity = mock(Identity.class);
		Task task = new ExceptionRemediationTask();
		when(responsibilityService.getResponsibility(any(DataException.class))).thenReturn(responsibility);
		when(responsibility.getResponsiblePerson()).thenReturn(identity);
		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);
		when(implementationService.getImplementationType(any(DataException.class))).thenReturn(ImplementationType.Manual);
		when(taskService.createTaskFor(any(DataException.class))).thenReturn(task);

		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(2000);
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(responsibilityService, times(1)).getResponsibility(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).save(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		assertEquals(Long.valueOf(1034), dataExceptionCapture.getValue().getRuleNumber());
		assertEquals(ImplementationType.Manual, dataExceptionCapture.getValue().getImplementationType());
		assertEquals("mdmdesc1", dataExceptionCapture.getValue().getDescription());
		assertEquals("mdmattrvalue", dataExceptionCapture.getValue().getMdmAttributevalue());
		assertEquals("mdmobjtype", dataExceptionCapture.getValue().getMdmObjectType());
		assertEquals("keycol1", dataExceptionCapture.getValue().getSourceSystemKeyColumn());
		assertEquals("srcinfo1", dataExceptionCapture.getValue().getSourceSystemObjectData());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).markSourceAsProcessed(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		
		verify(taskService, never()).save(task);
		
	}
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionFlow_withAutomatedImplementationType_doSaveDataExceptionAndCreateTask() throws InterruptedException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();
		Responsibility responsibility = mock(Responsibility.class);
		Identity identity = mock(Identity.class);
		Task task = new ExceptionRemediationTask();
		when(responsibilityService.getResponsibility(any(DataException.class))).thenReturn(responsibility);
		when(responsibility.getResponsiblePerson()).thenReturn(identity);
		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		sourceSystem.setOutboundQueue("outboundQueue");
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);
		when(implementationService.getImplementationType(any(DataException.class))).thenReturn(ImplementationType.Automated);
		when(taskService.createTaskFor(any(DataException.class))).thenReturn(task);

		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(2000);
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(responsibilityService, times(1)).getResponsibility(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).save(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		assertEquals(Long.valueOf(1034), dataExceptionCapture.getValue().getRuleNumber());
		assertEquals(ImplementationType.Automated, dataExceptionCapture.getValue().getImplementationType());
		assertEquals("mdmdesc1", dataExceptionCapture.getValue().getDescription());
		assertEquals("mdmattrvalue", dataExceptionCapture.getValue().getMdmAttributevalue());
		assertEquals("mdmobjtype", dataExceptionCapture.getValue().getMdmObjectType());
		assertEquals("keycol1", dataExceptionCapture.getValue().getSourceSystemKeyColumn());
		assertEquals("srcinfo1", dataExceptionCapture.getValue().getSourceSystemObjectData());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).markSourceAsProcessed(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		
		verify(taskService, never()).save(task);
		
	}
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionFlow_withStormDrainImplementationType_doNotSaveDataExceptionAndNotCreateTask() throws InterruptedException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();
		Responsibility responsibility = mock(Responsibility.class);
		Identity identity = mock(Identity.class);
		Task task = new ExceptionRemediationTask();
		when(responsibilityService.getResponsibility(any(DataException.class))).thenReturn(responsibility);
		when(responsibility.getResponsiblePerson()).thenReturn(identity);
		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		sourceSystem.setOutboundQueue("outboundQueue");
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);
		when(implementationService.getImplementationType(any(DataException.class))).thenReturn(ImplementationType.StormDrain);
		when(taskService.createTaskFor(any(DataException.class))).thenReturn(task);

		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(1000);
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(responsibilityService, times(1)).getResponsibility(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, never()).save(any(DataException.class));
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).markSourceAsProcessed(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		
		verify(taskService, never()).save(task);
		
	}
	
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionFlow_withForceStormDrainImplementationType_doNotSaveDataExceptionAndNotCreateTask() throws InterruptedException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();
		Responsibility responsibility = mock(Responsibility.class);
		Identity identity = mock(Identity.class);
		Task task = new ExceptionRemediationTask();
		when(responsibilityService.getResponsibility(any(DataException.class))).thenReturn(responsibility);
		when(responsibility.getResponsiblePerson()).thenReturn(identity);
		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		sourceSystem.setOutboundQueue("outboundQueue");
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);
		when(implementationService.getImplementationType(any(DataException.class))).thenReturn(ImplementationType.ForceStormDrain);
		when(taskService.createTaskFor(any(DataException.class))).thenReturn(task);

		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(1000);
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(responsibilityService, times(1)).getResponsibility(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		verify(dataExceptionService, never()).save(any(DataException.class));
		verify(dataExceptionService, never()).markSourceAsProcessed(dataExceptionCapture.capture());
		
		/*dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).markSourceAsProcessed(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());*/
		
		verify(taskService, never()).save(task);
		
	}
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionFlow_withPassthroughImplementationType_doSaveDataExceptionAndDontCreateTask() throws InterruptedException, JMSException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();
		Responsibility responsibility = mock(Responsibility.class);
		Identity identity = new Identity(IdentityType.SsoId, "itvxm01");
		Task task = new ExceptionRemediationTask();
		String queueName = "AART.R2DQ.SOURCESYSTEM.DATAEXCEPTON.INBOUND";
		
		when(responsibilityService.getResponsibility(any(DataException.class))).thenReturn(responsibility);
		when(responsibility.getResponsiblePerson()).thenReturn(identity);
		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		sourceSystem.setOutboundQueue(queueName);
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);
		when(implementationService.getImplementationType(any(DataException.class))).thenReturn(ImplementationType.PassThrough);
		when(taskService.createTaskFor(any(DataException.class))).thenReturn(task);
		doNothing().when(messageService).send(any(OutboundMessage.class));
		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(1000);
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(responsibilityService, times(1)).getResponsibility(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).save(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		assertEquals(Long.valueOf(1034), dataExceptionCapture.getValue().getRuleNumber());
		assertEquals(ImplementationType.PassThrough, dataExceptionCapture.getValue().getImplementationType());
		assertEquals("mdmdesc1", dataExceptionCapture.getValue().getDescription());
		assertEquals("mdmattrvalue", dataExceptionCapture.getValue().getMdmAttributevalue());
		assertEquals("mdmobjtype", dataExceptionCapture.getValue().getMdmObjectType());
		assertEquals("keycol1", dataExceptionCapture.getValue().getSourceSystemKeyColumn());
		assertEquals("srcinfo1", dataExceptionCapture.getValue().getSourceSystemObjectData());
		
		ArgumentCaptor<OutboundMessage> outboundMessageCatpor = ArgumentCaptor.forClass(OutboundMessage.class);
		verify(messageService).send(outboundMessageCatpor.capture());
		assertEquals(dataExceptionCapture.getValue().toJsonString(), outboundMessageCatpor.getValue().getData());
		assertEquals(queueName, outboundMessageCatpor.getValue().getOutbound());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).markSourceAsProcessed(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		assertEquals(ImplementationType.PassThrough, dataExceptionCapture.getValue().getImplementationType());
		
		verify(taskService, never()).save(task);
		
	}
	
	
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionChain_withRawInboundToDataExceptionTransformerThrowsAnException_doExceptFailedToCreateDataException() throws InterruptedException{
		InboundMessage inboundMessage = new InboundMessage();
		inboundMessage.setSource(InboundSource.MDMException);
		inboundMessage.setIdentifier(123L);
		inboundMessage.setData("{created:\"2013-06-03 13:09\",sourcesystem:\"fur\",sourcekeyvalue:\"keyval1\",sourcevalue:\"srcvalue1\",sourceinfo:\"srcinfo1\",sourcerecordid:\"srcid1\","+
				"code:1034,description:\"mdmdesc1\",type:\"mdmobjtype\",attr:\"mdmattrtype\",value:\"mdmattrvalue\"}");
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();
		
		//given
		when(sourceSystemService.getOrCreate("fur")).thenReturn(new SourceSystem());//Throw(new NullPointerException("Unable to find sourceSystem with 'fur'"));
		
		//when
		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(1000);
		
		//then
		Message<?> errorMessage = receiveExceptionsChannel.receive();
		verifyFailedCause((MessagingException)errorMessage.getPayload(), "Unable to find sourceSystem with 'fur'", inboundMessage);
				
		verify(responsibilityService, never()).getResponsibility(any(DataException.class));
		verify(dataExceptionService, never()).save(any(DataException.class));
		verify(dataExceptionService, never()).markSourceAsProcessed(any(DataException.class));
		verify(taskService, never()).save(any(Task.class));
		
	}
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionChain_withUpdateImplementationTypeThrowsAnException_doExpectFailedToCreateDataException() throws InterruptedException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();

		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);
		when(implementationService.getImplementationType(any(DataException.class))).thenThrow(new NullPointerException("Unable to updateImplementationType information"));
		when(responsibilityService.getResponsibility(any(DataException.class))).thenThrow(new NullPointerException("Unable to updateResponsibilityPerson information"));
		
		
		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(2000);
		
		verify(sourceSystemService, times(1)).getOrCreate("fur");
		
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(implementationService).getImplementationType(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		Message<?> errorMessage = receiveExceptionsChannel.receive();
		verifyFailedCause((MessagingException)errorMessage.getPayload(), "Unable to updateImplementationType information", dataExceptionCapture.getValue());
		
		verify(responsibilityService, never()).getResponsibility(any(DataException.class));
		verify(dataExceptionService, never()).save(any(DataException.class));
		verify(dataExceptionService, never()).markSourceAsProcessed(any(DataException.class));
		verify(taskService, never()).save(any(Task.class));
		
	}
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionChain_withUpdateResponsibilityPersonThrowsAnException_doExpectFailedToCreateDataException() throws InterruptedException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();

		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);
		when(implementationService.getImplementationType(any(DataException.class))).thenReturn(ImplementationType.Manual);
		when(responsibilityService.getResponsibility(any(DataException.class))).thenThrow(new NullPointerException("Unable to updateResponsibilityPerson information"));
		
		
		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(2000);
		
		verify(sourceSystemService, times(1)).getOrCreate("fur");
		
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(responsibilityService, times(1)).getResponsibility(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		Message<?> errorMessage = receiveExceptionsChannel.receive();
		verifyFailedCause((MessagingException)errorMessage.getPayload(), "Unable to updateResponsibilityPerson information", dataExceptionCapture.getValue());
		
		verify(dataExceptionService, never()).save(any(DataException.class));
		verify(dataExceptionService, never()).markSourceAsProcessed(any(DataException.class));
		verify(taskService, never()).save(any(Task.class));
		
	}
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionChain_withSaveDataExceptionThrowsAnException_doExpectFailedToCreateDataException() throws InterruptedException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();
		Responsibility responsibility = mock(Responsibility.class);
		Identity identity = new Identity(IdentityType.LocalGroup, "23");

		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);

		doThrow(new ConstraintViolationException("Failed to save Data Exception", new SQLException("Column 'MDM_VALUE' cannot be null"), "")).when(dataExceptionService).save(any(DataException.class));
		when(implementationService.getImplementationType(any(DataException.class))).thenReturn(ImplementationType.Manual);
		when(responsibilityService.getResponsibility(any(DataException.class))).thenReturn(responsibility);
		when(responsibility.getResponsiblePerson()).thenReturn(identity);
		
		
		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(1000);
		
		verify(sourceSystemService, times(1)).getOrCreate("fur");
		
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(responsibilityService, times(1)).getResponsibility(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).save(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		assertEquals(Long.valueOf(1034), dataExceptionCapture.getValue().getRuleNumber());
		assertEquals("mdmdesc1", dataExceptionCapture.getValue().getDescription());
		assertEquals("mdmattrvalue", dataExceptionCapture.getValue().getMdmAttributevalue());
		assertEquals("mdmobjtype", dataExceptionCapture.getValue().getMdmObjectType());
		assertEquals("keycol1", dataExceptionCapture.getValue().getSourceSystemKeyColumn());
		assertEquals("srcinfo1", dataExceptionCapture.getValue().getSourceSystemObjectData());
		
		Message<?> errorMessage = receiveExceptionsChannel.receive();
		verifyFailedCause((MessagingException)errorMessage.getPayload(), "Failed to save Data Exception", dataExceptionCapture.getValue());
		
		verify(dataExceptionService, never()).markSourceAsProcessed(any(DataException.class));
		verify(taskService, never()).save(any(Task.class));
		
	}
	
	@Test
	@DirtiesContext
	public void testCreateDataExceptionChain_withMarkAsProcessedThrowsAnException_doExpectFailedToCreateDataException() throws InterruptedException{
		InboundMessage inboundMessage = buildRawInboundMessage();
		Message<?> rawInboundMessage = MessageBuilder.withPayload(inboundMessage).build();
		Responsibility responsibility = mock(Responsibility.class);
		Identity identity = new Identity(IdentityType.LocalGroup, "23");

		SourceSystem sourceSystem= new SourceSystem("fur", "Find");
		when(sourceSystemService.getOrCreate("fur")).thenReturn(sourceSystem);
		when(implementationService.getImplementationType(any(DataException.class))).thenReturn(ImplementationType.Manual);
		when(responsibilityService.getResponsibility(any(DataException.class))).thenReturn(responsibility);
		when(responsibility.getResponsiblePerson()).thenReturn(identity);
		
		doThrow(new NullPointerException("Falied to mark source as processed")).when(dataExceptionService).markSourceAsProcessed(any(DataException.class));
		
		channelRawInboundMessageSink.send(rawInboundMessage);
		
		Thread.sleep(1000);
		
		verify(sourceSystemService, times(1)).getOrCreate("fur");
		
		
		ArgumentCaptor<DataException> dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(responsibilityService, times(1)).getResponsibility(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).save(dataExceptionCapture.capture());
		assertEquals(inboundMessage, dataExceptionCapture.getValue().getSource());
		assertEquals(sourceSystem, dataExceptionCapture.getValue().getSourceSystem());
		assertEquals(identity, dataExceptionCapture.getValue().getResponsiblePerson());
		assertEquals(Long.valueOf(1034), dataExceptionCapture.getValue().getRuleNumber());
		assertEquals("mdmdesc1", dataExceptionCapture.getValue().getDescription());
		assertEquals("mdmattrvalue", dataExceptionCapture.getValue().getMdmAttributevalue());
		assertEquals("mdmobjtype", dataExceptionCapture.getValue().getMdmObjectType());
		assertEquals("keycol1", dataExceptionCapture.getValue().getSourceSystemKeyColumn());
		assertEquals("srcinfo1", dataExceptionCapture.getValue().getSourceSystemObjectData());
		
		dataExceptionCapture = ArgumentCaptor.forClass(DataException.class);
		verify(dataExceptionService, times(1)).markSourceAsProcessed(dataExceptionCapture.capture());
		
		Message<?> errorMessage = receiveExceptionsChannel.receive();
		verifyFailedCause((MessagingException)errorMessage.getPayload(), "Falied to mark source as processed", dataExceptionCapture.getValue());
		
		verify(taskService, never()).save(any(Task.class));
		
	}

	private InboundMessage buildRawInboundMessage() {
		InboundMessage inboundMessage = new InboundMessage();
		inboundMessage.setSource(InboundSource.MDMException);
		inboundMessage.setIdentifier(123L);
		inboundMessage.setData("{created:\"2013-06-03 13:09\",sourcesystem:\"fur\",sourcekeycol:\"keycol1\",sourcekeyvalue:\"keyval1\",sourcevalue:\"srcvalue1\",sourceinfo:\"srcinfo1\",sourcerecordid:\"srcid1\","+
				"code:1034,description:\"mdmdesc1\",type:\"mdmobjtype\",attr:\"mdmattrtype\",value:\"mdmattrvalue\"}");
		return inboundMessage;
	}
	
	private void verifyFailedCause(MessagingException messagingException, String causeMessage, Object payload){
		if(messagingException.getCause().getMessage()!=null){
			assertTrue(messagingException.getCause().getMessage().contains(causeMessage));
		}
		assertEquals("CREATE_DATA_EXCEPTION", messagingException.getFailedMessage().getHeaders().get("FLOW_NAME"));
		assertEquals("InboundMessage", messagingException.getFailedMessage().getHeaders().get("FLOW_ENTITY"));
		assertNotNull(messagingException.getFailedMessage().getHeaders().get("FLOW_ENTITY_ID"));
		assertEquals(payload, messagingException.getFailedMessage().getPayload());
	}
	
}
