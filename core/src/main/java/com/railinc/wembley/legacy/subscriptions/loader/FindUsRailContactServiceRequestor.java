package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.api.event.FindUsRailContactVo;
import com.railinc.wembley.api.event.FindUsRailDeliverySpecVo;
import com.railinc.wembley.api.findusrail.FindUsRailContactCategoryRole;
import com.railinc.wembley.api.findusrail.FindUsRailContactCompanyType;
import com.railinc.wembley.legacy.services.findusrail.FindUsRailContactService;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContact;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactRequestMsg;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactResponse;
import com.railinc.wembley.legacy.subscriptions.DefaultSubscriptionImpl;
import com.railinc.wembley.legacy.subscriptions.Subscription;

public class FindUsRailContactServiceRequestor {

	private static final Logger log = LoggerFactory.getLogger(FindUsRailContactServiceRequestor.class);
	private FindUsRailContactService service;
	private boolean useMultipleEmails = false;


	public FindUsRailContactServiceRequestor() {
		log.info("Instanting the FindUsrailContactServiceRequestor");
	}

	public FindUsRailContactResponse getFindUsRailContacts(FindUsRailContactVo contact) {
		FindUsRailContactResponse response = null;
		if(service != null && contact != null) {
			FindUsRailContactRequestMsg request = new FindUsRailContactRequestMsg();
			request.setCompanyType(FindUsRailContactCompanyType.valueOf(contact.getCompanyType()));
			request.setMark(contact.getCompanyId());
			request.setCategory(contact.getCategory());
			request.setContactType(StringUtils.isEmpty(contact.getCategoryRole()) ? null : FindUsRailContactCategoryRole.valueOf(contact.getCategoryRole()));
			request.setCategoryFunction(StringUtils.isEmpty(contact.getCategoryFunction()) ? null : contact.getCategoryFunction());
			if(contact.isIncludeAgents() != null && contact.isIncludeAgents()) {
				request.setIncludeAgents(true);
			}
			if(contact.isIncludeChildren() != null && contact.isIncludeChildren()) {
				request.setIncludeChildren(true);
			}
			if(contact.isIncludeParent() != null && contact.isIncludeParent()) {
				request.setIncludeParent(true);
			}
			if(log.isDebugEnabled()) {
				log.debug(String.format("Calling the FindUsRail Contact Service with the following Request: %s", request));
			}
			response = this.service.getFindUsRailContacts(request);
		}
		return response;
	}

	public List<Subscription> createSubscription(String appId, String deliveryTiming, FindUsRailDeliverySpecVo furSpec, FindUsRailContactResponse response) {

		List<Subscription> subs = null;

		if(log.isDebugEnabled()) {
			log.debug(String.format("Creating subscriptions with the APP ID: %s for FindUsRailContacts with the Delivery Timing %s and Multiple Emails: %s",
					appId, deliveryTiming, String.valueOf(useMultipleEmails)));
			log.debug(String.format("Original FindUsRail Delivery Spec: %s", furSpec));
			log.debug(String.format("FindUsRail Contacts: %s", response));
		}

		if(useMultipleEmails) {
			subs = createSubscriptionsWithMultipleEmails(appId, deliveryTiming, furSpec, response);
		} else {
			subs = createSubscriptionWithSingleEmail(appId, deliveryTiming, furSpec, response);
		}

		if(log.isDebugEnabled()) {
			log.debug(String.format("Created the following FindUsRail based subscriptions: %s", subs));
		}

		return subs;
	}

	private List<Subscription> createSubscriptionWithSingleEmail(String appId, String deliveryTiming, FindUsRailDeliverySpecVo furSpec,FindUsRailContactResponse response) {

		List<Subscription> subs = new ArrayList<Subscription>();

		if(furSpec != null && response != null && response.getContacts() != null) {
			DefaultSubscriptionImpl sub = null;
			StringBuffer toAddr = new StringBuffer();

			EmailDeliverySpecVo delSpec = new EmailDeliverySpecVo();
			delSpec.setFrom(furSpec.getFrom());
			delSpec.setSubject(furSpec.getSubject());
			delSpec.setContentType(furSpec.getContentType());
			delSpec.setAttachments(furSpec.getAttachments());

			for(FindUsRailContact furContact : response.getContacts()) {
				if(furContact != null && StringUtils.isNotEmpty(furContact.getEmailAddress())) {
					if(sub == null) {
						sub = new DefaultSubscriptionImpl(appId);
						sub.setDeliveryTiming(deliveryTiming);
						sub.setDeliverySpec(delSpec);
					}
					if(toAddr.length() > 0) {
						toAddr.append(";");
					}
					toAddr.append(furContact.getEmailAddress());
				}
			}

			if(toAddr.length() > 0) {
				delSpec.setTo(toAddr.toString());
				subs.add(sub);
			}
		}

		return subs;
	}

	private List<Subscription> createSubscriptionsWithMultipleEmails(String appId, String deliveryTiming, FindUsRailDeliverySpecVo furSpec,FindUsRailContactResponse response) {

		List<Subscription> subs = new ArrayList<Subscription>();

		if(furSpec != null && response != null && response.getContacts() != null) {
			for(FindUsRailContact furContact : response.getContacts()) {
				if(furContact != null && StringUtils.isNotEmpty(furContact.getEmailAddress())) {
					DefaultSubscriptionImpl sub = new DefaultSubscriptionImpl(appId);
					sub.setDeliveryTiming(deliveryTiming);

					EmailDeliverySpecVo delSpec = new EmailDeliverySpecVo();
					delSpec.setFrom(furSpec.getFrom());
					delSpec.setSubject(furSpec.getSubject());
					delSpec.setContentType(furSpec.getContentType());
					delSpec.setAttachments(furSpec.getAttachments());
					delSpec.setTo(furContact.getEmailAddress());
					sub.setDeliverySpec(delSpec);

					subs.add(sub);
				}
			}
		}

		return subs;
	}

	public void setService(FindUsRailContactService service) {
		this.service = service;
	}

	public void setUseMultipleEmails(boolean useMultipleEmails) {
		this.useMultipleEmails = useMultipleEmails;
		log.info(String.format("Setting the UseMultipleEmails Flag: %s", String.valueOf(useMultipleEmails)));
	}
}
