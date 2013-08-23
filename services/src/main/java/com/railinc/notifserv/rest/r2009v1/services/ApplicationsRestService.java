package com.railinc.notifserv.rest.r2009v1.services;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.notifserv.restsvcs.r2009v1.Application;
import com.railinc.notifserv.restsvcs.r2009v1.Applications;
import com.railinc.notifserv.restsvcs.r2009v1.Event;
import com.railinc.notifserv.restsvcs.r2009v1.EventStatus;
import com.railinc.notifserv.restsvcs.r2009v1.SearchResults;
import com.railinc.wembley.legacy.service.NotificationSearchResults;
import com.railinc.wembley.legacy.service.NotificationServiceAdmin;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;
import com.railinc.wembley.legacysvc.domain.EventVo;
/*
 * Resource	Method	Representation	Status Codes
Employee	GET	Employee Format	200, 301, 410
Employee	PUT	Employee Format	200, 301, 400, 410
Employee	DELETE	N/A	200, 204
All Employees	GET	Employee List Format	200, 301
All Employees	POST	Employee Format	201, 400
 */
@Path("/applications")
public class ApplicationsRestService extends BaseServiceImpl {

	/**
	 * search for notifications
	 * @throws 409 there are no notifications that can be cancelled
	 * @param applicationId
	 * @param cId
	 * @return
	 */
	@DELETE
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/{applicationId}/event/{correlationId}/notifications")
	public Response cancelNotifications(
			@PathParam(PARAM_APPLICATION_ID) String applicationId,
			@PathParam(PARAM_CORRELATION_ID) String cId) {

		
		EventStatus eventStatus = getEventStatus(applicationId, cId);
		if (eventStatus.getTotalNotificationsRemaining() == 0) {
			throw409Conflict("All notifications are processed");
		}
			
		try {
			getService().cancelNotifications(eventStatus.getEventUid());
		} catch (WebApplicationException wae) {
			throw wae;
		} catch (Exception e) {
			throw500(e);
		}

		return Response.ok().build();
		
	}
	@POST
	@Consumes(CONTENTYPE_TEXT_XML)
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/")
	public Response createApplication(Application application) {
		if (null == application.getAdminEmail()) {
			throw400("adminEmail is required");
		}
		if (null == application.getAppId()) {
			throw400("applicationId is required");
		}
		if (null != getService().getApplication(application.getAppId())) {
			throw400("applicationId is already used");
		}
		if (null == application.getDefaultDeliveryTiming()) {
			application.setDefaultDeliveryTiming("0");
		}
		application.setCreated(Calendar.getInstance());

		ApplicationVo vo = new R2009V1Converter().convert(application);
		try {
			vo = getService().createApplication(vo);
		} catch (IllegalArgumentException iae) {
			throw400(iae.getMessage());
		}
		return Response.status(HttpServletResponse.SC_CREATED).build();
	}
	@DELETE
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/{applicationId}")
	public Response deleteApplication(@PathParam(PARAM_APPLICATION_ID) String applicationId) {
		if (null == applicationId) {
			throw400("applicationId is required");
		}
		ApplicationVo a = getService().getApplication(applicationId);
		if ( null == a ) {
			throw400("invalid application id");
		}
		try {
			getService().deleteApplication(applicationId);
		} catch (IllegalArgumentException iae) {
			throw400(iae.getMessage());
		}
		
		return Response.status(HttpServletResponse.SC_OK).build();
	}
	
	
	@GET
	@Path("/")
	@Produces(CONTENTYPE_TEXT_XML)
	public Applications getAllApplications() {
		List<ApplicationVo> allApplications = null;
		try {
			allApplications = getService().getAllApplications();
		} catch (Exception e) {
			throw500(e);
		}
		return new R2009V1Converter().convertApplications(allApplications);
	}

	/**
	 * @param applicationId
	 * @return null if not found, the app otherwise
	 */
	@GET
	@Path("/{applicationId}")
	@Produces(CONTENTYPE_TEXT_XML)
	public Application getApplication(
			@PathParam(PARAM_APPLICATION_ID) String applicationId) {
		ApplicationVo application = null;
		try {
			application = getService().getApplication(applicationId);
		} catch (Exception e) {
			throw500(e);
		}
		throw404IfNull(application);
		return new R2009V1Converter().convert(application);
	}
	
	@GET
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/{applicationId}/event/{correlationId}")
	public Event getEventByCorrelationId(
			@PathParam(PARAM_APPLICATION_ID) String applicationId,
			@PathParam(PARAM_CORRELATION_ID) String cId) {
		
		EventVo eventByCorrelationId = null;

		try {
			eventByCorrelationId = getService().getEventByCorrelationId(applicationId, cId);
		} catch (Exception e) {
			throw500(e);
		}
		throw404IfNull(eventByCorrelationId);
		return new R2009V1Converter().convert(eventByCorrelationId, true);
	}
	
	@GET
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/{applicationId}/event/{correlationId}/status")
	public EventStatus getEventStatus(
			@PathParam(PARAM_APPLICATION_ID) String applicationId,
			@PathParam(PARAM_CORRELATION_ID) String cId) {
		
		EventVo ev = null;
		Map<String, Integer> statusCountsByEventId = null;
		try {
			ev = getService().getEventByCorrelationId(applicationId, cId);
			throw404IfNull(ev);
			statusCountsByEventId = getService().getStatusCountsByEventId(ev.getEventUid());
		} catch (WebApplicationException wae) {
			throw wae;
		} catch (Exception e) {
			throw500(e);
		}

		return new R2009V1Converter().convert(ev, statusCountsByEventId);
	}

	public NotificationServiceAdmin getService() {
		require("NotificationServiceAdmin",service);
		return service;
	}
	
	/**
	 * search for notifications
	 * @param applicationId
	 * @param cId
	 * @return
	 */
	@GET
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/{applicationId}/event/{correlationId}/notifications/search")
	public SearchResults searchNotifications(
			@PathParam(PARAM_APPLICATION_ID) String applicationId,
			@PathParam(PARAM_CORRELATION_ID) String cId,
			@QueryParam("ds") String ds,
			@QueryParam("token") String token) {
		int maximum = DEFAULT_NOTIFICATION_SEARCH_MAX_RESULTS;
		EventVo eventByCorrelationId = null;
		NotificationSearchResults res = null;
		
		try {
			eventByCorrelationId = getService().getEventByCorrelationId(applicationId, cId);
			throw404IfNull(eventByCorrelationId);
			res = getService().findNotificationsByDeliverySpec(
					eventByCorrelationId.getEventUid(),
					ds, token, maximum);
		} catch (WebApplicationException wae) {
			throw wae;
		} catch (Exception e) {
			throw500(e);
		}

		return new R2009V1Converter().convertNotificationSearchResults(eventByCorrelationId, res);
	}
	
	public void setService(NotificationServiceAdmin service) {
		this.service = service;
	}
	
	
	@PUT
	@Consumes(CONTENTYPE_TEXT_XML)
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/{applicationId}")
	public com.railinc.notifserv.restsvcs.r2009v1.Application updateApplication(@PathParam(PARAM_APPLICATION_ID) String applicationId, Application application) {
		ApplicationVo app = getService().getApplication(applicationId);
		throw404IfNull(app);
		
		if (null == applicationId) {
			throw400("applicationId is required");
		}
		// load the app, does it exist?
		// 	update only those attributes specified

		if (null == application.getAppId()) {
			throw400("application is required");
		}
		if (null == application.getAppId()) {
			application.setAppId(applicationId);
		}
		if (null != application.getAdminEmail()) {
			app.setAdminEmail(application.getAdminEmail());
		}
		if (null != application.getDefaultDeliveryTiming()) {
			app.setDefaultDeliveryTiming(application.getDefaultDeliveryTiming());
		}
		if (!app.getAppId().equals(applicationId) && null !=  getService().getApplication(app.getAppId())) {
			throw400("applicationId is already in use.");
		}
		try {
			getService().updateApplication(applicationId, app);
		} catch (IllegalArgumentException iae) {
			throw400(iae.getMessage());
		}
		return new R2009V1Converter().convert(app);
	}

	
	
	@Override
	protected Logger getLog() {
		return log;
	}
		
	private static final String PARAM_CORRELATION_ID = "correlationId";

	private static final String PARAM_APPLICATION_ID = "applicationId";

	private static final int DEFAULT_NOTIFICATION_SEARCH_MAX_RESULTS = 5;

	private NotificationServiceAdmin service;
	private Logger log = LoggerFactory.getLogger(ApplicationsRestService.class);
}
