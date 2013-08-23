package com.railinc.wembley.legacy.service;

import java.util.List;

import com.railinc.wembley.legacy.domain.dao.MailingListDao;
import com.railinc.wembley.legacy.domain.dao.MailingListSubscriptionDao;
import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUser;
import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionClass;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class MailingListsServiceImpl implements MailingListsService {
	private MailingListDao dao = null;
	private MailingListSubscriptionDao subscriptionDao;
	private SSO sso = null;
	
	
	public MailingListSubscriptionDao getSubscriptionDao() {
		return subscriptionDao;
	}

	public void setSubscriptionDao(MailingListSubscriptionDao subscriptionDao) {
		this.subscriptionDao = subscriptionDao;
	}
	
	public MailingListDao getDao() {
		return dao;
	}

	public void setDao(MailingListDao dao) {
		this.dao = dao;
	}

	public MailingListsVo allMailingLists() {
		return getDao().getMailingLists();
	}
	
	public Subscribers getSubscribersToMailingList(String mailingListKey) {
		if (mailingListKey == null) {
			return new Subscribers();
		}
		MailingListSubscriptions subscriptionsForMailingList = getSubscriptionDao().getSubscriptionsForMailingList(mailingListKey);
		Subscribers subscribers = subscriptionsForMailingList.execute();
		return subscribers;
	}
	
	public MailingListsVo getMailingListsNotSubscribedTo(String realm, String userId) {
		// get the list of all mailing lists,
		// get the list of 'my subscriptions'
		// return the list of mailing lists not in 'my subscriptions'
		MailingListsVo allMailingLists = allMailingLists();
		
		MailingListSubscriptions mySubscriptions = getMySubscriptions(realm, userId);
		for (MailingListSubscription s : mySubscriptions) {
			String mailingListKey = s.getMailingListKey();
			MailingListVo mailingListToRemove = allMailingLists.byKey(mailingListKey);
			allMailingLists.remove(mailingListToRemove);
		}
		return allMailingLists;
	}

	public MailingListSubscriptions getMySubscriptions(String realm, String userId) {
		Subscriber user = null;
		MailingListSubscriptions returnValue = new MailingListSubscriptions();
		
		// load all mailing list subscriptions
		if (SSOUser.REALM.equals(realm)) {
			SSOUser ssoUser = new SSOUser(getSso(), userId);
			
			// preload this user's stuff
			List<String> roles = getSso().getRolesForUser(userId);
			List<String> apps = getSso().getAppsForUser(userId);
			
			ssoUser.addSsoRoles(roles);
			ssoUser.addSsoModuleIds(apps);
			user = ssoUser;
		}
		
		List<String> mailingListIds = getDao().getMailingListIds();
		for (String mailingListId : mailingListIds) {
			MailingListSubscriptions subscriptionsForMailingList = getSubscriptionDao().getSubscriptionsForMailingList(mailingListId);
			MailingListSubscription match = subscriptionsForMailingList.match(user);
			if (match != null && SubscriptionMode.INCLUSION.equals(match.mode())) {
				returnValue.add(match);
			}
		}

		return returnValue;
	}

	public SSO getSso() {
		return sso;
	}

	public void setSso(SSO sso) {
		this.sso = sso;
	}

	public void unsubscribeMe(String realm, String userId, String mailingListId) {
		Subscriber user = null;
		if (mailingListId == null || realm == null || userId == null) {
			return;
		}
		// load all mailing list subscriptions
		if (SSOUser.REALM.equals(realm)) {
			SSOUser ssoUser = new SSOUser(getSso(), userId);
			
			// preload this user's stuff
			List<String> roles = getSso().getRolesForUser(userId);
			List<String> apps = getSso().getAppsForUser(userId);
			
			ssoUser.addSsoRoles(roles);
			ssoUser.addSsoModuleIds(apps);
			user = ssoUser;
		}
		
		// find the matching subscription for that mailingListId
		MailingListSubscriptions subscriptionsForMailingList = getSubscriptionDao().getSubscriptionsForMailingList(mailingListId);
		MailingListSubscription match = subscriptionsForMailingList.match(user);
		if ( null == match ) {
			// if there's no matching subscription then don't do anything
			return;
		}

		if (match.explicit()) {
			// if there's a matching subscription and it's explicit
			// delete it.
			getSubscriptionDao().updateMode(match.key(), SubscriptionMode.EXCLUSION);
		} else {
			// if there's a matching subscription and it's implicit,
			// 	create an explicit exclusion for this user.
			getSubscriptionDao().exclude(mailingListId, SubscriptionClass.SSOUser, userId);
		}
	}
	
	public void subscribeMe(String realm, String userId, String mailingListId) {
		Subscriber user = null;
		if (mailingListId == null || realm == null || userId == null) {
			return;
		}
		// load all mailing list subscriptions
		if (SSOUser.REALM.equals(realm)) {
			SSOUser ssoUser = new SSOUser(getSso(), userId);
			
			// preload this user's stuff
			List<String> roles = getSso().getRolesForUser(userId);
			List<String> apps = getSso().getAppsForUser(userId);
			
			ssoUser.addSsoRoles(roles);
			ssoUser.addSsoModuleIds(apps);
			user = ssoUser;
		}
		// find the matching subscription for that mailingListId
		MailingListSubscriptions subscriptionsForMailingList = getSubscriptionDao().getSubscriptionsForMailingList(mailingListId);
		MailingListSubscription match = subscriptionsForMailingList.match(user);

		if (match != null && match.explicit() && match.mode().equals(SubscriptionMode.INCLUSION)) {
			// the user is already subscribed explicitly
			return;
		}

		if (match == null || !match.explicit()) {
			// wedon't have an explicit match so, create the subscritpoin
			// setup subscription
			getSubscriptionDao().include(mailingListId, SubscriptionClass.SSOUser, userId);
		}

		// we have a match but it's an explicit exclusion
		// swap the exclusion to inclusion
		if (match != null && match.explicit() && match.mode().equals(SubscriptionMode.EXCLUSION)) {
			getSubscriptionDao().updateMode(match.key(), SubscriptionMode.INCLUSION);
		}
		
	}

	public MailingListVo createMailingList(MailingListVo vo) {
		getDao().createMailingList(vo);
		return getDao().getMailingList(vo.getKey());
	}

	public void deleteMailingList(String mailingListKey) {
		MailingListSubscriptions subs = getSubscriptionDao().getSubscriptionsForMailingList(mailingListKey);
		for (MailingListSubscription mls : subs) {
			getSubscriptionDao().deleteSubscription(mls.key());
		}
		getDao().deleteMailingList(mailingListKey);
	}

	public MailingListVo getMailingList(String key) {
		return getDao().getMailingList(key);
	}

	public MailingListVo updateMailingList(String key, MailingListVo vo) {
		getDao().updateMailingList(key, vo);
		// if the key changes, update subscriptions too
		if (!key.equals(vo.getKey())) {
			getSubscriptionDao().mailingListKeyChanged(key, vo.getKey());
		}
		return getDao().getMailingList(key);
	}

	public MailingListSubscriptions getMailingListSubscriptions(
			String mailingListKey) {
		if (mailingListKey == null) {
			return new MailingListSubscriptions();
		}
		MailingListSubscriptions subscriptionsForMailingList = getSubscriptionDao().getSubscriptionsForMailingList(mailingListKey);
		return subscriptionsForMailingList;
	}

	public MailingListSubscription createBasicUserSubscription(
			String mailingListKey, String email, SubscriptionMode mode) {
		return getSubscriptionDao().insert(mailingListKey, mode, SubscriptionClass.BasicUser, null, email);
	}

	public MailingListSubscription createSSORoleSubscription(
			String mailingListKey, String ssoRole, SubscriptionMode mode) {
		return getSubscriptionDao().insert(mailingListKey, mode, SubscriptionClass.SSOUsersInRole, ssoRole, null);
	}
	
	public MailingListSubscription createSsoAppSubscription(
			String mailingListKey, String ssoapp, SubscriptionMode mode) {
		return getSubscriptionDao().insert(mailingListKey, mode, SubscriptionClass.SSOAppUsers, ssoapp, null);
	}

	public MailingListSubscription createSSOUserSubscription(
			String mailingListKey, String ssoUser, SubscriptionMode mode) {
		return getSubscriptionDao().insert(mailingListKey, mode, SubscriptionClass.SSOUser, ssoUser, null);
	}

	public MailingListSubscription createSSOUsersSubscription(
			String mailingListKey) {
		return getSubscriptionDao().insert(mailingListKey, SubscriptionMode.INCLUSION, SubscriptionClass.SSOAllUsers, null, null);
	}

	public boolean deleteSubscription(String subscriptionKey) {
		if (null == subscriptionKey) {
			return false;
		}
		boolean deleted = getSubscriptionDao().deleteSubscription(subscriptionKey);
		return deleted;
	}

	public MailingListSubscription getSubscription(String subscriptionKey) {
		if (null == subscriptionKey) {
			return null;
		}
		return getSubscriptionDao().getSubscription(subscriptionKey);
	}



}
