package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.BaseEmailDeliverySpecVo;
import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.api.event.SSORoleVo;
import com.railinc.wembley.api.event.SSOUserIdVo;
import com.railinc.wembley.legacy.services.sso.SsoContact;
import com.railinc.wembley.legacy.services.sso.SsoContactRequestMsg;
import com.railinc.wembley.legacy.services.sso.SsoContactResponse;
import com.railinc.wembley.legacy.services.sso.SsoContactService;
import com.railinc.wembley.legacy.subscriptions.DefaultSubscriptionImpl;
import com.railinc.wembley.legacy.subscriptions.Subscription;

public class SsoContactServiceRequestor {

	private static final Logger log = LoggerFactory.getLogger(SsoContactServiceRequestor.class);
	private SsoContactService ssoContactService;
	private boolean useMultipleEmails = false;

	public SsoContactServiceRequestor( SsoContactService ssoContactService ) {
		this.ssoContactService = ssoContactService;
		log.info("Instantiated the SsoContactServiceRequestor");
	}

	public SsoContactResponse getSsoContactsByRole( SSORoleVo ssoRoleVo ) {

		SsoContactResponse response = null;

		if ( ssoContactService != null && ssoRoleVo != null ) {

			SsoContactRequestMsg request = new SsoContactRequestMsg();
			request.setMark( ssoRoleVo.getMark() );
			request.setRole( ssoRoleVo.getRole() );

			if ( request.getMark() != null && request.getMark().length() > 0 ) {
				response = this.ssoContactService.getSsoContactByRoleAndMark( request );
			}
			else {
				response = this.ssoContactService.getSsoContactByRole( request );
			}
		}

		return response;
	}

	public SsoContactResponse getSsoContactsByUserId( SSOUserIdVo ssoUserIdVo ) {

		SsoContactResponse response = null;

		if ( ssoContactService != null && ssoUserIdVo != null ) {

			SsoContactRequestMsg request = new SsoContactRequestMsg();
			request.setUserId( ssoUserIdVo.getUserId() );

			response = this.ssoContactService.getSsoContactByUserId( request );
		}

		return response;
	}

	public List<Subscription> createSubscription( String appId, String deliveryTiming, BaseEmailDeliverySpecVo ssoDelSpec, SsoContactResponse response) {

		List<Subscription> subs = null;

		if(log.isDebugEnabled()) {
			log.debug(String.format("Creating subscriptions with the APP ID: %s for SSOContacts with the Delivery Timing %s and Multiple Emails: %s",
					appId, deliveryTiming, String.valueOf(useMultipleEmails)));
			log.debug(String.format("Original SSO Delivery Spec: %s", ssoDelSpec));
			log.debug(String.format("SSO Contacts: %s", response));
		}

		if(useMultipleEmails) {
			subs = createSubscriptionsWithMultipleEmails(appId, deliveryTiming, ssoDelSpec, response);
		} else {
			subs = createSubscriptionWithSingleEmail(appId, deliveryTiming, ssoDelSpec, response);
		}

		if(log.isDebugEnabled()) {
			log.debug(String.format("Created the following SSO based subscriptions: %s", subs));
		}

		return subs;
	}

	private List<Subscription> createSubscriptionWithSingleEmail(String appId, String deliveryTiming, BaseEmailDeliverySpecVo ssoDelSpec,
			SsoContactResponse response) {

		List<Subscription> subs = new ArrayList<Subscription>();

		if(ssoDelSpec != null && response != null && response.getContacts() != null) {
			DefaultSubscriptionImpl sub = null;
			StringBuffer toAddr = new StringBuffer();

			EmailDeliverySpecVo delSpec = new EmailDeliverySpecVo();
			delSpec.setFrom(ssoDelSpec.getFrom());
			delSpec.setSubject(ssoDelSpec.getSubject());
			delSpec.setContentType(ssoDelSpec.getContentType());
			delSpec.setAttachments(ssoDelSpec.getAttachments());

			for(SsoContact ssoContact : response.getContacts()) {
				if(ssoContact != null && StringUtils.isNotEmpty(ssoContact.getEmailAddress())) {
					if(sub == null) {
						sub = new DefaultSubscriptionImpl(appId);
						sub.setDeliveryTiming(deliveryTiming);
						sub.setDeliverySpec(delSpec);
					}
					if(toAddr.length() > 0) {
						toAddr.append(";");
					}
					toAddr.append(ssoContact.getEmailAddress());
				}
			}

			if(toAddr.length() > 0) {
				delSpec.setTo(toAddr.toString());
				subs.add(sub);
			}
		}

		return subs;
	}

	private List<Subscription> createSubscriptionsWithMultipleEmails(String appId, String deliveryTiming, BaseEmailDeliverySpecVo ssoDelSpec,
			SsoContactResponse response) {

		List<Subscription> subs = new ArrayList<Subscription>();

		if(ssoDelSpec != null && response != null && response.getContacts() != null) {
			for(SsoContact ssoContact : response.getContacts()) {
				if(ssoContact != null && StringUtils.isNotEmpty(ssoContact.getEmailAddress())) {
					DefaultSubscriptionImpl sub = new DefaultSubscriptionImpl(appId);
					sub.setDeliveryTiming(deliveryTiming);

					EmailDeliverySpecVo delSpec = new EmailDeliverySpecVo();
					delSpec.setFrom(ssoDelSpec.getFrom());
					delSpec.setSubject(ssoDelSpec.getSubject());
					delSpec.setContentType(ssoDelSpec.getContentType());
					delSpec.setAttachments(ssoDelSpec.getAttachments());
					delSpec.setTo(ssoContact.getEmailAddress());
					sub.setDeliverySpec(delSpec);

					subs.add(sub);
				}
			}
		}

		return subs;
	}

	public void setUseMultipleEmails(boolean useMultipleEmails) {
		this.useMultipleEmails = useMultipleEmails;
	}
}
