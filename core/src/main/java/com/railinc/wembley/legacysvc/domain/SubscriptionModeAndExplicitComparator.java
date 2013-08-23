package com.railinc.wembley.legacysvc.domain;

import java.util.Comparator;

public final class SubscriptionModeAndExplicitComparator implements Comparator<MailingListSubscription> {

	public static final SubscriptionModeAndExplicitComparator INCLUSIONS_IMPLICIT_FIRST = new SubscriptionModeAndExplicitComparator(1,1);
	public static final SubscriptionModeAndExplicitComparator INCLUSIONS_EXPLICIT_FIRST = new SubscriptionModeAndExplicitComparator(1,-1);
	public static final SubscriptionModeAndExplicitComparator EXCLUSIONS_IMPLICIT_FIRST = new SubscriptionModeAndExplicitComparator(-1,1);
	public static final SubscriptionModeAndExplicitComparator EXCLUSIONS_EXPLICIT_FIRST = new SubscriptionModeAndExplicitComparator(-1,-1);
	private int inclusionMultiplier;
	private int implicitMultiplier;
	
	private SubscriptionModeAndExplicitComparator(int inclusionMultiplier, int implicitMultiplier) {
		this.inclusionMultiplier = inclusionMultiplier;
		this.implicitMultiplier = implicitMultiplier;
	}
	
	public int compare(MailingListSubscription arg0, MailingListSubscription arg1) {
		
		int value = inclusionMultiplier * arg0.mode().compareTo(arg1.mode());
		// if the inclusion mode is the same, then we need to sort on the explicit vs implicit
		if (0 == value) {
			value = implicitMultiplier * Boolean.valueOf(arg0.explicit()).compareTo(Boolean.valueOf(arg1.explicit()));
		} 
		return value;
	}

}
