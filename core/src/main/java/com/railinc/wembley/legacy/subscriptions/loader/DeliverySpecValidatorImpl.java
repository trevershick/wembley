package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationService;
import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.FailedEventReason;
import com.railinc.wembley.api.util.ServiceLookupUtil;
import com.railinc.wembley.legacy.handlers.FailedEventHandler;

public class DeliverySpecValidatorImpl implements DeliverySpecValidator {

	private static final Logger log = LoggerFactory.getLogger(DeliverySpecValidatorImpl.class);
	private List<FailedEventHandler> failedEventHandlers;

	public boolean isDeliverySpecValid(String appId, Event event, DeliverySpec delSpec) {
		boolean isValid = false;

		if(delSpec != null) {
			List<String> msgs = delSpec.validate();
			if(msgs != null && msgs.size() > 0) {
				handleFailedEvent(appId, event, msgs);
			} else {
				isValid = true;
			}
		}

		return isValid;
	}

	private void handleFailedEvent(String appId, Event event, List<String> msgs) {
		NotificationService failedEventHandler = ServiceLookupUtil.lookupService(appId, this.failedEventHandlers, true);
		if(failedEventHandler != null) {
			try {
				NotificationServiceException e = new NotificationServiceException(msgs.toString());
				((FailedEventHandler)failedEventHandler).handleFailedEvent(appId, event, FailedEventReason.INVALID_DELIVERY_SPEC, e);
			} catch (Throwable e2) {
				log.error("Error in FailedEventHandler", e2);
			}
		} else {
			log.warn(String.format("The event %s for app ID %s failed, but there was no configured failed event handler", event, appId));
		}
	}

	public void setFailedEventHandlers(List<FailedEventHandler> failedEventHandlers) {
		this.failedEventHandlers = failedEventHandlers;
	}
}
