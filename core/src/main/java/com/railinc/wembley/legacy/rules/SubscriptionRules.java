package com.railinc.wembley.legacy.rules;

import java.util.List;

import com.railinc.wembley.legacysvc.domain.Subscribers;

public interface SubscriptionRules {

	/**
	 * Executes the rule on the provide collection. The collection
	 * will possibly be modified.
	 * @param subscribers
	 */
	void apply( Subscribers subscribers );
	
	List<SubscriptionRule> getRulesForPhases( List<SubscriptionRulePhases> phases );
}
