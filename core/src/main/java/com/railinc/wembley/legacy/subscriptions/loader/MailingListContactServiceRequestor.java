package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.BaseEmailDeliverySpecVo;
import com.railinc.wembley.api.event.EmailDeliverySpecVo;
import com.railinc.wembley.legacy.subscriptions.DefaultSubscriptionImpl;
import com.railinc.wembley.legacy.subscriptions.Subscription;
import com.railinc.wembley.legacysvc.domain.Subscriber;
import com.railinc.wembley.legacysvc.domain.Subscribers;

public class MailingListContactServiceRequestor {

	private static final Logger log = LoggerFactory.getLogger( MailingListContactServiceRequestor.class );
	private boolean useMultipleEmails = false;

	public List<Subscription> createSubscription( String appId,
			                                      String deliveryTiming,
			                                      BaseEmailDeliverySpecVo delSpec,
			                                      Subscribers subscribers ) {

		List<Subscription> subs = null;

		if(log.isDebugEnabled()) {
			
			log.debug( String.format( "Creating subscriptions with the APP ID: %s for Subscribers with the Delivery Timing %s and Multiple Emails: %s",
					appId, deliveryTiming, String.valueOf( useMultipleEmails ) ) );
			log.debug( String.format( "Original Delivery Spec: %s", delSpec ) );
		}

		if(useMultipleEmails) {
			subs = createSubscriptionsWithMultipleEmails( appId, deliveryTiming, delSpec, subscribers );
		} else {
			subs = createSubscriptionWithSingleEmail( appId, deliveryTiming, delSpec, subscribers );
		}

		if(log.isDebugEnabled()) {
			log.debug( String.format( "Created the following subscriptions: %s", subs ) );
		}

		return subs;
	}

	private List<Subscription> createSubscriptionWithSingleEmail( String appId,
			                                                      String deliveryTiming,
			                                                      BaseEmailDeliverySpecVo baseDelSpec,
			                                                      Subscribers subscribers ) {

		List<Subscription> subs = new ArrayList<Subscription>();

		if ( baseDelSpec != null && subscribers != null && subscribers.size() > 0 ) {
			
			DefaultSubscriptionImpl sub = null;
			StringBuffer toAddr = new StringBuffer();

			EmailDeliverySpecVo delSpec = new EmailDeliverySpecVo();
			delSpec.setFrom( baseDelSpec.getFrom() );
			delSpec.setSubject( baseDelSpec.getSubject() );
			delSpec.setContentType( baseDelSpec.getContentType() );
			delSpec.setAttachments( baseDelSpec.getAttachments() );

			for ( Subscriber subscriber : subscribers ) {
				
				if ( subscriber != null && StringUtils.isNotEmpty( subscriber.deliveryArgument() ) ) {
					
					if( sub == null ) {
					
						sub = new DefaultSubscriptionImpl(appId);
						sub.setDeliveryTiming(deliveryTiming);
						sub.setDeliverySpec(delSpec);
					}
					
					if(toAddr.length() > 0) {
						toAddr.append(";");
					}
					
					toAddr.append( subscriber.deliveryArgument() );
				}
			}

			if(toAddr.length() > 0) {
				
				delSpec.setTo( toAddr.toString() );
				subs.add( sub );
			}
		}

		return subs;
	}

	private List<Subscription> createSubscriptionsWithMultipleEmails( String appId,
			                                                          String deliveryTiming,
			                                                          BaseEmailDeliverySpecVo baseDelSpec,
			                                                          Subscribers subscribers ) {

		List<Subscription> subs = new ArrayList<Subscription>();

		if ( baseDelSpec != null && subscribers != null && subscribers.size() > 0 ) {
			
			for ( Subscriber subscriber : subscribers ) {
				
				if ( subscriber != null && StringUtils.isNotEmpty( subscriber.deliveryArgument() ) ) {
					
					DefaultSubscriptionImpl sub = new DefaultSubscriptionImpl(appId);
					sub.setDeliveryTiming(deliveryTiming);

					EmailDeliverySpecVo delSpec = new EmailDeliverySpecVo();
					delSpec.setFrom( baseDelSpec.getFrom() );
					delSpec.setSubject( baseDelSpec.getSubject() );
					delSpec.setContentType( baseDelSpec.getContentType() );
					delSpec.setAttachments( baseDelSpec.getAttachments() );
					delSpec.setTo( subscriber.deliveryArgument() );
					sub.setDeliverySpec( delSpec );

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
