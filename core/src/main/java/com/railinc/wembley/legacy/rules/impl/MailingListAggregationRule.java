package com.railinc.wembley.legacy.rules.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.railinc.wembley.legacy.rules.MailingListSubscriptionRuleBase;
import com.railinc.wembley.legacy.rules.SubscriberRuleCondition;
import com.railinc.wembley.legacy.rules.SubscriptionRuleAction;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;

public class MailingListAggregationRule extends MailingListSubscriptionRuleBase {

	public MailingListAggregationRule( String ruleId,
			                           int ruleSeq,
			                           SubscriberRuleCondition cond,
			                           String condArg,
			                           String actionArg) {
		
		super( ruleId, ruleSeq, "MAILINGLIST", cond, condArg, SubscriptionRuleAction.AGGREGATE, actionArg);
	}
	
	@Override
	public void apply(Subscribers subscribers) {
		Set<Subscriber> unmodifiableSet = new HashSet<Subscriber>(subscribers);
		Iterator<Subscriber> iterator = unmodifiableSet.iterator();
		
		SortedSet<String> alreadySeen = new TreeSet<String>();
		while (iterator.hasNext()) {
			Subscriber next = iterator.next();
			String deliveryAndAddress = next.delivery().toString() + "/" + next.deliveryArgument();
			deliveryAndAddress = deliveryAndAddress.toUpperCase();
			
			if (alreadySeen.contains(deliveryAndAddress)) {
				subscribers.remove(next);
			} else {
				alreadySeen.add(deliveryAndAddress);
			}
		}
//		
//		
//		
//		
//		
//		Iterator<Subscriber> subIter = subscribers.iterator();
//		Delivery delivery = null;
//		
//		while ( subIter.hasNext() ) {
//			
//			Subscriber subscriber = subIter.next();
//
//			if ( SubscriberRuleCondition.DELIVERY_ARGUMENT.equals( condition() ) ) {
//
//				if ( ( StringUtils.isEmpty( conditionArgument() ) && StringUtils.isEmpty( subscriber.deliveryArgument() ) ) ||
//					 ( StringUtils.equalsIgnoreCase( subscriber.deliveryArgument(), conditionArgument() ) ) ) {
//
//					subIter.remove();
//					delivery = subscriber.delivery(); 
//				}
//			}
//		}
//		
//		if ( delivery != null ) {
//			
//			SubscriberBaseImpl aggSub = new SubscriberBaseImpl( "", "", delivery, conditionArgument() );
//			subscribers.add( aggSub );
//		}
	}
}
