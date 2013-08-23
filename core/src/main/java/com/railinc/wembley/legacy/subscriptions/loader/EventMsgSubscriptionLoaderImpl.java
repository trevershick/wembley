package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.DeliverySpecs;
import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FtpViaMqDeliverySpecVo;
import com.railinc.wembley.api.event.MqDeliverySpecVo;
import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacy.subscriptions.DefaultSubscriptionImpl;
import com.railinc.wembley.legacy.subscriptions.Subscription;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class EventMsgSubscriptionLoaderImpl implements SubscriptionLoader {

	private static final Logger log = LoggerFactory.getLogger(EventMsgSubscriptionLoaderImpl.class);
	private String appId;
	private ApplicationDao applicationDao;
	private DeliverySpecValidator deliverySpecValidator;

	public EventMsgSubscriptionLoaderImpl(String appId) {
		this.appId = appId;
		log.info(String.format("Instantiating EventMsgSubscriptionLoaderImpl with AppId %s", appId));
	}

	public List<Subscription> loadSubscriptions(Event event) {
		List<Subscription> subs = new ArrayList<Subscription>();

		if(log.isDebugEnabled()) {
			log.debug(String.format("Loading subscriptions using the EventMsgSubscriptionLoader with AppId %s for an event with AppId of %s",
					this.appId, event != null && event.getEventHeader() != null ? event.getEventHeader().getAppId() : null));
			log.debug("ApplicationDao=" + this.applicationDao);
		}

		if(event != null && event.getEventHeader() != null && event.getEventHeader().getDeliverySpecs() != null) {
			DeliverySpecs delSpecs = event.getEventHeader().getDeliverySpecs();
			if(delSpecs.getDeliverySpecs() != null) {
				for(DeliverySpec spec : delSpecs.getDeliverySpecs()) {
					if ( spec != null && (spec instanceof EmailDeliverySpecVo || spec instanceof MqDeliverySpecVo || spec instanceof FtpViaMqDeliverySpecVo ) ) {
						if(this.deliverySpecValidator == null || this.deliverySpecValidator.isDeliverySpecValid(event.getEventHeader().getAppId(), event, spec)) {
							if(log.isDebugEnabled()) {
								log.debug(String.format("Creating a subscription for the Delivery Spec %s with an Event App Id of %s", spec, event.getEventHeader().getAppId()));
							}
							DefaultSubscriptionImpl sub = new DefaultSubscriptionImpl(event.getEventHeader().getAppId());
							if(applicationDao != null) {
								ApplicationVo app = applicationDao.getApplication(event.getEventHeader().getAppId());
								if(app != null) {
									sub.setDeliveryTiming(app.getDefaultDeliveryTiming());
								}
							}
							sub.setDeliverySpec(spec);
							subs.add(sub);
						} else {
							log.warn(String.format("Received an invalid delivery spec, no subscriptions loaded: %s", spec));
						}
					}
				}
			}
		}
		return subs;
	}

	public String getAppId() {
		return appId;
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
		log.debug("Setting the ApplicationDao");
	}

	public void setDeliverySpecValidator(DeliverySpecValidator deliverySpecValidator) {
		this.deliverySpecValidator = deliverySpecValidator;
	}
}
