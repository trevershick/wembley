package com.railinc.wembley.legacy.rules.dao;

import com.railinc.wembley.legacy.rules.MailingListSubscriptionRuleBase;
import com.railinc.wembley.legacy.rules.SubscriberRuleCondition;
import com.railinc.wembley.legacy.rules.SubscriptionRuleAction;
import com.railinc.wembley.legacy.rules.impl.MailingListAggregationRule;
import com.railinc.wembley.legacy.rules.impl.MailingListSubstitutionRule;

public class SubscriptionRulesFactory {

	private static final String RULETYPE_MAILINGLIST = "MAILINGLIST";

	public MailingListSubscriptionRuleBase create( String ruleId,
			                                       int ruleSeq,
			                                       String ruleType,
			                                       SubscriberRuleCondition cond,
			                                       String condArg,
			                                       SubscriptionRuleAction action,
			                                       String actionArg) {
		
		
		MailingListSubscriptionRuleBase rule = null;
		
		if ( RULETYPE_MAILINGLIST.equalsIgnoreCase( ruleType ) && SubscriptionRuleAction.SUBSTITUTE.equals( action ) ) {
			rule = new MailingListSubstitutionRule( ruleId, ruleSeq, cond, condArg, actionArg);
		}
		if ( RULETYPE_MAILINGLIST.equalsIgnoreCase( ruleType ) && SubscriptionRuleAction.AGGREGATE.equals( action ) ) {
			rule = new MailingListAggregationRule( ruleId, ruleSeq, cond, condArg, actionArg);
		}
		
		return rule;
	}
}
