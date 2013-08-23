package com.railinc.r2dq.correspondence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.jms.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.core.PollableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"/spring-test-r2dq-si-jms.xml",
		"/spring-test-r2dq-mock-configservice.xml", 
		"/spring-r2dq-velocity.xml",
		"/spring-r2dq-si-correspondence.xml",
		"/spring-test-r2dq-services.xml"})
public class SICorrespondenceFlowTest {

	
	@Autowired
	@Qualifier("mockDcOutboundNotificationServiceQueue")
	PollableChannel notifserv;
	
	
	@Autowired
	CorrespondenceService service;
	
	
	@Autowired
	Queue outboundNotificationServiceQueue;
	
	@Test
	public void test_template() throws Exception {
		TestCorrespondence tc = new TestCorrespondence();
		tc.setFrom(new SimpleContact("Trever SHick", "trever.shick@railinc.com"));
		tc.addRecipient(new SimpleContact("whoever", "whoever@whereever.com"));
		when(outboundNotificationServiceQueue.getQueueName()).thenReturn("AART.NOTIFICATION.SERVICE.QUEUE");
		service.convertAndSend(tc);

		
		String message = (String) this.notifserv.receive(1000).getPayload();
		assertNotNull(message);
		assertTrue(message.contains("This is my subject"));

	}
	
	@Test
	public void test_send_correspondence() throws Exception {
		Correspondence tc = new Correspondence();
		tc.setTextPlain("Wow");
		tc.setSubject("this is a simple subject");
		tc.setFrom(new SimpleContact("Trever SHick", "trever.shick@railinc.com"));
		tc.addRecipient(new SimpleContact("whoever", "whoever@whereever.com"));
		when(outboundNotificationServiceQueue.getQueueName()).thenReturn("AART.NOTIFICATION.SERVICE.QUEUE");
		service.send(tc);

		String message = (String) this.notifserv.receive(1000).getPayload();
		assertNotNull(message);
		
		assertTrue(message.contains("this is a simple subject"));
		
	}

}
