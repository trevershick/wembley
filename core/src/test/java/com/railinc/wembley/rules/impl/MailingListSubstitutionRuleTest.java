package com.railinc.wembley.rules.impl;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.railinc.wembley.legacy.rules.SubscriberRuleCondition;
import com.railinc.wembley.legacy.rules.SubscriptionRuleAction;
import com.railinc.wembley.legacy.rules.impl.MailingListSubstitutionRule;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.SubscriberBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscribers;

public class MailingListSubstitutionRuleTest {

	@org.junit.Test
	public void testApplyWithOneSubscriber() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB", Delivery.EMAIL, "me@me.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB" ).deliveryArgument() );
		
		assertEquals( "you@you.com", subRule.actionArgument() );
		assertEquals( SubscriptionRuleAction.SUBSTITUTE, subRule.action() );
		assertEquals( SubscriberRuleCondition.DELIVERY_ARGUMENT, subRule.condition() );
		assertEquals( "TESTRULE", subRule.ruleId());
		assertEquals( "MAILINGLIST", subRule.ruleType() );
		assertEquals( 1, subRule.ruleSequence() );
	}

	@Test
	public void testApplyWithOneSubscriberWithNullEmail() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB", Delivery.EMAIL, null ) );

		subRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( null, subscribers.byUid( "realm", "SUB" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithTwoSubscribersAndSameEmail() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "me@me.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 2, subscribers.size() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB2" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithTwoSubscribersAndSameEmailDomain() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "you@me.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 2, subscribers.size() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB2" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithTwoSubscribersAndDifferentEmails() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "we@we.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 2, subscribers.size() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
		assertEquals( "we@we.com", subscribers.byUid( "realm", "SUB2" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithTwoSubscribersAndDifferentEmailDomains() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "me@we.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 2, subscribers.size() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
		assertEquals( "me@we.com", subscribers.byUid( "realm", "SUB2" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithTwoSubscribersAndSameEmailDifferentCase() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "ME@ME.COM" ) );

		subRule.apply( subscribers );
		
		assertEquals( 2, subscribers.size() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB2" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithDeliveryCondition() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY, "EMAIL", "RSS");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( Delivery.RSS, subscribers.byUid( "realm", "SUB1" ).delivery() );
		assertEquals( "me@me.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithDeliveryConditionEmptyConditionArg() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY, "", "RSS");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( Delivery.RSS, subscribers.byUid( "realm", "SUB1" ).delivery() );
		assertEquals( "me@me.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithDeliveryConditionEmptyConditionArgMultipleSubscribers() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY, "", "RSS");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", null, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB3", Delivery.RSS, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB4", Delivery.EMAIL, "me@me.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 4, subscribers.size() );
		assertEquals( Delivery.RSS, subscribers.byUid( "realm", "SUB1" ).delivery() );
		assertEquals( Delivery.RSS, subscribers.byUid( "realm", "SUB2" ).delivery() );
		assertEquals( Delivery.RSS, subscribers.byUid( "realm", "SUB3" ).delivery() );
		assertEquals( Delivery.RSS, subscribers.byUid( "realm", "SUB4" ).delivery() );
	}

	@Test
	public void testApplyWithFullEmailMatch() {
		
		MailingListSubstitutionRule subRule =
			new MailingListSubstitutionRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "me@me.com", "you@you.com");

		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );

		subRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
	}
}
