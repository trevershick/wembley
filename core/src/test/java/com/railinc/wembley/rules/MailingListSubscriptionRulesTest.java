package com.railinc.wembley.rules;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.railinc.wembley.legacy.rules.MailingListSubscriptionRuleBase;
import com.railinc.wembley.legacy.rules.MailingListSubscriptionRules;
import com.railinc.wembley.legacy.rules.SubscriberRuleCondition;
import com.railinc.wembley.legacy.rules.impl.MailingListAggregationRule;
import com.railinc.wembley.legacy.rules.impl.MailingListSubstitutionRule;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.SubscriberBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscribers;

public class MailingListSubscriptionRulesTest {

	@Test
	public void testApplyWithOneRuleOneSubscriber() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "TESTRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "me@me.com", "");
		
		List<MailingListSubscriptionRuleBase> ruleList = new ArrayList<MailingListSubscriptionRuleBase>();
		ruleList.add( aggRule );
		
		MailingListSubscriptionRules rules = new MailingListSubscriptionRules( ruleList );
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "", "blah", Delivery.EMAIL, "me@me.com" ) );

		rules.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
	}

	@Test
	public void testApplyWithTwoDifferentRulesAndOneSubscriber() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "AGGRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "you@you.com", "");

		MailingListSubstitutionRule subRule = 
			new MailingListSubstitutionRule( "SUBRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com");

		List<MailingListSubscriptionRuleBase> ruleList = new ArrayList<MailingListSubscriptionRuleBase>();
		ruleList.add( subRule );
		ruleList.add( aggRule );
		
		MailingListSubscriptionRules rules = new MailingListSubscriptionRules( ruleList );
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "", "blah", Delivery.EMAIL, "me@me.com" ) );

		rules.apply( subscribers );
		
		assertEquals( 1, subscribers.size() );
	}

	@Test
	public void testApplyWithTwoDifferentRulesAndMultipleSubscribers() {
		
		MailingListAggregationRule aggRule =
			new MailingListAggregationRule( "AGGRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "you@you.com", "" );

		MailingListSubstitutionRule subRule = 
			new MailingListSubstitutionRule( "SUBRULE", 1, SubscriberRuleCondition.DELIVERY_ARGUMENT, "@me.com", "you@you.com" );

		List<MailingListSubscriptionRuleBase> ruleList = new ArrayList<MailingListSubscriptionRuleBase>();
		ruleList.add( subRule );
		ruleList.add( aggRule );
		
		MailingListSubscriptionRules rules = new MailingListSubscriptionRules( ruleList );
		
		Subscribers subscribers = new Subscribers();
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB1", Delivery.EMAIL, "us@us.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB2", Delivery.EMAIL, "me@we.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB3", Delivery.EMAIL, "me@me.com" ) );
		subscribers.add( new SubscriberBaseImpl( "realm", "SUB4", Delivery.EMAIL, "me@me.com" ) );

		rules.apply( subscribers );
		
		assertEquals( 3, subscribers.size() );
		assertEquals( "us@us.com", subscribers.byUid( "realm", "SUB1" ).deliveryArgument() );
		assertEquals( "me@we.com", subscribers.byUid( "realm", "SUB2" ).deliveryArgument() );
	}
}
