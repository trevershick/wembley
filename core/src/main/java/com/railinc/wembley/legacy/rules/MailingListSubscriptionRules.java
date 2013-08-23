package com.railinc.wembley.legacy.rules;

import java.util.ArrayList;
import java.util.List;

import com.railinc.wembley.legacysvc.domain.Subscribers;

public class MailingListSubscriptionRules extends ArrayList<MailingListSubscriptionRuleBase> implements SubscriptionRules {

	private static final long serialVersionUID = 8188647595540592997L;

	public MailingListSubscriptionRules( List<? extends MailingListSubscriptionRuleBase> ruleList ) {
		super( ruleList );
	}

	public void apply( Subscribers subscribers ) {
		
		for ( MailingListSubscriptionRuleBase rule : this ) {
			rule.apply( subscribers );
		}
	}

	public List<SubscriptionRule> getRulesForPhases( List<SubscriptionRulePhases> phases ) {
		return null;
	}
}
