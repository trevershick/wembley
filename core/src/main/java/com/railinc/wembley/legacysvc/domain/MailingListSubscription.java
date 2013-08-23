package com.railinc.wembley.legacysvc.domain;

import java.util.Date;



public interface MailingListSubscription {
	String key();
	
	String subscriptionType();
	String subscriptionDetails();
	String description();
	Date lastModified();
	/**
	 * Wheteher a subscription si explicit or not is dependent on whether or
	 * not the subscription points directly to the user and only that user
	 * If it's done thru expansino of a 'group' then it's not explicit
	 * @return
	 */
	boolean explicit();
	Delivery delivery();
	String deliveryArgument();
	String getMailingListKey();
	/**
	 * Executes on the set provided. Will modify the set
	 * @param members
	 */
	void execute(Subscribers members);
	
	/**
	 * returns the results as a new set
	 * @return
	 */
	Subscribers execute();
	/**
	 * Does this subscfription match the subscriber?
	 * @param member
	 * @return
	 */
	boolean matches(Subscriber subscriber);
	/**
	 * 
	 * @return the mode (Inclusion/Exclusion) that this subscription operates in
	 */
	SubscriptionMode mode();
}
