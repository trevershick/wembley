package com.railinc.wembley.legacy.service;

import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;


public interface MailingListsService {
	MailingListsVo allMailingLists();
	MailingListSubscriptions getMySubscriptions(String realm, String userId);
	MailingListsVo getMailingListsNotSubscribedTo(String realm, String userId);
	
	Subscribers getSubscribersToMailingList(String mailingListKey);

	void unsubscribeMe(String realm, String userId, String mailingListId);
	/**
	 * at this point, we won't mess with alternate email addresses and mailing modes at this point
	 * for this method we'll assume default email
	 */
	void subscribeMe(String realm, String userId, String mailingListId);
	void deleteMailingList(String mailingListKey);
	MailingListVo updateMailingList(String key, MailingListVo vo);
	MailingListVo createMailingList(MailingListVo vo);
	MailingListVo getMailingList(String key);
	MailingListSubscriptions getMailingListSubscriptions(String mailingListKey);
	/**
	 * @param subscriptionKey
	 * @return true if the subscription was deleted, false otherwise
	 */
	boolean deleteSubscription(String subscriptionKey);
	/**
	 * @param subscriptionKey
	 * @return null if not found
	 */
	MailingListSubscription getSubscription(String subscriptionKey);
	MailingListSubscription createBasicUserSubscription(String mailingListKey, String email,
			SubscriptionMode mode);
	MailingListSubscription createSSORoleSubscription(String mailingListKey, String ssoRole,
			SubscriptionMode mode);
	MailingListSubscription createSSOUserSubscription(String mailingListKey, String ssoUser,
			SubscriptionMode mode);
	MailingListSubscription createSSOUsersSubscription(String mailingListKey);
	MailingListSubscription createSsoAppSubscription(String mailingListKey,
			String ssoapp, SubscriptionMode mode);
}
