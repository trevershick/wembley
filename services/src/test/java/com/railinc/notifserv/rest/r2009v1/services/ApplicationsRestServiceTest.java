package com.railinc.notifserv.rest.r2009v1.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.easymock.EasyMock;

import junit.framework.TestCase;

import com.railinc.notifserv.restsvcs.r2009v1.Application;
import com.railinc.notifserv.restsvcs.r2009v1.Applications;
import com.railinc.wembley.legacy.service.NotificationServiceAdmin;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class ApplicationsRestServiceTest extends TestCase {

	private PrintStream err;
	private PrintStream out;

	public void setUp() {
		err = System.err;
		out = System.out;
		System.setErr(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		}));
		System.setOut(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		}));
	}
	public void tearDown() {
		System.setErr(err);
		System.setOut(out);
	}
	
	public void testGetAllApplications() {
		NotificationServiceAdmin mock = EasyMock.createMock(NotificationServiceAdmin.class);
		
		ApplicationsRestService s = new ApplicationsRestService();
		s.setService(mock);
		
		List<ApplicationVo> l = new ArrayList<ApplicationVo>();
		l.addAll(Arrays.asList(createApplicationVo("EHMS")));
		EasyMock.expect(mock.getAllApplications()).andReturn(l);
		EasyMock.expect(mock.getAllApplications()).andThrow(new RuntimeException("test"));
		EasyMock.replay(mock);
		
		Applications allApplications = s.getAllApplications();
		assertEquals(1, allApplications.getApplications().size());
		assertEquals("EHMS", allApplications.getApplications().get(0).getAppId());
		
		try {
			s.getAllApplications();
		} catch (WebApplicationException wae) {
			assertEquals(500, wae.getResponse().getStatus());
		}

	}

	
	public void testGetApplication() {
		NotificationServiceAdmin mock = EasyMock.createMock(NotificationServiceAdmin.class);
		
		ApplicationsRestService s = new ApplicationsRestService();
		s.setService(mock);
		
		EasyMock.expect(mock.getApplication("EHMS")).andReturn(createApplicationVo("EHMS"));
		EasyMock.expect(mock.getApplication("EMIS")).andReturn(null);
		EasyMock.expect(mock.getApplication("XXX")).andThrow(new RuntimeException("test"));
		EasyMock.replay(mock);
		
		Application application = s.getApplication("EHMS");
		assertNotNull(application);
		assertEquals("EHMS", application.getAppId());

		try {
			application = s.getApplication("EMIS");
		} catch (WebApplicationException wae) {
			assertEquals(404, wae.getResponse().getStatus());
		}
		
		try {
			application = s.getApplication("XXX");
		} catch (WebApplicationException wae) {
			assertEquals(500, wae.getResponse().getStatus());
		}
	}

	private ApplicationVo createApplicationVo(String appId) {
		ApplicationVo a = new ApplicationVo();
		a.setAppId(appId);
		a.setCreatedTimestamp(new Date());
		a.setDefaultDeliveryTiming("1");
		return a;
		
	}
	

	/*public void testGetEvents() {
		EventVo eventVo = new EventVo();
		EventVo eventVo2 = new EventVo();
		NotificationServiceAdmin mock = EasyMock.createMock(NotificationServiceAdmin.class);
		
		ApplicationsRestService s = new ApplicationsRestService();
		s.setService(mock);
		
		List<ApplicationVo> l = new ArrayList<ApplicationVo>();
		l.addAll(Arrays.asList(createApplicationVo("EHMS")));
		EasyMock.expect(mock.getEventsByCorrelationId("EHMS", "abcd")).andReturn(Arrays.asList(eventVo,eventVo2));
		EasyMock.expect(mock.getEventsByCorrelationId("EHMS", "abcd")).andThrow(new RuntimeException("test"));
		EasyMock.replay(mock);
		
		Events events = s.getEvents("EHMS", "abcd");
		assertEquals(2, events.getEvents().size());

		
		try {
			s.getEvents("EHMS", "abcd");
		} catch (WebApplicationException wae) {
			assertEquals(500, wae.getResponse().getStatus());
			assertTrue(wae.getMessage().contains("test"));
		}

		
	}*/

	
	
//	public void test_getLatestEventByCorrelationId() {
//		EventVo eventVo = new EventVo();
//		NotificationServiceAdmin mock = EasyMock.createMock(NotificationServiceAdmin.class);
//		
//		ApplicationsRestService s = new ApplicationsRestService();
//		s.setService(mock);
//		
//		EasyMock.expect(mock.getLatestEventByCorrelationId("EHMS", "abcd")).andReturn(eventVo);
//		EasyMock.expect(mock.getLatestEventByCorrelationId("EHMS", "abcd")).andReturn(null);
//		EasyMock.expect(mock.getLatestEventByCorrelationId("EHMS", "abcd")).andThrow(new RuntimeException("test"));
//		EasyMock.replay(mock);
//		
//		Event e = s.getLatestEventByCorrelationId("EHMS", "abcd");
//		assertNotNull(e);
//
//		
//		try {
//			s.getLatestEventByCorrelationId("EHMS", "abcd");
//		} catch (WebApplicationException wae) {
//			assertEquals(404, wae.getResponse().getStatus());
//		}
//		try {
//			s.getLatestEventByCorrelationId("EHMS", "abcd");
//		} catch (WebApplicationException wae) {
//			assertEquals(500, wae.getResponse().getStatus());
//			assertTrue(wae.getMessage().contains("test"));
//		}
//	}

	public void test_user_service_when_not_set() {
		
		ApplicationsRestService s = new ApplicationsRestService();
		s.setService(null);
		
		

		
		try {
			s.getAllApplications();
		} catch (WebApplicationException wae) {
			assertEquals(500, wae.getResponse().getStatus());
		}
	}

}
