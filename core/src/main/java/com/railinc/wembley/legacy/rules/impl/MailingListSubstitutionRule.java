package com.railinc.wembley.legacy.rules.impl;

import org.apache.commons.lang.StringUtils;

import com.railinc.wembley.legacy.rules.MailingListSubscriptionRuleBase;
import com.railinc.wembley.legacy.rules.SubscriberRuleCondition;
import com.railinc.wembley.legacy.rules.SubscriptionRuleAction;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.SubscriberBaseImpl;
import com.railinc.wembley.legacysvc.domain.Subscribers;

public class MailingListSubstitutionRule extends MailingListSubscriptionRuleBase {

	public MailingListSubstitutionRule( String ruleId,
			                           int ruleSeq,
			                           SubscriberRuleCondition cond,
			                           String condArg,
			                           String actionArg) {
		
		super( ruleId, ruleSeq, "MAILINGLIST", cond, condArg, SubscriptionRuleAction.SUBSTITUTE, actionArg);
	}
	
	@Override
	public void apply( Subscribers subscribers ) {
		
		for ( Subscriber subscriber : subscribers ) {
			replaceSubscriberValue( subscriber );
		}
	}
	
	protected void replaceSubscriberValue( Subscriber subscriber ) {
		
		SubscriberBaseImpl subBase = (SubscriberBaseImpl)subscriber;

		if ( SubscriberRuleCondition.DELIVERY_ARGUMENT.equals( condition() ) ) {
			if ( StringUtils.isEmpty( conditionArgument() ) ||
				 StringUtils.indexOf( StringUtils.lowerCase( subscriber.deliveryArgument() ), StringUtils.lowerCase( conditionArgument() ) ) >= 0 ) {
				subBase.setDeliveryArgument( actionArgument() );
			}
		}
		else if ( SubscriberRuleCondition.DELIVERY.equals( condition() ) ) {
			if ( StringUtils.isEmpty( conditionArgument() ) ||
				 StringUtils.indexOf( subscriber.delivery().name(), conditionArgument() ) >= 0 ) {
				subBase.setDelivery( Delivery.valueOf( actionArgument() ) );
			}			
		}
	}
}
