package com.railinc.wembley.legacy.rules;

import com.railinc.wembley.legacysvc.domain.Subscribers;

public abstract class MailingListSubscriptionRuleBase implements SubscriptionRule {

	private SubscriptionRuleAction action;
	private String actionArgument;
	private SubscriberRuleCondition condition;
	private String conditionArgument;
	private String ruleId;
	private int ruleSequence;
	private String ruleType;
	
	public abstract void apply(Subscribers subscribers);

	public MailingListSubscriptionRuleBase( String ruleId,
            								int ruleSeq,
            								String ruleType,
            								SubscriberRuleCondition cond,
            								String condArg,
            								SubscriptionRuleAction action,
            								String actionArg) {
		
		this.ruleId = ruleId;
		this.ruleSequence = ruleSeq;
		this.ruleType = ruleType;
		this.condition = cond;
		this.conditionArgument = condArg;
		this.action = action;
		this.actionArgument = actionArg;
	}
	
	public SubscriptionRuleAction action() {
		return action;
	}

	public String actionArgument() {
		return actionArgument;
	}

	public SubscriberRuleCondition condition() {
		return condition;
	}

	public String conditionArgument() {
		return conditionArgument;
	}

	public String ruleId() {
		return ruleId;
	}

	public int ruleSequence() {
		return ruleSequence;
	}

	public String ruleType() {
		return ruleType;
	}	
}
