package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.DeliverySpecs;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.SSOUserDeliverySpecVo;
import com.railinc.wembley.api.event.SSOUserIdVo;
import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacy.handlers.SubscriptionLoaderHandler;
import com.railinc.wembley.legacy.services.sso.SsoContactResponse;
import com.railinc.wembley.legacy.subscriptions.Subscription;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class SsoUserSubscriptionLoaderImpl implements SubscriptionLoader {

	private static final Logger log = LoggerFactory.getLogger(SsoUserSubscriptionLoaderImpl.class);
	private String appId;
	private ApplicationDao applicationDao;
	private SsoContactServiceRequestor ssoContactRequestor;
	private DeliverySpecValidator deliverySpecValidator;
	private List<SubscriptionLoaderHandler> subscriptionLoaderHandlers;

	public SsoUserSubscriptionLoaderImpl(String appId, SsoContactServiceRequestor ssoContactRequestor) {
		this.appId = appId;
		this.ssoContactRequestor = ssoContactRequestor;
		log.info(String.format("Instantiating SsoUserSubscriptionLoaderImpl with AppId %s and Requestor %s", appId, ssoContactRequestor));
	}

	public List<Subscription> loadSubscriptions(Event event) {

		List<Subscription> subscriptions = new ArrayList<Subscription>();
		if (log.isDebugEnabled()) {
			log.debug(String.format("[%s] Loading subscriptions using the SsoUserSubscriptionLoaderImpl", this.appId));
			log.debug("ApplicationDao = " + this.applicationDao);
			log.debug("SsoContactServiceRequestor = " + this.ssoContactRequestor);
		}

		if (ssoContactRequestor != null) {
			if (event != null && event.getEventHeader() != null && event.getEventHeader().getDeliverySpecs() != null) {
				if (log.isDebugEnabled()) {
					log.debug(String.format("Loading subscriptions for app ID '%s'", event.getEventHeader().getAppId()));
				}

				DeliverySpecs deliverySpecs = event.getEventHeader().getDeliverySpecs();
				if (deliverySpecs.getDeliverySpecs() != null) {
					for (DeliverySpec deliverySpec : deliverySpecs.getDeliverySpecs()) {
						if (deliverySpec != null && deliverySpec instanceof SSOUserDeliverySpecVo) {
							if(this.deliverySpecValidator == null || this.deliverySpecValidator.isDeliverySpecValid(event.getEventHeader().getAppId(),
												event, deliverySpec)) {
								if (log.isDebugEnabled()) {
									log.debug(String.format("[%s] Creating a subscription for the Delivery Spec %s with an Event App Id of %s",
															appId, deliverySpec, event.getEventHeader().getAppId()));
								}

								subscriptions.addAll(createSubscriptions(event, (SSOUserDeliverySpecVo) deliverySpec));
							} else {
								invokeSubLoaderHandlers(event, null, null, null);
								log.warn(String.format("Received an invalid delivery spec, no subscriptions loaded: %s", deliverySpec));
							}
						}
					}
				}
			}
		}

		if (log.isDebugEnabled()) {
			log.debug(String.format("[%s] Loaded %d subscription(s)", this.appId, subscriptions.size()));
		}

		return subscriptions;
	}

	private void invokeSubLoaderHandlers(Event event, List<Subscription> subs, SSOUserIdVo ssoUserId, SsoContactResponse response) {
		if(this.subscriptionLoaderHandlers != null && !this.subscriptionLoaderHandlers.isEmpty()) {
			String subInfo = String.format("Requested SSOUser: %s - Returned %s", ssoUserId, response);
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

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
		log.debug(String.format("[%s] Setting the ApplicationDao", appId));
	}

	private List<Subscription> createSubscriptions(Event event, SSOUserDeliverySpecVo ssoUserDelSpec) {

		List<Subscription> subscriptions = new ArrayList<Subscription>();
		String deliveryTiming = null;
		ApplicationVo app = null;

		if (ssoUserDelSpec.getUserIds() != null) {
			if (applicationDao != null) {
				app = applicationDao.getApplication(event.getEventHeader().getAppId());
			}

			deliveryTiming = app != null ? app.getDefaultDeliveryTiming() : "0";
			for (SSOUserIdVo ssoUserId : ssoUserDelSpec.getUserIds()) {
				if (ssoUserId.getUserId() != null && ssoUserId.getUserId().length() > 0) {
					SsoContactResponse response = ssoContactRequestor.getSsoContactsByUserId(ssoUserId);
					List<Subscription> subs = ssoContactRequestor.createSubscription(event.getEventHeader().getAppId(),
										deliveryTiming, ssoUserDelSpec, response);
					invokeSubLoaderHandlers(event, subs, ssoUserId, response);
					subscriptions.addAll(subs);
				}
			}
		}

		return subscriptions;
	}

	public void setDeliverySpecValidator(DeliverySpecValidator deliverySpecValidator) {
		this.deliverySpecValidator = deliverySpecValidator;
	}

	public void setSubscriptionLoaderHandlers(List<SubscriptionLoaderHandler> subscriptionLoaderHandlers) {
		this.subscriptionLoaderHandlers = subscriptionLoaderHandlers;
	}
}
