package com.railinc.notifserv.rest.r2009v1.services;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.railinc.notifserv.restsvcs.r2009v1.Application;
import com.railinc.notifserv.restsvcs.r2009v1.Applications;
import com.railinc.notifserv.restsvcs.r2009v1.BasicUserSubscriber;
import com.railinc.notifserv.restsvcs.r2009v1.Delivery;
import com.railinc.notifserv.restsvcs.r2009v1.Event;
import com.railinc.notifserv.restsvcs.r2009v1.EventStatus;
import com.railinc.notifserv.restsvcs.r2009v1.Events;
import com.railinc.notifserv.restsvcs.r2009v1.MailingList;
import com.railinc.notifserv.restsvcs.r2009v1.MailingLists;
import com.railinc.notifserv.restsvcs.r2009v1.Notification;
import com.railinc.notifserv.restsvcs.r2009v1.NotificationCountByStatus;
import com.railinc.notifserv.restsvcs.r2009v1.Notifications;
import com.railinc.notifserv.restsvcs.r2009v1.SearchResults;
import com.railinc.notifserv.restsvcs.r2009v1.Subscription;
import com.railinc.notifserv.restsvcs.r2009v1.Subscriptions;
import com.railinc.notifserv.restsvcs.r2009v1.UserSubscriber;
import com.railinc.wembley.legacy.domain.impl.basic.BasicUser;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUser;
import com.railinc.wembley.legacy.service.NotificationSearchResults;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;
import com.railinc.wembley.legacysvc.domain.EventVo;
import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;
import com.railinc.wembley.legacysvc.domain.NotificationVo;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;

public class R2009V1Converter {
	private static final Logger LOG = LoggerFactory.getLogger(R2009V1Converter.class);
	
	public Subscriptions convert(MailingListSubscriptions v, MailingListsVo lists) {
		if (v == null) {
			return null;
		}
		Subscriptions a = new Subscriptions();
		for (MailingListSubscription vo:v) {
			Subscription convert = convert(vo, lists.byKey(vo.getMailingListKey()));
			if (null != convert) {
				a.getSubscriptions().add(convert);
			}
		}
		return a;
	}

	public Subscription convert(MailingListSubscription v, MailingListVo ml) {
		if (v == null) {
			return null;
		}
		if (null == RESTSupportedSubscriptionTypes.valueOf(v.subscriptionType())) {
			return null;
		}
		Subscription s = new Subscription();
		s.setKey(v.key());
		s.setExplicit(v.explicit());
		s.setMode(v.mode().toString());
		Delivery deliveryType = new Delivery();
		deliveryType.setMedia(v.delivery().toString());
		deliveryType.setParam(v.deliveryArgument());
		s.setDelivery(deliveryType);
		s.setMailingList(convert(ml));
		s.setDescription(v.description());
		s.setType(v.subscriptionType());
		s.setTypeArgument(v.subscriptionDetails());
		s.setSubscriptionDate(convert(v.lastModified()));
		return s;
	}
	public Applications convertApplications(List<ApplicationVo> v) {
		if (v == null) {
			return null;
		}
		Applications a = new Applications();
		for (ApplicationVo vo:v) {
			a.getApplications().add(convert(vo));
		}
		return a;
	}
	
	public Application convert(ApplicationVo v) {
		if (v == null) {
			return null;
		}
		Application ml = new Application();
		ml.setAdminEmail(v.getAdminEmail());
		ml.setAppId(v.getAppId());
		ml.setCreated(convert(v.getCreatedTimestamp()));
		ml.setDefaultDeliveryTiming(v.getDefaultDeliveryTiming());
		return ml;
	}
	
	public Calendar convert(Date date) {
		if (null == date) {
			return null;
		}
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		return instance;
	}
	
	public MailingLists convert(MailingListsVo v) {
		if (v == null) {
			return null;
		}
		MailingLists mailingLists = new MailingLists();
		for (MailingListVo vo:v) {
			mailingLists.getMailingLists().add(convert(vo));
		}
		return mailingLists;
	}
	
	
	
	public MailingList convert(MailingListVo v) {
		if (v == null) {
			return null;
		}
		MailingList ml = new MailingList();
		ml.setActive(v.isActive());
		ml.setApplication(v.getApplication());
		ml.setDescription(v.getDescription());
		ml.setFromAddress(v.getFromAddress());
		ml.setKey(v.getKey());
		ml.setShortName(v.getShortName());
		ml.setTitle(v.getTitle());
		ml.setType(v.getType());

		return ml;
	}

	public Event convert(EventVo v, boolean withContents) {
		if (v == null) {
			return null;
		}
		Event ml = new Event();
		ml.setUid(v.getEventUid());
		ml.setStateChanged(convert(v.getStateTimestamp()));
		ml.setRetryCount(v.getRetryCount());
		ml.setState(v.getState());
		ml.setSendAfter(convert(v.getSendAfter()));

		if (withContents) {
			byte[] contents = v.getContents();
			ml.setRawEvent(new String(contents));
			ml.setEvent(parseXmlQuietly(contents));
		}
		
		return ml;
	}

	private Element parseXmlQuietly(byte[] contents) {
		try {
			DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document d = newDocumentBuilder.parse(new ByteArrayInputStream(contents));
			return d.getDocumentElement();
		} catch (Exception e) {
			return null;
		}
	}

	public Notification convert(NotificationVo v, boolean withContents) {
		if (v == null) {
			return null;
		}
		Notification jaxbNotification = new Notification();
		jaxbNotification.setUid(v.getNotificationUid());
		jaxbNotification.setCreated(convert(v.getCreatedTimestamp()));
		jaxbNotification.setStateChanged(convert(v.getStateTimestamp()));
		jaxbNotification.setRetryCount(v.getRetryCount());
		jaxbNotification.setState(v.getState());
		
		

		if (withContents) {
			byte[] content = v.getDeliverySpecification();
			jaxbNotification.setDelivery(parseXmlQuietly(content));
		}
		
		return jaxbNotification;
	}

	
	public Events convertEvents(List<EventVo> eventsByCorrelationId, boolean withContents) {
		Events events = new Events();
		for (EventVo e : eventsByCorrelationId) {
			events.getEvents().add(convert(e,withContents));
		}
		return events;
	}

	public com.railinc.notifserv.restsvcs.r2009v1.Subscribers convert(Subscribers ss) {
		com.railinc.notifserv.restsvcs.r2009v1.Subscribers ssts = new com.railinc.notifserv.restsvcs.r2009v1.Subscribers();
		for (Subscriber s : ss) {
			com.railinc.notifserv.restsvcs.r2009v1.Subscriber convert = convert(s);
			if (convert != null) {
				ssts.getSubscribers().add(convert);
			}
		}
		return ssts;
		
	}
	public com.railinc.notifserv.restsvcs.r2009v1.Subscriber convert(Subscriber v) {
		if (v == null) {
			return null;
		}
		if (v instanceof SSOUser) {
			UserSubscriber s = new UserSubscriber();
			s.setUid(v.uid());
			s.setRealm(v.realm());

			Delivery deliveryType = new Delivery();
			deliveryType.setMedia(v.delivery().toString());
			deliveryType.setParam(v.deliveryArgument());
			s.setDelivery(deliveryType);
			return s;
		} else if (v instanceof BasicUser) {
			
			BasicUserSubscriber s = new BasicUserSubscriber();
			
			Delivery deliveryType = new Delivery();
			deliveryType.setMedia(v.delivery().toString());
			deliveryType.setParam(v.deliveryArgument());
			s.setDelivery(deliveryType);
			
			return s;
			
		} else {
			LOG.error("dont' know how to convert a " + v.getClass().getName());
			return null;
		}
		
	}

	public Notifications convertNotifications(List<NotificationVo> notifications, boolean withContents) {
		Notifications events = new Notifications();
		for (NotificationVo e : notifications) {
			events.getNotifications().add(convert(e,withContents));
		}
		return events;
	}

	public ApplicationVo convert(Application v) {
		ApplicationVo ml = new ApplicationVo();
		ml.setAdminEmail(v.getAdminEmail());
		ml.setAppId(v.getAppId());
		ml.setCreatedTimestamp(v.getCreated().getTime());
		ml.setDefaultDeliveryTiming(v.getDefaultDeliveryTiming());
		return ml;
	}

	public MailingListVo convert(MailingList ml) {
		MailingListVo v = new MailingListVo();
		v.setActive(ml.isActive());
		v.setApplication(ml.getApplication());
		v.setDescription(ml.getDescription());
		v.setFromAddress(ml.getFromAddress());
		v.setKey(ml.getKey());
		v.setShortName(ml.getShortName());
		v.setTitle(ml.getTitle());
		v.setType(ml.getType());
		return v;
	}

	public Subscriptions convert(MailingListSubscriptions mls) {
		Subscriptions subscriptions = new Subscriptions();
		for (MailingListSubscription ml : mls) {
			subscriptions.getSubscriptions().add(convert(ml));
		}
		return subscriptions;
	}

	public Subscription convert(MailingListSubscription ml) {
		Subscription s = new Subscription();
		s.setKey(ml.key());
		s.setMode(ml.mode().toString());
		s.setExplicit(ml.explicit());
		Delivery deliveryType = new Delivery();
		deliveryType.setMedia(ml.delivery().toString());
		deliveryType.setParam(ml.deliveryArgument());
		s.setDelivery(deliveryType);
		s.setDescription(ml.description());
		s.setSubscriptionDate(convert(ml.lastModified()));
		s.setType(ml.subscriptionType());
		s.setTypeArgument(ml.subscriptionDetails());
//		MailingList mailingList = new MailingList();
//		mailingList.setKey(ml.getMailingListKey());
//		s.setMailingList(mailingList);
		return s;
	}


	public EventStatus convert(EventVo event, Map<String,Integer> statuses) {
		EventStatus s = new EventStatus();
		s.setEventUid(event.getEventUid());
		s.setState(event.getState());
		s.setSendAfter(convert(event.getSendAfter()));
		int total = 0;
		int remaining = 0;
		for (Map.Entry<String, Integer> e : statuses.entrySet()) {
			String state = e.getKey();
			int count = e.getValue().intValue();
			
			total += count;
			if ("OPEN".equals(state)) {
				remaining += count;
			}
			NotificationCountByStatus nc = new NotificationCountByStatus();
			nc.setCount(count);
			nc.setState(state);
			s.getNotificationCountByStatuses().add(nc);
		}
		s.setTotalNotificationCount(total);
		s.setTotalNotificationsRemaining(remaining);
		s.setStateChanged(convert(event.getStateTimestamp()));
		return s;
	}

	public SearchResults convertNotificationSearchResults(EventVo eventByCorrelationId,
			NotificationSearchResults res) {
		SearchResults r = new SearchResults();
		r.setToken(res.getToken());
		
		Notifications notifs = convertNotifications(res, true);
		r.getAnies().addAll(notifs.getNotifications());
		return r;
	}

	
}
