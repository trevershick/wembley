package com.railinc.wembley.legacy.rules;

import com.railinc.wembley.legacysvc.domain.Subscribers;

public interface SubscriptionRule {

	String ruleId();
	int ruleSequence();
	String ruleType();
	SubscriberRuleCondition condition();
	String conditionArgument();
	SubscriptionRuleAction action();
	String actionArgument();
	
	/**
	 * Executes the rule on the provide collection. The collection
	 * will possibly be modified.
	 * @param subscribers
	 */
	void apply( Subscribers subscribers );
}
