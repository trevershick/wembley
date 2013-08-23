package com.railinc.wembley.legacy.subscriptions.loader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.AbstractMailinglistDeliverySpecVo;
import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.DeliverySpecs;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.MailinglistDeliverySpecVo;
import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacy.handlers.SubscriptionLoaderHandler;
import com.railinc.wembley.legacy.subscriptions.Subscription;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;

public class MailinglistSubscriptionLoaderImpl implements SubscriptionLoader {

	private static final Logger log = LoggerFactory.getLogger( MailinglistSubscriptionLoaderImpl.class );
	
	private String appId;
	private ApplicationDao applicationDao;
	private MailinglistSubscriptionResolver mailinglistSubResolver;
	private DeliverySpecValidator deliverySpecValidator;
	private List<SubscriptionLoaderHandler> subscriptionLoaderHandlers;

	public MailinglistSubscriptionLoaderImpl( String appId ) {
	
		this.appId = appId;
		log.info( String.format( "Instantiating MailinglistSubscriptionLoaderImpl with AppId %s", appId ) );
	}
	
	public List<Subscription> loadSubscriptions( Event event ) {
		
		List<Subscription> subscriptions = new ArrayList<Subscription>();

		if ( log.isDebugEnabled() ) {
			
			log.debug( String.format( "[%s] Loading subscriptions using the MailinglistSubscriptionLoaderImpl", this.appId ) );
			log.debug( "ApplicationDao = " + this.applicationDao );
		}
		
		DeliverySpecs deliverySpecs = event.getEventHeader().getDeliverySpecs();

		if ( deliverySpecs.getDeliverySpecs() != null) {
			
			for ( DeliverySpec deliverySpec : deliverySpecs.getDeliverySpecs() ) {
				
				if ( deliverySpec != null && deliverySpec instanceof MailinglistDeliverySpecVo ) {
					
					if ( this.deliverySpecValidator == null ||
						 this.deliverySpecValidator.isDeliverySpecValid( event.getEventHeader().getAppId(), event, deliverySpec ) ) {
						
						if (log.isDebugEnabled()) {
							log.debug( String.format( "[%s] Creating a subscription for the Delivery Spec %s with an Event App Id of %s",
													  appId, deliverySpec, event.getEventHeader().getAppId() ) );
						}
						
						subscriptions.addAll( createSubscriptions( event, (MailinglistDeliverySpecVo) deliverySpec ) );
					}
					else {
						
						invokeSubLoaderHandlers( event, null, null );
						log.warn( String.format( "Received an invalid delivery spec, no subscriptions loaded: %s", deliverySpec ) );
					}
				}
			}			
		}

		if (log.isDebugEnabled()) {
			log.debug( String.format( "[%s] Loaded %d subscription(s)", this.appId, subscriptions.size() ) );
		}

		return subscriptions;
	}

	private List<Subscription> createSubscriptions( Event event, MailinglistDeliverySpecVo mailinglistDelSpec ) {

		List<Subscription> subscriptions = new ArrayList<Subscription>();
		String deliveryTiming = null;
		ApplicationVo app = null;

		
		
		if (null == mailinglistDelSpec.getMailingLists() || mailinglistDelSpec.getMailingLists().size() == 0) {
			return subscriptions;
		}
		
		
		String fromAddress = mailinglistDelSpec.getFrom();
		
		
			
			if (applicationDao != null) {
				app = applicationDao.getApplication( event.getEventHeader().getAppId() );
			}

			deliveryTiming = app != null ? app.getDefaultDeliveryTiming() : "0";
			
			MailingListSubscriptions allMailingListSubscriptions = new MailingListSubscriptions();
			
			for ( AbstractMailinglistDeliverySpecVo theMailingList : mailinglistDelSpec.getMailingLists() ) {
				// 'theMailingList' as an example is Application Mailinglist: Application=EMIS, Type=OUTAGE (from the XML)
				// for each one we need to get the mailinglist and then the mailinglist subscriptions
				MailingListsVo mailinglists = mailinglistSubResolver.getMailingLists( theMailingList );

				// ensure we have a 'fromAddress'
				for (MailingListVo v : mailinglists) {
					if (fromAddress == null || fromAddress.length() == 0) {
						fromAddress = v.getFromAddress();
					}
				}
				
				MailingListSubscriptions mailinglistSubs =
					mailinglistSubResolver.getMailingListSubscriptions( mailinglists );
				allMailingListSubscriptions.addAll(mailinglistSubs);
			}
			
			
			if ( allMailingListSubscriptions.size() > 0 ) {
				subscriptions.addAll(
						mailinglistSubResolver.createSubscriptions( event.getEventHeader().getAppId(),
								                                    deliveryTiming,
								                                    createNewMailinglistDelSpec( mailinglistDelSpec, fromAddress ),
								                                    allMailingListSubscriptions ) );
				// do we need to do this?
				invokeSubLoaderHandlers( event, subscriptions, mailinglistDelSpec );
			}				
	

		return subscriptions;
	}	
	
	protected void invokeSubLoaderHandlers( Event event, List<Subscription> subs, MailinglistDeliverySpecVo mailinglist ) {
		if (subscriptionLoaderHandlers == null || subscriptionLoaderHandlers.isEmpty()) {
			return;
		}
	
		String subInfo = String.format( "Requested Mailinglist: %s - Returned %s", mailinglist, subs );
		
		for(SubscriptionLoaderHandler handler : this.subscriptionLoaderHandlers) {
			
			if ( handler != null ) {
				
				try {
					handler.handleSubscriptionsLoaded( this.getClass(), event, subs, subInfo );
				}
				catch ( Throwable e ) {
					log.error( "Error trying to invoke Subscription Loader Handler", e );
				}
			}
		}
	}

	protected MailinglistDeliverySpecVo createNewMailinglistDelSpec( MailinglistDeliverySpecVo mailinglistDelSpec,
																	 String fromAddress ) {
		
		MailinglistDeliverySpecVo newMlDs = new MailinglistDeliverySpecVo();
		
		newMlDs.setAttachments( mailinglistDelSpec.getAttachments() );
		newMlDs.setContentType( mailinglistDelSpec.getContentType() );
		newMlDs.setMailingLists( mailinglistDelSpec.getMailingLists() );
		newMlDs.setSubject( mailinglistDelSpec.getSubject() );
		newMlDs.setFrom( mailinglistDelSpec.getFrom() );
		
		if ( mailinglistDelSpec.getFrom() == null || mailinglistDelSpec.getFrom().length() == 0 ) {
			newMlDs.setFrom(fromAddress);
		}
		
		return newMlDs;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setDeliverySpecValidator(DeliverySpecValidator deliverySpecValidator) {
		this.deliverySpecValidator = deliverySpecValidator;
	}
	
	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public void setMailinglistSubResolver(
			MailinglistSubscriptionResolver mailinglistSubResolver) {
		this.mailinglistSubResolver = mailinglistSubResolver;
	}

	public void setSubscriptionLoaderHandlers(List<SubscriptionLoaderHandler> subscriptionLoaderHandlers) {
		this.subscriptionLoaderHandlers = subscriptionLoaderHandlers;
	}
}
