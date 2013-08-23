package com.railinc.wembley.domain;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import org.junit.Test;

import com.railinc.wembley.domain.test.mocks.MockSso;
import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOModuleSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSORoleSubscription;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserSubscription;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionClass;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class MailingListSubscriptionsTest {

	@Test
	public void testExecuteForOneUser() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription sub = createMailinglistSubVo( "Y", "MLID", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUser, "JOEUSER", SubscriptionMode.INCLUSION, "LIST" );
		subs.add( sub );
		
		Subscribers subscribers = subs.execute();

		assertEquals( 1, subscribers.size() );
		assertNotNull( subscribers.byUid( "SSO", "JOEUSER" ) );
	}

	@Test
	public void testExecuteForOneUserExcludingHimself() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription subInc = createMailinglistSubVo( "Y", "MLID1", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUser, "JOEUSER", SubscriptionMode.INCLUSION, "LIST" );

		MailingListSubscription subExc = createMailinglistSubVo( "Y", "MLID2", Delivery.EMAIL, "", "MAILLINGLIST",
                SubscriptionClass.SSOUser, "JOEUSER", SubscriptionMode.EXCLUSION, "LIST" );
		subs.add( subInc );
		subs.add( subExc );
		
		Subscribers subscribers = subs.execute();

		assertEquals( 0, subscribers.size() );
	}

	@Test
	public void testExecuteForMultipleIncludeUsers() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription sub1 = createMailinglistSubVo( "Y", "MLID1", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "LIST" );

		MailingListSubscription sub2 = createMailinglistSubVo( "Y", "MLID2", Delivery.EMAIL, "", "MAILLINGLIST",
                SubscriptionClass.SSOUser, "JOEUSER2", SubscriptionMode.INCLUSION, "LIST" );
		
		subs.add( sub1 );
		subs.add( sub2 );
		
		Subscribers subscribers = subs.execute();

		assertEquals( 2, subscribers.size() );
	}

	@Test
	public void testExecuteForMultipleIncludeUsersDifferentLists() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription sub1 = createMailinglistSubVo( "Y", "MLID1", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "LIST1" );

		MailingListSubscription sub2 = createMailinglistSubVo( "Y", "MLID2", Delivery.EMAIL, "", "MAILLINGLIST",
                SubscriptionClass.SSOUser, "JOEUSER2", SubscriptionMode.INCLUSION, "LIST2" );
		
		subs.add( sub1 );
		subs.add( sub2 );
		
		Subscribers subscribers = subs.execute();

		assertEquals( 2, subscribers.size() );
	}

	@Test
	public void testByMailingListWithOneSub() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription sub = createMailinglistSubVo( "Y", "MLID", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "MLIST" );

		subs.add( sub );
		
		MailingListSubscription msub = subs.byMailingList( "MLID" );

		assertNotNull( msub );
	}

	@Test
	public void testByMailingListWithMultipleSubsOneMatch() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription sub1 = createMailinglistSubVo( "Y", "MLID1", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "MLIST1" );

		MailingListSubscription sub2 = createMailinglistSubVo( "Y", "MLID2", Delivery.EMAIL, "", "MAILLINGLIST",
                SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "MLIST2" );

		subs.add( sub1 );
		subs.add( sub2 );
		
		MailingListSubscription msub = subs.byMailingList( "MLID1" );

		assertNotNull( msub );
	}

	@Test
	public void testByMailingListWithMultipleSubsNoMatch() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription sub1 = createMailinglistSubVo( "Y", "MLID1", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "MLIST1" );

		MailingListSubscription sub2 = createMailinglistSubVo( "Y", "MLID2", Delivery.EMAIL, "", "MAILLINGLIST",
                SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "MLIST2" );

		subs.add( sub1 );
		subs.add( sub2 );
		
		MailingListSubscription msub = subs.byMailingList( "MLID3" );

		assertNull( msub );
	}

	@Test
	public void testByMailingListWithMultipleSubsMultipleMatches() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription sub1 = createMailinglistSubVo( "Y", "MLID1", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "MLIST1" );

		MailingListSubscription sub2 = createMailinglistSubVo( "Y", "MLID1", Delivery.EMAIL, "", "MAILLINGLIST",
                SubscriptionClass.SSOUser, "JOEUSER1", SubscriptionMode.INCLUSION, "MLIST1" );

		subs.add( sub1 );
		subs.add( sub2 );
		
		MailingListSubscription msub = subs.byMailingList( "MLID1" );

		assertNotNull( msub );
	}

	@Test
	public void testExecuteForRoleWithOneUser() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription sub = createMailinglistSubVo( "Y", "MLID", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUsersInRole, "ROLE1", SubscriptionMode.INCLUSION,
				                                              "LIST" );
		subs.add( sub );
		
		Subscribers subscribers = subs.execute();

		assertEquals( 1, subscribers.size() );
		assertNotNull( subscribers.byUid( "SSO", MockSso.MOCK_USER ) );
	}

	@Test
	public void testExecuteForRoleWithOneUserAndExcludeThatUser() {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();
		
		MailingListSubscription subInc = createMailinglistSubVo( "Y", "MLID1", Delivery.EMAIL, "", "MAILLINGLIST",
				                                              SubscriptionClass.SSOUsersInRole, "ROLE1", SubscriptionMode.INCLUSION,
				                                              "LIST" );

		MailingListSubscription subExc = createMailinglistSubVo( "Y", "MLID2", Delivery.EMAIL, "", "MAILLINGLIST",
                SubscriptionClass.SSOUser, MockSso.MOCK_USER, SubscriptionMode.EXCLUSION, "LIST" );

		subs.add( subInc );
		subs.add( subExc );
		
		Subscribers subscribers = subs.execute();

		assertEquals( 0, subscribers.size() );
	}


	public static MailingListSubscription createMailinglistSubVo( String activeFlag,
																  String subId,
													              Delivery delivery,
													              String delMechArg,
													              String listType,
													              SubscriptionClass subClass,
													              String subClassArg,
													              SubscriptionMode subMode,
													              String typeArg ) {

		MailingListSubscription mlSub = null;
		SSO sso = new MockSso( subClass, subClassArg, "mockemail@railinc.com" );
		
		if ( SubscriptionClass.SSOAppUsers.equals( subClass ) ) {			
			mlSub =	new SSOModuleSubscription( sso, subId, subClassArg, delivery, "", subMode, listType );
		}
		else if ( SubscriptionClass.SSOUsersInRole.equals( subClass ) ) {			
			mlSub =	new SSORoleSubscription( sso, subId, subClassArg, delivery, "", subMode, listType );
		}
		else if ( SubscriptionClass.SSOUser.equals( subClass ) ) {	
			mlSub =	new SSOUserSubscription( sso, subId, subClassArg, delivery, "", subMode, listType );
		}
		
		return mlSub;
	}
}
