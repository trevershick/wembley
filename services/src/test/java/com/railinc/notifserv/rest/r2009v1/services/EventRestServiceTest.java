package com.railinc.notifserv.rest.r2009v1.services;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import junit.framework.TestCase;

import com.railinc.notifserv.restsvcs.r2009v1.Event;
import com.railinc.wembley.legacy.service.NotificationServiceAdmin;
import com.railinc.wembley.legacysvc.domain.EventVo;

public class EventRestServiceTest extends TestCase {

	private EventsRestService restService;
	private NotificationServiceAdmin service;
	private PrintStream err;
	private PrintStream out;
	private EventVo eventVo;
	private Date ts;
	

	
	public void setUpQuiet() {
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
	
	
	public void setUp() {
		setUpQuiet();
		restService = new EventsRestService();
		service = createMock(NotificationServiceAdmin.class);
		restService.setService(service);
		
		eventVo = new EventVo();
		eventVo.setContents("eventcontents".getBytes());
		eventVo.setEventUid("abcd");
		eventVo.setRetryCount(2);
		eventVo.setState("CLOSED");
		ts = new Date();
		eventVo.setStateTimestamp(ts);
	}

	public void testGetEvent() {
		expect(service.getEvent("abcd")).andReturn(eventVo);
		replay(service);

		Event event = restService.getEvent("abcd");
		assertEquivalence(event, eventVo);
	}

	public void testGetEvent_ERR() {
		expect(service.getEvent("abcd")).andThrow(new RuntimeException("oops"));
		replay(service);

		try {
			restService.getEvent("abcd");
			fail("Should have thrown a WAE");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, wae
					.getResponse().getStatus());
		}
	}

	public void testGetEvent_404() {
		expect(service.getEvent("abcd")).andReturn(null);
		replay(service);

		try {
			restService.getEvent("abcd");
			fail("Should have thrown a WAE");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_NOT_FOUND, wae
					.getResponse().getStatus());
		}
	}
	private void assertEquivalence(Event ml, EventVo v) {
		assertEquals(v.getEventUid(), ml.getUid());
		assertEquals(v.getRetryCount(), ml.getRetryCount());
		assertEquals(v.getState(), ml.getState());
		assertEquals(v.getStateTimestamp(), ml.getStateChanged().getTime());
	}
}
