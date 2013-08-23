package com.railinc.wembley.rules.impl;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.railinc.wembley.legacy.rules.SubscriberRuleCondition;
import com.railinc.wembley.legacy.rules.impl.MailingListAggregationRule;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.SubscriberBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscribers;

public class MailingListAggregationRuleTest {

	@Test
	public void testApplyToOneSubscriber() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "me@me.com", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "", "blah", Delivery.EMAIL, "me@me.com" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( "me@me.com", ((Subscriber)subscribers.toArray()[0]).deliveryArgument() );
	}

	@org.junit.Test
	public void testApplyToTwoSubscribers() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "me@me.com", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "me@me.com" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( "me@me.com", ((Subscriber)subscribers.toArray()[0]).deliveryArgument() );
	}

	@Test
	public void testApplyToTenSubscribers() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "me@me.com", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB3", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB4", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB5", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB6", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB7", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB8", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB9", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB10", Delivery.EMAIL, "me@me.com" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( "me@me.com", ((Subscriber)subscribers.toArray()[0]).deliveryArgument() );
	}

	@Test
	public void testApplyToTwoSubscribersOneMatchingConditionArgument() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "me@me.com", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "you@you.com" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 2, subscribers.size() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB2" ).deliveryArgument() );
	}

	@Test
	public void testApplyToTwoSubscribersNoneMatchingConditionArgument() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "me@me.com", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "us@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "you@you.com" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 2, subscribers.size() );
		assertEquals( "us@me.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
		assertEquals( "you@you.com", subscribers.byUid( "realm", "SUB2" ).deliveryArgument() );
	}

	@Test
	public void testApplyWithSubscribesThatHaveSameEmailWithDifferentCases() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "me@me.com", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "ME@ME.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "ME@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB3", Delivery.EMAIL, "me@me.com" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		// one of the me@me.com(s) should come back.
		assertEquals( "me@me.com".toUpperCase(), ((Subscriber)subscribers.toArray()[0]).deliveryArgument().toUpperCase() );
	}

	@Test
	public void testApplyNullConditionArgument() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, null, "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB", Delivery.EMAIL, "me@me.com" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( "me@me.com", subscribers.byUid( "realm", "SUB" ).deliveryArgument() );
	}

	@Test
	public void testApplyEmptyConditionArgument() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB", Delivery.EMAIL, "me@me.com" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( "me@me.com", subscribers.byUid( "realm", "SUB" ).deliveryArgument() );
	}

	@Test
	public void testApplyEmptyConditionArgumentAndEmptyEmail() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB", Delivery.EMAIL, "" ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		assertEquals( "", ((Subscriber)subscribers.toArray()[0]).deliveryArgument() );
	}

	
	@Test
	public void testApplyEmptyConditionArgumentAndNullEmail() {
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "", "");
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB", Delivery.EMAIL, null ) );
		
		aggRule.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
		// so, this used to be an empty string ehre, but really, if the processing side
		// gets a subscriber with a null email it should handle it. thus, if it goes
		// thru the aggregation rule, it should still come out null on the other side.
		assertEquals( null, ((Subscriber)subscribers.toArray()[0]).deliveryArgument() );
	}
}
