package com.railinc.notifserv.rest.r2009v1.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.notifserv.restsvcs.r2009v1.Subscriptions;
import com.railinc.wembley.legacy.service.MailingListsService;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;

@Path("/subscriptions")
public class SubscriptionsRestService extends BaseServiceImpl {
	MailingListsService service;
	
	@GET
	@Path("/users/{realm}/{uid}")
	@Produces(CONTENTYPE_TEXT_XML)
	public Subscriptions mySubscriptions(@PathParam("realm") String realm, @PathParam("uid") String uid) {
		require("MailingListsService", getService());
		throwBadRequestIfNull(realm,"realm");
		throwBadRequestIfNull(uid, "uid");
		Subscriptions result = null;
		
		try {
			MailingListSubscriptions mine = getService().getMySubscriptions(realm, uid);
			MailingListsVo allMailingLists = getService().allMailingLists();
			result = new R2009V1Converter().convert(mine,allMailingLists);
		} catch (Exception e) {
			throw500(e);
		}
		return result;
	}
	
	
	
	
	
	
	public MailingListsService getService() {
		return service;
	}

	public void setService(MailingListsService service) {
		this.service = service;
	}

	@Override
	protected Logger getLog() {
		// Auto-generated method stub
		return log;
	}
	private Logger log = LoggerFactory.getLogger(SubscriptionsRestService.class);
}
