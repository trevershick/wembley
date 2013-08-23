package com.railinc.wembley.legacysvc.domain;

import java.util.Comparator;

public final class SubscriptionModeComparator implements Comparator<MailingListSubscription> {

	public static final SubscriptionModeComparator INCLUSIONS_FIRST = new SubscriptionModeComparator(1);
	public static final SubscriptionModeComparator EXCLUSIONS_FIRST = new SubscriptionModeComparator(-1);
	private int multiplier;
	
	private SubscriptionModeComparator(int multiplier) {
		this.multiplier = multiplier;
	}
	
	public int compare(MailingListSubscription arg0, MailingListSubscription arg1) {
		return multiplier * arg0.mode().compareTo(arg1.mode());
	}

}
