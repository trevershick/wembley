package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import com.railinc.wembley.api.event.AbstractMailinglistDeliverySpecVo;
import com.railinc.wembley.api.event.ApplicationMailinglistVo;
import com.railinc.wembley.api.event.BaseEmailDeliverySpecVo;
import com.railinc.wembley.api.event.NamedMailinglistVo;
import com.railinc.wembley.legacy.domain.dao.MailingListDao;
import com.railinc.wembley.legacy.domain.dao.MailingListSubscriptionDao;
import com.railinc.wembley.legacy.rules.MailingListSubscriptionRules;
import com.railinc.wembley.legacy.rules.dao.MailingListSubscriptionRuleDao;
import com.railinc.wembley.legacy.subscriptions.Subscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;
import com.railinc.wembley.legacysvc.domain.Subscribers;

public class MailinglistSubscriptionResolver {

	private MailingListDao mailinglistDao;
	private MailingListSubscriptionDao mailinglistSubDao;
	private MailingListSubscriptionRuleDao mailinglistRulesDao;

	private MailingListContactServiceRequestor mailinglistContactServiceRequestor;

	public MailinglistSubscriptionResolver( MailingListDao mailinglistDao,
			                                MailingListSubscriptionDao mailinglistSubDao,
			                                MailingListSubscriptionRuleDao mailinglistRulesDao ) {
	
		this.mailinglistDao = mailinglistDao;
		this.mailinglistSubDao = mailinglistSubDao;
		this.mailinglistRulesDao = mailinglistRulesDao;
	}
	
	public MailingListsVo getMailingLists( AbstractMailinglistDeliverySpecVo mailinglistDs ) {
		
		MailingListsVo mailinglists = null;
		
		if ( mailinglistDs instanceof ApplicationMailinglistVo ) {
			mailinglists = getApplicationMailingLists( (ApplicationMailinglistVo)mailinglistDs );
		}
		else if ( mailinglistDs instanceof NamedMailinglistVo ) {
			mailinglists = getNamedMailingLists( ((NamedMailinglistVo)mailinglistDs).getShortName() );
		}
		
		return mailinglists;
	}
	
	public MailingListSubscriptions getMailingListSubscriptions( MailingListsVo mailinglists ) {
		
		MailingListSubscriptions subs = new MailingListSubscriptions();

		for ( MailingListVo mailinglist : mailinglists ) {
			subs.addAll( getMailingListSubscriptionsFromDao( mailinglist ) );
		}
		
		return subs;
	}

	protected MailingListsVo getApplicationMailingLists( ApplicationMailinglistVo appMailinglist ) {
		return mailinglistDao.getMailingList( appMailinglist.getApplication(), appMailinglist.getType() );
	}

//	protected MailingListsVo getNamedMailingLists( NamedMailinglistVo namedMailinglist ) {
//		return mailinglistDao.getMailingListWithShortName( namedMailinglist.getShortName() );
//	}
	
	protected MailingListsVo getNamedMailingLists( String shortName ) {
		MailingListsVo vo = new MailingListsVo();
		MailingListVo mailingListByShortName = mailinglistDao.getMailingListByShortName( shortName );
		if (null != mailingListByShortName) {
			vo.add(mailingListByShortName);
		}
		return vo;
}

	protected MailingListSubscriptions getMailingListSubscriptionsFromDao( MailingListVo mailinglist ) {
		return mailinglistSubDao.getSubscriptionsForMailingList( mailinglist.getKey() );
	}

	public List<Subscription> createSubscriptions( String appId,
 									               String deliveryTiming,
									               BaseEmailDeliverySpecVo emailDs,
									               MailingListSubscriptions mailinglistSubs ) {

		List<Subscription> subscriptions = new ArrayList<Subscription>();
		
		Subscribers subscribers = mailinglistSubs.execute();
		
		MailingListSubscriptionRules rules = mailinglistRulesDao.getMailingListSubscriptionRules();
		rules.apply( subscribers );
		
		subscriptions.addAll( mailinglistContactServiceRequestor.createSubscription( appId, deliveryTiming, emailDs, subscribers ) );
		
		return subscriptions;
	}

	public void setMailinglistContactServiceRequestor(
			MailingListContactServiceRequestor mailinglistContactServiceRequestor) {
		this.mailinglistContactServiceRequestor = mailinglistContactServiceRequestor;
	}
}
