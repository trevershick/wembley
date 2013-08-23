package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.DeliverySpecs;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FindUsRailContactVo;
import com.railinc.wembley.api.event.FindUsRailDeliverySpecVo;
import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacy.handlers.SubscriptionLoaderHandler;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactResponse;
import com.railinc.wembley.legacy.subscriptions.Subscription;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class FindUsRailContactSubscriptionLoaderImpl implements SubscriptionLoader {

	private static final Logger log = LoggerFactory.getLogger(FindUsRailContactSubscriptionLoaderImpl.class);
	private String appId;
	private ApplicationDao applicationDao;
	private FindUsRailContactServiceRequestor findUsRailContactService;
	private DeliverySpecValidator deliverySpecValidator;
	private List<SubscriptionLoaderHandler> subscriptionLoaderHandlers;

	public FindUsRailContactSubscriptionLoaderImpl(String appId, ApplicationDao applicationDao, FindUsRailContactServiceRequestor findUsRailContactService) {
		this.appId = appId;
		this.applicationDao = applicationDao;
		this.findUsRailContactService = findUsRailContactService;
		log.info(String.format("Instantiated the FindUsRailContactSubscriptionLoaderImpl for App ID: %s", appId));
	}

	public List<Subscription> loadSubscriptions(Event event) {
		List<Subscription> subs = new ArrayList<Subscription>();

		if(log.isDebugEnabled()) {
			log.debug(String.format("Loading subscriptions using the FindUsRailContactSubscriptionLoader with AppId %s for an event with AppId of %s",
					this.appId, event != null && event.getEventHeader() != null ? event.getEventHeader().getAppId() : null));
			log.debug("ApplicationDao=" + this.applicationDao);
			log.debug("FindUsRailContactService=" + this.findUsRailContactService);
		}


		if(event != null && event.getEventHeader() != null && event.getEventHeader().getDeliverySpecs() != null && findUsRailContactService != null) {
			String deliveryTiming = null;
			if(applicationDao != null) {
				ApplicationVo app = applicationDao.getApplication(event.getEventHeader().getAppId());
				if(app != null) {
					deliveryTiming = app.getDefaultDeliveryTiming();
				}
			}

			DeliverySpecs delSpecs = event.getEventHeader().getDeliverySpecs();
			if(delSpecs.getDeliverySpecs() != null) {
				for(DeliverySpec spec : delSpecs.getDeliverySpecs()) {
					if (spec != null && spec instanceof FindUsRailDeliverySpecVo) {
						if(this.deliverySpecValidator == null || this.deliverySpecValidator.isDeliverySpecValid(event.getEventHeader().getAppId(), event, spec)) {
							FindUsRailDeliverySpecVo furSpec = (FindUsRailDeliverySpecVo)spec;
							if(furSpec.getContacts() != null) {
								for(FindUsRailContactVo contact : furSpec.getContacts()) {
									FindUsRailContactResponse response = this.findUsRailContactService.getFindUsRailContacts(contact);
									List<Subscription> s = this.findUsRailContactService.createSubscription(event.getEventHeader().getAppId(), deliveryTiming,
											furSpec, response);
									invokeSubLoaderHandlers(event, s, contact, response);
									subs.addAll(s);
								}
							}
						} else {
							invokeSubLoaderHandlers(event, null, null, null);
							log.warn(String.format("Received an invalid delivery spec, no subscriptions loaded: %s", spec));
						}
					}
				}
			}
		}
		return subs;
	}

	private void invokeSubLoaderHandlers(Event event, List<Subscription> subs, FindUsRailContactVo contact, FindUsRailContactResponse response) {
		if(this.subscriptionLoaderHandlers != null && !this.subscriptionLoaderHandlers.isEmpty()) {
			String subInfo = String.format("Requested FindUsRailContact: %s - Returned %s", contact, response);
			for(SubscriptionLoaderHandler handler : this.subscriptionLoaderHandlers) {
				if(handler != null) {
					try {
						handler.handleSubscriptionsLoaded(this.getClass(), event, subs, subInfo);
					} catch (Throwable e) {
						log.error("Error trying to invoke Subscription Loader Handler", e);
					}
				}
			}
		}
	}

	public String getAppId() {
		return appId;
	}

	public void setDeliverySpecValidator(DeliverySpecValidator deliverySpecValidator) {
		this.deliverySpecValidator = deliverySpecValidator;
	}

	public void setSubscriptionLoaderHandlers(List<SubscriptionLoaderHandler> subscriptionLoaderHandlers) {
		this.subscriptionLoaderHandlers = subscriptionLoaderHandlers;
	}
}
