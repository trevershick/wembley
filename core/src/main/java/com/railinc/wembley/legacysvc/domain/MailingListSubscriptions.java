package com.railinc.wembley.legacysvc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MailingListSubscriptions extends ArrayList<MailingListSubscription> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3748748743402783760L;

	
	public MailingListSubscriptions(List<? extends MailingListSubscription> asList) {
		super(asList);
	}
	public MailingListSubscriptions(){}


	public MailingListSubscription byMailingList(String mailingListKey) {
		for (MailingListSubscription s : this) {
			if (s.key().equals(mailingListKey)) {
				return s;
			}
		}
		return null;
	}
	
	public Subscribers execute() {
		Subscribers subscribers = new Subscribers();

		List<MailingListSubscription> ss = new ArrayList<MailingListSubscription>(this);
		
		// put them in inclusino/exclusion 
		Collections.sort(ss, SubscriptionModeComparator.INCLUSIONS_FIRST);
		

		for (MailingListSubscription s : ss) {
			s.execute(subscribers);
		}	
		
		
		return subscribers;
	}
	

	public MailingListSubscription match(Subscriber member) {
		List<MailingListSubscription> ss = new ArrayList<MailingListSubscription>(this);
		
		// put them in exclusion/inclusion order 
		Collections.sort(ss, SubscriptionModeAndExplicitComparator.EXCLUSIONS_EXPLICIT_FIRST);

		
		for (MailingListSubscription s : ss) {
			if (SubscriptionMode.INCLUSION == s.mode() && s.matches(member)) {
				return s;
			} else if (SubscriptionMode.EXCLUSION == s.mode() && s.matches(member)) {
				return s;
			}
		}	
		return null;
	}

}
