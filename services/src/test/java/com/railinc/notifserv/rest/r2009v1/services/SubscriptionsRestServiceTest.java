package com.railinc.notifserv.rest.r2009v1.services;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import org.easymock.EasyMock;

import junit.framework.TestCase;

import com.railinc.notifserv.restsvcs.r2009v1.Subscription;
import com.railinc.notifserv.restsvcs.r2009v1.Subscriptions;
import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserSubscription;
import com.railinc.wembley.legacy.service.MailingListsService;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class SubscriptionsRestServiceTest extends TestCase {

	private SubscriptionsRestService restService;
	private MailingListsService service;
	private PrintStream err;
	private PrintStream out;
	private MailingListVo mailingListVo;
	private MailingListsVo mailingListsVo;
	private MailingListVo mailingListVo2;
	private MailingListVo mailingListVo3;


	private MailingListSubscriptions mailingListSubscriptions;
	private SSOUserSubscription userSubscription;
	private Date lastModified;
	

	
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
		restService = new SubscriptionsRestService();
		service = EasyMock.createMock(MailingListsService.class);
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
		
		mailingListSubscriptions = new MailingListSubscriptions();
		userSubscription = new SSOUserSubscription(EasyMock.createMock(SSO.class),"THEID","sdtxs01",com.railinc.wembley.legacysvc.domain.Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MYAPPOUT3" );
		lastModified = new java.sql.Date(System.currentTimeMillis());
		userSubscription.setLastModified(lastModified);
		mailingListSubscriptions.add(userSubscription);
	}

	
	
	
	
	
	
	
	
	public void test_mySubscriptions() {
		
		EasyMock.expect(service.getMySubscriptions("SSO", "sdtxs01")).andReturn(mailingListSubscriptions);
		EasyMock.expect(service.allMailingLists()).andReturn(mailingListsVo);
		EasyMock.replay(service);

		Subscriptions mySubscriptions = restService.mySubscriptions("SSO","sdtxs01");
		assertEquals(1, mySubscriptions.getSubscriptions().size());
		Subscription subscription = mySubscriptions.getSubscriptions().get(0);
		assertEquivalence(this.userSubscription, subscription);

	}
	
	
	public void test_mySubscriptions_500() {
		
		EasyMock.expect(service.getMySubscriptions("SSO", "sdtxs01")).andThrow(new RuntimeException("oops"));
		EasyMock.replay(service);

		try {
			restService.mySubscriptions("SSO","sdtxs01");
			fail("Should be throwing wae");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, wae.getResponse().getStatus());
		}
		
	}
	public void test_mySubscriptions_BAD_REQUEST() {
		

		try {
			restService.mySubscriptions(null,"sdtxs01");
			fail("Should be throwing wae");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_BAD_REQUEST, wae.getResponse().getStatus());
		}
		try {
			restService.mySubscriptions("SSO",null);
			fail("Should be throwing wae");
		} catch (WebApplicationException wae) {
			assertEquals(HttpServletResponse.SC_BAD_REQUEST, wae.getResponse().getStatus());
		}
		
	}
	
	
	private void assertEquivalence(SSOUserSubscription expected,
			Subscription actual) {
		
		assertEquals(expected.lastModified(), actual.getSubscriptionDate().getTime());
		assertEquals(expected.delivery().name(), actual.getDelivery().getMedia());
		assertEquals(expected.getMailingListKey(), actual.getMailingList().getKey());
		assertEquals(expected.subscriptionType(), actual.getType());
		assertEquals(expected.subscriptionDetails(),actual.getTypeArgument());
	}
	
	

//	public void testGetSubscription_404() {
//		expect(service.getSubscription("abcd")).andReturn(null);
//		replay(service);
//
//		try {
//			restService.getSubscription("abcd");
//			fail("Should have thrown a WAE");
//		} catch (WebApplicationException wae) {
//			assertEquals(HttpServletResponse.SC_NOT_FOUND, wae
//					.getResponse().getStatus());
//		}
//	}
	
}
