package com.railinc.wembley.api.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.legacy.senders.SenderService;

public class ServiceLookupUtil {
	private static final Logger log = LoggerFactory.getLogger( ServiceLookupUtil.class );

	/**
	 * Given a list of Notification Service objects, find the one that matches
	 * the given application ID
	 *
	 * @param appId
	 * @param services
	 * @return
	 */
	public static NotificationService lookupService(String appId, List<? extends NotificationService> services) {
		return lookupService(appId, services, false);
	}

	/**
	 * Given a list of Notification Service objects, find the one that matches
	 * the given application ID. Return the default object if none is found with
	 * the given application ID.
	 *
	 * @param appId
	 * @param services
	 * @param returnDefault
	 * @return
	 */
	public static NotificationService lookupService(String appId, List<? extends NotificationService> services,
			boolean returnDefault) {
		NotificationService service = null;

		if (services != null) {
			for (NotificationService serv : services) {
				if (serv != null) {
					if (appId != null && appId.equals(serv.getAppId())) {
						service = serv;
						break;
					} else if (returnDefault && NotificationService.DEFAULT_APP_ID.equals(serv.getAppId())) {
						service = serv;
					}
				}
			}
		}

		return service;
	}

	public static List<? extends NotificationService> lookupServices(String appId,
			List<? extends NotificationService> services) {
		return lookupServices(appId, services, false);
	}
	/**
	 * returns the default services from the list of services provided. the deafult
	 * services are determined by the application id on each service.
	 * @param services
	 * @return
	 */
	public static List<? extends NotificationService> lookupDefaultServices(
			List<? extends NotificationService> services) {
		List<NotificationService> returnList = new ArrayList<NotificationService>();
		for (NotificationService service : services) {
			if (NotificationService.DEFAULT_APP_ID.equals(service.getAppId())) {
				returnList.add(service);
			}
		}
		return returnList;
	}
	
	/**
	 * If returnDefault is true, then the LAST default service will be returned.
	 * This is improperly coded to handle multiple default services.
	 * @param appId
	 * @param services
	 * @param returnDefault
	 * @return
	 */
	public static List<? extends NotificationService> lookupServices(String appId,
			List<? extends NotificationService> services, boolean returnDefault) {
		List<NotificationService> servicesList = new ArrayList<NotificationService>();
		NotificationService defaultService = null;
		if (services != null) {
			for (NotificationService serv : services) {
				if (serv != null) {
					if (appId != null && appId.equals(serv.getAppId())) {
						servicesList.add(serv);
					} else if (returnDefault && NotificationService.DEFAULT_APP_ID.equals(serv.getAppId())) {
						if (defaultService != null) {
							log.warn("defaultService was already set to " + defaultService + " but is now set to " + serv);
						}
						defaultService = serv;
					}
				}
			}
		}
		if (returnDefault && defaultService != null && servicesList.isEmpty()) {
			servicesList.add(defaultService);
		}
		return servicesList;
	}

	public static SenderService lookupSenderService(String appId, Class<? extends DeliverySpec> deliverySpecType,
			List<? extends SenderService> services, boolean returnDefault) {
		SenderService service = null;
		List<SenderService> senderSvcs = new ArrayList<SenderService>();

		if (deliverySpecType != null && services != null) {
			for (SenderService serv : services) {
				if (deliverySpecType.equals(serv.getDeliverySpecType())) {
					senderSvcs.add(serv);
				}
			}

			service = (SenderService) lookupService(appId, senderSvcs, returnDefault);
		}

		return service;
	}

	public static TransformationService lookupTransformationService(String appId, Class<? extends DeliverySpec> deliverySpecType,
			List<? extends TransformationService> services, boolean returnDefault) {

		TransformationService service = null;

		if (deliverySpecType != null && services != null) {
			List<TransformationService> transServices = new ArrayList<TransformationService>();
			for (TransformationService serv : services) {
				List<Class<? extends DeliverySpec>> specTypes = serv.getDeliverySpecTypes();
				if(specTypes == null || specTypes.isEmpty() || specTypes.contains(deliverySpecType)) {
					transServices.add(serv);
				}
			}
			service = (TransformationService) lookupService(appId, transServices, returnDefault);
		}

		return service;
	}
}
