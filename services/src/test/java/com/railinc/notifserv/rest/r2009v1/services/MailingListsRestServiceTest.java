package com.railinc.notifserv.rest.r2009v1.services;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import junit.framework.TestCase;

import com.railinc.notifserv.restsvcs.r2009v1.MailingList;
import com.railinc.notifserv.restsvcs.r2009v1.MailingLists;
import com.railinc.notifserv.restsvcs.r2009v1.Subscribers;
import com.railinc.notifserv.restsvcs.r2009v1.UserSubscriber;
import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUser;
import com.railinc.wembley.legacy.service.MailingListsService;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;
import com.railinc.wembley.legacysvc.domain.Subscriber;

public class MailingListsRestServiceTest extends TestCase {

	private MailingListsRestService restService;
	private MailingListsService service;
	private MailingListVo mailingListVo;
	private MailingListsVo mailingListsVo;
	private MailingListVo mailingListVo2;
	private MailingListVo mailingListVo3;
	private PrintStream err;
	private PrintStream out;

	
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
		restService = new MailingListsRestService();
		service = createMock(MailingListsService.class);
		restService.setService(service);

		mailingListVo = new MailingListVo();
		mailingListVo.setActive(true);
		mailingListVo.setApplication("MYAPP");
		mailingListVo.setDescription("my desc");
		mailingListVo.setFromAddress("trever.shick@railinc.com");
		mailingListVo.setKey("MYAPPOUT");
		mailingListVo.setShortName("Short Name");
		mailingListVo.setTitle("My Title");
		mailingListVo.setType("Outage");

		mailingListVo2 = new MailingListVo();
		mailingListVo2.setActive(true);
		mailingListVo2.setApplication("MYAPP2");
		mailingListVo2.setDescription("my desc2");
		mailingListVo2.setFromAddress("trever.shick2@railinc.com");
		mailingListVo2.setKey("MYAPPOUT2");
		mailingListVo2.setShortName("Short Name2");
		mailingListVo2.setTitle("My Title2");
		mailingListVo2.setType("Outage");

		mailingListVo3 = new MailingListVo();
		mailingListVo3.setActive(true);
		mailingListVo3.setApplication("MYAPP3");
		mailingListVo3.setDescription("my desc3");
		mailingListVo3.setFromAddress("trever.shick3@railinc.com");
		mailingListVo3.setKey("MYAPPOUT3");
		mailingListVo3.setShortName("Short Name3");
		mailingListVo3.setTitle("My Title3");
		mailingListVo3.setType("Outage");

		mailingListsVo = new MailingListsVo();
		mailingListsVo.add(mailingListVo);
		mailingListsVo.add(mailingListVo2);
		mailingListsVo.add(mailingListVo3);
	}

	public void testGetAllMailingLists() {
		expect(service.allMailingLists()).andReturn(this.mailingListsVo);
		replay(service);

		MailingLists allml = restService.getAllMailingLists();
		MailingList mailingList = allml.getMailingLists().get(0);
		assertEquivalence(mailingList, mailingListVo);
	}

	public void testGetAllMailingLists_ERR() {
		expect(service.allMailingLists())
				.andThrow(new RuntimeException("oops"));
		replay(service);

		try {
			restService.getAllMailingLists();
			fail("Should have thrown a WAE");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, wae
					.getResponse().getStatus());
		}
	}

	public void testGetUnsubscribed() {
		MailingListsVo notSubscribedTo = new MailingListsVo();
		notSubscribedTo.add(mailingListVo3);

		expect(service.getMailingListsNotSubscribedTo("SSO", "sdtxs01"))
				.andReturn(notSubscribedTo);
		replay(service);

		MailingLists allml = restService.getUnsubscribed("SSO", "sdtxs01");
		MailingList mailingList = allml.getMailingLists().get(0);
		assertEquivalence(mailingList, mailingListVo3);
	}

	public void testGetUnsubscribed_ERR() {
		expect(service.getMailingListsNotSubscribedTo("SSO", "sdtxs01"))
				.andThrow(new RuntimeException("oops"));
		replay(service);

		try {
			restService.getUnsubscribed("SSO", "sdtxs01");
			fail("Should have thrown a WAE");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, wae
					.getResponse().getStatus());
		}
	}

	public void testGetMailingListSubscribers() {
		com.railinc.wembley.legacysvc.domain.Subscribers subscribers = new com.railinc.wembley.legacysvc.domain.Subscribers();
		SSO sso = createMock(SSO.class);
		Subscriber s = new SSOUser(sso, "sdtxs01", Delivery.EMAIL,
				"trever.shick@railinc.com");
		Subscriber s2 = new SSOUser(sso, "xxxxxxx", Delivery.EMAIL,
				"xx@railinc.com");
		subscribers.add(s);
		subscribers.add(s2);

		expect(service.getMailingList("TEST")).andReturn(new MailingListVo());
		expect(service.getSubscribersToMailingList("TEST")).andReturn(subscribers);
		replay(service);
		
		Subscribers mls = restService.getSubscribers("TEST");
		assertEquals(subscribers.size(), mls.getSubscribers().size());

		com.railinc.notifserv.restsvcs.r2009v1.Subscriber s1 = mls.getSubscribers().get(1);
		UserSubscriber ust = (UserSubscriber) s1;
		assertEquals(s.uid(), ust.getUid());
	}

	public void testGetMailingListSubscribers_ERR() {


		expect(service.getMailingList("TEST")).andReturn(this.mailingListVo);
		expect(service.getSubscribersToMailingList("TEST")).andThrow(new RuntimeException("oops"));
		replay(service);

		try {
			restService.getSubscribers("TEST");
			fail("Should throw a 500 error");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, wae
					.getResponse().getStatus());
		}
	}

	public void testGetMailingList() {
		expect(service.getMailingList("TEST")).andReturn(mailingListVo2);
		replay(service);

		MailingList mailingList = restService.getMailingList("TEST");
		assertEquivalence(mailingList, mailingListVo2);

	}

	public void testGetMailingList_404() {
		expect(service.getMailingList("NONEX")).andReturn(null);
		replay(service);

		try {
			restService.getMailingList("NONEX");
			fail("Should be throwing a 404");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_NOT_FOUND, wae.getResponse()
					.getStatus());
		}

	}

	public void testUnsubcribe() {
		service.unsubscribeMe("SSO", "sdtxs01", "MAILINGLISTID");
		replay(service);

		Response unsubcribe = restService.unsubcribe("MAILINGLISTID", "SSO",
				"sdtxs01");
		assertEquals(HttpServletResponse.SC_OK, unsubcribe.getStatus());
	}

	public void testUnsubcribe_ERR() {
		service.unsubscribeMe("SSO", "sdtxs01", "MAILINGLISTID");
		expectLastCall().andThrow(new RuntimeException("oops"));
		replay(service);

		try {
			restService.unsubcribe("MAILINGLISTID", "SSO", "sdtxs01");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, wae
					.getResponse().getStatus());
		}
	}

	public void testSubcribe() {
		service.subscribeMe("SSO", "sdtxs01", "MAILINGLISTID");
		replay(service);

		Response r = restService.subcribe("MAILINGLISTID", "SSO", "sdtxs01");
		assertEquals(HttpServletResponse.SC_CREATED, r.getStatus());
	}

	public void testSubcribe_ERR() {
		service.subscribeMe("SSO", "sdtxs01", "MAILINGLISTID");
		expectLastCall().andThrow(new RuntimeException("oops"));
		replay(service);

		try {
			restService.subcribe("MAILINGLISTID", "SSO", "sdtxs01");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, wae
					.getResponse().getStatus());
		}
	}

	public void testGetMailingListsService() {
		restService.setService(null);
		assertNull(restService.getService());

		try {
			restService.require("mailingListService", restService
					.getService());
			fail("Should throw an exception");
		} catch (Exception e) {
			// good
		}

		restService.setService(service);
		assertSame(service, restService.getService());
	}

	private void assertEquivalence(MailingList ml, MailingListVo v) {
		assertEquals(v.getApplication(), ml.getApplication());
		assertEquals(v.getDescription(), ml.getDescription());
		assertEquals(v.getFromAddress(), ml.getFromAddress());
		assertEquals(v.getKey(), ml.getKey());
		assertEquals(v.getTitle(), ml.getTitle());
		assertEquals(v.getShortName(), ml.getShortName());
		assertEquals(v.isActive(), ml.isActive().booleanValue());
		assertEquals(v.getType(), ml.getType());

	}
}
