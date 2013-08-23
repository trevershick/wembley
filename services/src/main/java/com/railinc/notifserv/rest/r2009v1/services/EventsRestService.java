package com.railinc.notifserv.rest.r2009v1.services;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.notifserv.restsvcs.r2009v1.Event;
import com.railinc.wembley.legacy.service.NotificationServiceAdmin;
import com.railinc.wembley.legacysvc.domain.EventVo;

@Path("/events")
public class EventsRestService extends BaseServiceImpl {

	@GET
	@Path("/{uid}")
	@Produces(CONTENTYPE_TEXT_XML)
	public Event getEvent(@PathParam("uid") String uid) {
		EventVo e = null;
		try {
			e = getService().getEvent(uid);
		} catch (Exception ex) {
			throw500(ex);
		}
		throw404IfNull(e);
		return new R2009V1Converter().convert(e,true);
	}
/*
	@GET
	@Path("/{uid}/notifications")
	public Notifications getNotificationsForEvent(@PathParam("uid") String uid) {
		List<NotificationVo> e = null;
		try {
			e = getService().getNotificationsForEvent(uid);
		} catch (Exception ex) {
			throw500(ex);
		}
		throw404IfNull(e);
		return new R2009V1Converter().convert(e,true);
	}
	*/
	
	public NotificationServiceAdmin getService() {
		return service;
	}
	public void setService(NotificationServiceAdmin service) {
		this.service = service;
	}

	
	@Override
	protected Logger getLog() {
		return log;
	}
	private Logger log = LoggerFactory.getLogger(EventsRestService.class);
	

	private NotificationServiceAdmin service;
}
