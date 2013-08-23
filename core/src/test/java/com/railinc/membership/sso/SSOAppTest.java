package com.railinc.membership.sso;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.railinc.wembley.legacy.domain.impl.basic.BasicUserSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOModuleSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSORoleSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUser;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserSubscription;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;
public class SSOAppTest {
	private static final String JUNK_UID = "xxxxxxx"; 
	

	public SSO iHateCalls() {
		return new SSOIHateUsage();
	}
	public SSO standardSso() {
		SSOInMemoryImpl inMemoryImpl = new SSOInMemoryImpl();
		inMemoryImpl.add("EHMS","EHMS_APP_USR","sdtxs01","user1","user2","user3");
		inMemoryImpl.add("EHMS","EHMS_APP_ADM","sdtxs01");
		inMemoryImpl.add("EMIS","EMIS_APP_USR","sdtxs01","user1","user2","user3");
		inMemoryImpl.add("EMIS","EHMS_APP_ADM","user1");
		return inMemoryImpl;
	}
	
	@Test
	public void test_basic_inclusion_of_users() {
		MailingListSubscription r = new SSOUserSubscription(iHateCalls(),JUNK_UID,"user1",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r2 = new SSOUserSubscription(iHateCalls(),JUNK_UID,"user2",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r3 = new SSOUserSubscription(iHateCalls(),JUNK_UID,"user3",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(r,r2,r3));
		Set<Subscriber> subscribers = ss.execute();
		assertEquals("Should have 3 users", 3, subscribers.size());
	}
	
	@Test
	public void test_basic_inclusion_of_users_ignore_dup() {
		MailingListSubscription r = new SSOUserSubscription(iHateCalls(),JUNK_UID,"user1",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r2 = new SSOUserSubscription(iHateCalls(),JUNK_UID,"user2",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r3 = new SSOUserSubscription(iHateCalls(),JUNK_UID,"user1",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(r,r2,r3));
		Set<Subscriber> subscribers = ss.execute();
		assertEquals("Shouldn't include user1 twice", 2, subscribers.size());
	}
	
	
	
	
	@Test
	public void test_basic_inclusion_of_roles() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_USR", "sdtxs01");
		sso.add("EHMS", "EHMS_APP_ADM", "sdmxs01");
		
		MailingListSubscription r = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r2 = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(r,r2));
		Set<Subscriber> subscribers = ss.execute();
		assertEquals("Should have 2 users", 2, subscribers.size());
	}
	
	
	@Test
	public void test_basic_inclusion_of_roles_ignore_dups() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_USR", "sdtxs01");
		sso.add("EHMS", "EHMS_APP_ADM", "sdtxs01");
		
		MailingListSubscription r = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r2 = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(r,r2));
		Set<Subscriber> subscribers = ss.execute();
		assertEquals("Should have 1 user only", 1, subscribers.size());
	}
	
	@Test
	public void test_basic_inclusion_of_user_and_roles_ignore_dups() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_USR", "sdtxs01");
		sso.add("EHMS", "EHMS_APP_ADM", "sdtxs01");
		
		MailingListSubscription r = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r2 = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r3 = new SSOUserSubscription(sso,JUNK_UID,"sdtxs01",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(r,r2,r3));
		Set<Subscriber> subscribers = ss.execute();
		assertEquals("Should have 1 user only", 1, subscribers.size());
	}
	
	@Test
	public void test_basic_inclusion_of_basic_user() {
		
		MailingListSubscription r4 = new BasicUserSubscription(UUID.randomUUID().toString(), Delivery.EMAIL,"trever.shick@yahoo.com",SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(r4));
		Set<Subscriber> subscribers = ss.execute();
		List<Subscriber> arrayList = new ArrayList<Subscriber>(subscribers);
		assertEquals("Should have 1 user", 1, subscribers.size());
		assertEquals("trever.shick@yahoo.com", arrayList.get(0).deliveryArgument());
	}
	
	@Test
	public void test_basic_inclusion_of_user_and_roles() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_USR", "sdmxs01");
		sso.add("EHMS", "EHMS_APP_ADM", "sdxxs01");
		
		MailingListSubscription include_EHMS_APP_USR = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription include_EHMS_APP_ADM = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription include_sdtxs01 = new SSOUserSubscription(sso,JUNK_UID,"sdtxs01",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(include_EHMS_APP_USR,include_EHMS_APP_ADM,include_sdtxs01));
		Set<Subscriber> subscribers = ss.execute();
		assertEquals("Should have 3 users", 3, subscribers.size());
	}
	
	@Test
public void test_basic_inclusion_of_app_and_user_and_roles() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_USR", "user1");
		sso.add("EMIS", "EMIS_APP_ADM", "user2");
		
		MailingListSubscription include_EHMS_APP_USR = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINGLISTKEY");
		MailingListSubscription include_EMIS = new SSOModuleSubscription(sso,JUNK_UID,"EMIS",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINGLISTKEY");
		MailingListSubscription include_user3 = new SSOUserSubscription(sso,JUNK_UID,"user3",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINGLISTKEY");
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(include_EHMS_APP_USR,include_EMIS,include_user3));
		Set<Subscriber> subscribers = ss.execute();
		assertEquals("Should have 3 users", 3, subscribers.size());
	}
	
	@Test
	public void test_basic_inclusion_of_app_and_user_and_roles_avoid_dups() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_USR", "user1");
		sso.add("EMIS", "EMIS_APP_ADM", "user1");
		
		MailingListSubscription r = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINGLISTKEY");
		MailingListSubscription r2 = new SSOModuleSubscription(sso,JUNK_UID,"EMIS",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINGLISTKEY");
		MailingListSubscription r3 = new SSOUserSubscription(sso,JUNK_UID,"user1",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINGLISTKEY");
		
		MailingListSubscriptions ss = new MailingListSubscriptions(Arrays.asList(r,r2,r3));
		Set<Subscriber> subscribers = ss.execute();
		assertEquals("Should have 1 user only", 1, subscribers.size());
	}

	
	@Test
	public void testSSOUser_Rules() {

		MailingListSubscription r = new SSOUserSubscription(standardSso(),JUNK_UID,"test",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		Set<Subscriber> members = r.execute();
		
		
		assertEquals(1, members.size());		
		assertTrue(r.matches(new SSOUser(standardSso(),"test")));
		
	}
	
	
	@Test
	public void testSSORole_Rules_forward() {


		MailingListSubscription r = new SSORoleSubscription(standardSso(),JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null, SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		Set<Subscriber> members = r.execute();
		assertEquals(4, members.size());
		
		assertTrue(r.matches(new SSOUser(standardSso(),"sdtxs01")));
	}

	
	
	@Test
	public void test_doesnt_use_sso_when_populated_with_role_backward() {

		MailingListSubscription r = new SSORoleSubscription(iHateCalls(),JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
			
		SSOUser xxxUserNotInSSO = new SSOUser(iHateCalls(),"xxx");
		xxxUserNotInSSO.addSsoRole("EHMS_APP_USR");
		assertTrue(r.matches(xxxUserNotInSSO));
		
	}
	

	@Test
	public void test_simple_role_match_backward_no_sso() {

		MailingListSubscription r = new SSORoleSubscription(iHateCalls(),JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscriptions subs = new MailingListSubscriptions(Arrays.asList(r));
		
		SSOUser theUser = new SSOUser(iHateCalls(),"sdtxs01");
		theUser.addSsoRole("EHMS_APP_USR");
		MailingListSubscription match = subs.match(theUser);
		assertNotNull("Found a match", match);
		assertSame(r,match);
		assertEquals(SubscriptionMode.INCLUSION, match.mode());

		
	}
	@Test
	public void test_simple_role_match_but_excluded_by_user_backward_no_sso() {

		MailingListSubscription r = new SSORoleSubscription(iHateCalls(),JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription r2 = new SSOUserSubscription(iHateCalls(),JUNK_UID,"sdtxs01",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		MailingListSubscriptions subs = new MailingListSubscriptions(Arrays.asList(r,r2));
		
		SSOUser theUser = new SSOUser(iHateCalls(),"sdtxs01");
		theUser.addSsoRole("EHMS_APP_USR");
		MailingListSubscription match = subs.match(theUser);
		assertNotNull("Found a match", match);
		assertSame(r2,match);
		assertEquals(SubscriptionMode.EXCLUSION, match.mode());

		
	}

	@Test
	public void test_Mode_Enum_Doesnt_Change_so_sorting_works() {
		assertEquals(-1, SubscriptionMode.INCLUSION.compareTo(SubscriptionMode.EXCLUSION));
		assertEquals(1, SubscriptionMode.EXCLUSION.compareTo(SubscriptionMode.INCLUSION));
		assertEquals(0, SubscriptionMode.INCLUSION.compareTo(SubscriptionMode.INCLUSION));
	}
	
	@Test
	public void test_doesnt_use_sso_when_populated_with_app_backward() {

		
		SSOModuleSubscription r = new SSOModuleSubscription(iHateCalls(),JUNK_UID,"EHMS",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
			
			
		SSOUser xxxUserNotInSSO = new SSOUser(iHateCalls(),"xxx");
		xxxUserNotInSSO.addSsoModuleId("EHMS");
		assertTrue(r.matches(xxxUserNotInSSO));
		
	}

	
	@Test
	public void test_incl_excl() {
		SSO sso = standardSso();


		SSORoleSubscription EHMS_APP_USR_Include = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		SSORoleSubscription EHMS_APP_USR_Exclude = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		SSORoleSubscription EHMS_APP_ADM_Include = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		SSORoleSubscription EHMS_APP_ADM_Exclude = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		subs.add(EHMS_APP_USR_Include);

		Set<Subscriber> members = subs.execute();
		assertEquals(4, members.size());
		
		
		subs = new MailingListSubscriptions();
		subs.add(EHMS_APP_USR_Include);
		subs.add(EHMS_APP_USR_Exclude);
		members = subs.execute();
		assertEquals(0, members.size());
		
		members.clear();
		subs = new MailingListSubscriptions();
		SSOModuleSubscription EHMS_Include = new SSOModuleSubscription(sso,JUNK_UID,"EHMS",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		SSOModuleSubscription EHMS_Exclude = new SSOModuleSubscription(sso,JUNK_UID,"EHMS",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		subs.add(EHMS_Include);
		subs.add(EHMS_APP_ADM_Exclude);
		members = subs.execute();
		assertEquals(3, members.size());
		

	}
	
	
	@Test
	public void test_incl_excl_with_duplicate_roles() {
		SSO sso = standardSso();
		MailingListSubscription EHMS_APP_USR = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_USR",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscriptions rule = new MailingListSubscriptions();
		rule.add(EHMS_APP_USR);
		rule.add(EHMS_APP_USR);
		Set<Subscriber> members =  rule.execute();
		
		assertEquals(4, members.size());
		
	}
	
	
	/**
	 * if a role is exluded, even with an explicit user subscription, that user
	 * will be removed.  this simply illustrates that role and app level exclusions
	 * must be used carefully
	 */
	@Test
	public void test_implicit_explicit_role_vs_user() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_ADM", "sdtxs01");


		MailingListSubscription INCLUDE_sdtxs01 = new SSOUserSubscription(sso,JUNK_UID,"sdtxs01",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription EXCLUDE_user1 = new SSOUserSubscription(sso,JUNK_UID,"user1",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		MailingListSubscription EXCLUDE_EHMS_APP_ADM = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		
		
		MailingListSubscriptions rule = new MailingListSubscriptions();
		rule.add(EXCLUDE_EHMS_APP_ADM);
		rule.add(INCLUDE_sdtxs01);
		rule.add(EXCLUDE_user1);
		
		
		Subscribers members = rule.execute();
		System.out.println(members);
		assertEquals(0, members.size());
		
	}

	
	@Test
	public void test_implicit_explicit_multiple_inclusions_shoudl_return_explicit_one() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_ADM", "sdtxs01");


		MailingListSubscription INCLUDE_EHMS_APP_ADM = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINGLISTKEY");
		MailingListSubscription INCLUDE_sdtxs01 = new SSOUserSubscription(sso,JUNK_UID,"sdtxs01",Delivery.EMAIL, "trevershick@yahoo.com",SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription INCLUDE_EHMS = new SSOModuleSubscription(sso,JUNK_UID,"EHMS",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINGLISTKEY");
		
		
		MailingListSubscriptions rule = new MailingListSubscriptions();
		rule.add(INCLUDE_EHMS_APP_ADM);
		rule.add(INCLUDE_sdtxs01);
		rule.add(INCLUDE_EHMS);
		
		
		Subscribers members = rule.execute();
		System.out.println(members);
		assertEquals(1, members.size());
		Subscriber byUid = members.byUid("SSO", "sdtxs01");
		assertNotNull(byUid);
		assertEquals("trevershick@yahoo.com", byUid.deliveryArgument());
		
	}

	
	@Test
	public void test_implicit_explicit_role_vs_user_backward_why_am_i_excluded() {
		SSOInMemoryImpl sso = new SSOInMemoryImpl();
		sso.add("EHMS", "EHMS_APP_ADM", "sdtxs01");


		MailingListSubscription INCLUDE_sdtxs01 = new SSOUserSubscription(sso,JUNK_UID,"sdtxs01",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription EXCLUDE_user1 = new SSOUserSubscription(sso,JUNK_UID,"user1",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		MailingListSubscription EXCLUDE_EHMS_APP_ADM = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		
		
		MailingListSubscriptions rule = new MailingListSubscriptions();
		rule.add(EXCLUDE_EHMS_APP_ADM);
		rule.add(INCLUDE_sdtxs01);
		rule.add(EXCLUDE_user1);
		
		SSOUser sdtxs01 = new SSOUser(sso, "sdtxs01");
		MailingListSubscription match = rule.match(sdtxs01);
		assertNotNull(match);
		assertEquals(SubscriptionMode.EXCLUSION, match.mode());
		assertSame(match, EXCLUDE_EHMS_APP_ADM);
		
	}

	@Test
	public void test_implicit_explicit() {
		SSO sso = standardSso();


		MailingListSubscription include_sdtxs01 = new SSOUserSubscription(sso,JUNK_UID,"sdtxs01",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription exclude_user1 = new SSOUserSubscription(sso,JUNK_UID,"user1",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		MailingListSubscription include_EHMS_APP_ADM = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		
		MailingListSubscriptions rule = new MailingListSubscriptions();
		rule.add(include_EHMS_APP_ADM);
		rule.add(include_sdtxs01);
		rule.add(exclude_user1);
		
		
		Subscribers members = rule.execute();
		System.out.println(members);
		assertEquals(1, members.size());
//		assertTrue("Is Explicit", members.iterator().next().attributes().isExplicit());
		assertEquals(Delivery.EMAIL, members.iterator().next().delivery());
		assertNotNull(members.byUid(SSOUser.REALM,"sdtxs01"));
	}
	
	@Test
	public void test_implicit_explicit_prefer_explicit_over_implicit_backward() {
		SSO sso = standardSso();


		MailingListSubscription include_sdtxs01 = new SSOUserSubscription(sso,JUNK_UID,"sdtxs01",Delivery.EMAIL,"trever.shick@railinc.com",SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription include_EHMS_APP_ADM = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		
		MailingListSubscriptions rule = new MailingListSubscriptions();
		rule.add(include_EHMS_APP_ADM);
		rule.add(include_sdtxs01);
		
		SSOUser user = new SSOUser(iHateCalls(),"sdtxs01");
		user.addSsoRole("EHMS_APP_ADM");
		
		MailingListSubscription match = rule.match(user);
		assertSame(match, include_sdtxs01);

		
		// switch the order of the lsit and try again
		rule = new MailingListSubscriptions();
		rule.add(include_sdtxs01);
		rule.add(include_EHMS_APP_ADM);
		
		user = new SSOUser(iHateCalls(),"sdtxs01");
		user.addSsoRole("EHMS_APP_ADM");
		
		match = rule.match(user);
		assertSame(match, include_sdtxs01);
		
	}

	@Test
	public void test_implicit_explicit_with_delivery_arg() {
		SSO sso = standardSso();


		MailingListSubscription include_sdtxs01 = new SSOUserSubscription(sso,JUNK_UID,"sdtxs01",Delivery.EMAIL,"trever.shick@railinc.com",SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		MailingListSubscription exclude_user1 = new SSOUserSubscription(sso,JUNK_UID,"user1",Delivery.EMAIL,null,SubscriptionMode.EXCLUSION,"MAILINGLISTKEY");
		MailingListSubscription include_EHMS_APP_ADM = new SSORoleSubscription(sso,JUNK_UID,"EHMS_APP_ADM",Delivery.EMAIL,null,SubscriptionMode.INCLUSION,"MAILINSTLISTKEY");
		
		
		MailingListSubscriptions rule = new MailingListSubscriptions();
		rule.add(include_EHMS_APP_ADM);
		rule.add(include_sdtxs01);
		rule.add(exclude_user1);
		
		
		Subscribers members = rule.execute();
		System.out.println(members);
		assertEquals(1, members.size());
//		assertTrue("Is Explicit", members.iterator().next().attributes().isExplicit());
//		assertSame(include_sdtxs01, members.iterator().next().attributes().getRule());
		assertEquals(Delivery.EMAIL, members.iterator().next().delivery());
		assertNotNull(members.byUid(SSOUser.REALM,"sdtxs01"));
		assertEquals("trever.shick@railinc.com", members.byUid(SSOUser.REALM,"sdtxs01").deliveryArgument());
	}
}
