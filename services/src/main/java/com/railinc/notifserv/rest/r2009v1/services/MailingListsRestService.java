package com.railinc.notifserv.rest.r2009v1.services;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.notifserv.restsvcs.r2009v1.MailingList;
import com.railinc.notifserv.restsvcs.r2009v1.MailingLists;
import com.railinc.notifserv.restsvcs.r2009v1.Subscription;
import com.railinc.notifserv.restsvcs.r2009v1.Subscriptions;
import com.railinc.wembley.legacy.service.MailingListsService;
import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;
import com.railinc.wembley.legacysvc.domain.Subscribers;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

@Path("/mailinglists")
public class MailingListsRestService extends BaseServiceImpl {

	
	
	private static final String MAILING_LISTS_SERVICE = "MailingListsService";
	
	
	
	@Resource
	public void setContext(WebServiceContext context) {

	}

	@GET
	@Path("/")
	@Produces(CONTENTYPE_TEXT_XML)
	public MailingLists getAllMailingLists() {
		MailingLists result = null;
		require(MAILING_LISTS_SERVICE, getService());
		try {
			MailingListsVo allMailingLists = getService().allMailingLists();
			result = new R2009V1Converter().convert(allMailingLists);
		} catch (Exception e) {
			throw500(e);
		}
		return result;
	}

	@GET
	@Path("/unsubscribed/{realm}/{uid}")
	@Produces(CONTENTYPE_TEXT_XML)
	public MailingLists getUnsubscribed(@PathParam("realm") String realm,
			@PathParam("uid") String uid) {
		MailingLists result = null;
		require(MAILING_LISTS_SERVICE, getService());
		try {
			MailingListsVo allMailingLists = getService()
					.getMailingListsNotSubscribedTo(realm, uid);
			result = new R2009V1Converter().convert(allMailingLists);
		} catch (Exception e) {
			throw500(e);
		}
		return result;
	}

	@GET
	@Path("/{mailingListKey}/subscribers")
	@Produces(CONTENTYPE_TEXT_XML)
	public com.railinc.notifserv.restsvcs.r2009v1.Subscribers getSubscribers(
			@PathParam("mailingListKey") String mailingListKey) {
		require(MAILING_LISTS_SERVICE, getService());

		com.railinc.notifserv.restsvcs.r2009v1.Subscribers result = null;
		getMailingList(mailingListKey);

		try {
			Subscribers ss = getService().getSubscribersToMailingList(
					mailingListKey);
			result = new R2009V1Converter().convert(ss);
		} catch (Exception e) {
			throw500(e);
		}
		return result;
	}

	@GET
	@Path("/{mailingListKey}")
	@Produces(CONTENTYPE_TEXT_XML)
	public MailingList getMailingList(
			@PathParam("mailingListKey") String mailingListKey) {
		MailingListVo result = null;

		try {
			result = getService().getMailingList(mailingListKey);
		} catch (Exception e) {
			throw500(e);
		}
		throw404IfNull(result);
		return new R2009V1Converter().convert(result);
	}

	@POST
	@Consumes(CONTENTYPE_TEXT_XML)
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/")
	public Response createMailingList(MailingList ml) {
		if (null == ml.getKey()) {
			ml.setKey(UUID.randomUUID().toString());
		}
		if (null != getService().getMailingList(ml.getKey())) {
			throw400(ml.getKey() + " is already in use");
		}

		if (null == ml.getShortName()) {
			throw400("shortName is required");
		}
		if (null == ml.getTitle()) {
			ml.setTitle(ml.getShortName());
		}
		if (null == ml.getFromAddress()) {
			throw400("fromAddress is required");
		}
		if (null == ml.getDescription()) {
			ml.setDescription("No Description");
		}
		ml.setActive(true);

		MailingListVo vo = new R2009V1Converter().convert(ml);
		vo = getService().createMailingList(vo);
		MailingList convert = new R2009V1Converter().convert(vo);
		return Response.status(HttpServletResponse.SC_CREATED).entity(convert)
				.build();
	}

	@PUT
	@Consumes(CONTENTYPE_TEXT_XML)
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/{mailingListKey}")
	public Response updateMailingList(
			@PathParam("mailingListKey") String mailingListKey, MailingList ml) {
		MailingListVo v = getService().getMailingList(mailingListKey);
		
		if (null == mailingListKey) {
			throw400("mailingListKey is required");
		}
		// load the app, does it exist?
		// update only those attributes specified

		if (null != ml.getApplication()) {
			v.setApplication(ml.getApplication());
		}

		if (null != ml.getDescription()) {
			v.setDescription(ml.getDescription());
		}

		if (null != ml.getFromAddress()) {
			v.setFromAddress(ml.getFromAddress());
		}

		if (null != ml.getKey()) {
			v.setKey(ml.getKey());
		}
		if (null != ml.getShortName()) {
			v.setShortName(ml.getShortName());
		}
		if (null != ml.getTitle()) {
			v.setTitle(ml.getTitle());
		}

		if (null != ml.getType()) {
			v.setType(ml.getType());
		}
		if (null != ml.isActive()) {
			v.setActive(ml.isActive().booleanValue());
		}

		MailingListVo vo = getService().updateMailingList(mailingListKey, v);
		MailingList convert = new R2009V1Converter().convert(vo);
		return Response.ok().entity(convert).build();
	}

	@DELETE
	@Produces(CONTENTYPE_TEXT_XML)
	@Path("/{mailingListKey}")
	public Response deleteMailingList(
			@PathParam("mailingListKey") String mailingListKey) {
		if (null == mailingListKey) {
			throw400("mailingListKey is required");
		}
		MailingListVo a = getService().getMailingList(mailingListKey);
		if (null == a) {
			throw400("invalid mailing list id");
		}
		getService().deleteMailingList(mailingListKey);
		return Response.status(HttpServletResponse.SC_OK).build();
	}

	@DELETE
	@Path("/{mailingListKey}/subscription/{realm}/{uid}")
	@Produces(CONTENTYPE_TEXT_XML)
	public Response unsubcribe(
			@PathParam("mailingListKey") String mailingListKey,
			@PathParam("realm") String realm, @PathParam("uid") String uid) {
		try {
			getService().unsubscribeMe(realm, uid, mailingListKey);
		} catch (Exception e) {
			throw500(e);
		}
		return Response.ok().build();
	}

	@POST
	@Path("/{mailingListKey}/subscription/{realm}/{uid}")
	@Produces(CONTENTYPE_TEXT_XML)
	public Response subcribe(
			@PathParam("mailingListKey") String mailingListKey,
			@PathParam("realm") String realm, @PathParam("uid") String uid) {
		try {
			getService().subscribeMe(realm, uid, mailingListKey);
		} catch (Exception e) {
			throw500(e);
		}
		return Response.status(HttpServletResponse.SC_CREATED).build();
	}

	@GET
	@Path("/{mailingListKey}/subscriptions")
	@Produces(CONTENTYPE_TEXT_XML)
	public Subscriptions subscriptionsForMailingList(
			@PathParam("mailingListKey") String mailingListKey) {
		MailingListSubscriptions mls = getService()
				.getMailingListSubscriptions(mailingListKey);
		return new R2009V1Converter().convert(mls);
	}

	@POST
	@Path("/{mailingListKey}/subscriptions")
	@Produces(CONTENTYPE_TEXT_XML)
	public Response createSubscription(
			@PathParam("mailingListKey") String mailingListKey,
			Subscription newSub) {
		MailingListSubscription newlyCreated = null;
		Subscription returnValue = null;

		MailingListVo mailingList = getService().getMailingList(mailingListKey);
		if (null == mailingList) {
			throw400("No mailing list was found with the key : " + mailingListKey);
		}

		
		if (newSub.getMode() == null) {
			newSub.setMode(SubscriptionMode.INCLUSION.toString());
		}

		
		if (null == newSub.getType()) {
			throw400("'type' is a required element");
		}
		
		RESTSupportedSubscriptionTypes type = null;
		try {
			type = RESTSupportedSubscriptionTypes.valueOf(newSub.getType());
		} catch (IllegalArgumentException iae) {
			throw400("Invalid type specified, must be one of " + ArrayUtils.toString(RESTSupportedSubscriptionTypes.values()));
		}

		SubscriptionMode mode = null;
		try {
			mode = SubscriptionMode.valueOf(newSub.getMode());
		} catch (IllegalArgumentException iae) {
			throw400("Invalid mode specified, must be one of " + ArrayUtils.toString(SubscriptionMode.values()));
		}

		
		try {
			switch (type) {
				case ssousers:
					newlyCreated = getService().createSSOUsersSubscription(mailingListKey);
					break;
				case ssouser:
					String ssoUser = newSub.getTypeArgument();
					if (StringUtils.isBlank(ssoUser)) {
						throw400("The 'typeArgument' element is required for 'ssouser' type");
					}
					newlyCreated = getService().createSSOUserSubscription(mailingListKey, ssoUser, mode);
					break;
				case basicuser:
					if (null == newSub.getDelivery()) {
						throw400("The 'delivery' element is required for 'basicuser'");
					}
					String email = newSub.getDelivery().getParam();

					if (StringUtils.isBlank(email)) { throw400("The 'delivery/param' element is required for 'basicuser' type"); }
					newlyCreated = getService().createBasicUserSubscription(mailingListKey, email, mode);
					break;
				case ssorole:
					String ssoRole = newSub.getTypeArgument();
					if (StringUtils.isBlank(ssoRole)) {
						throw400("The 'typeArgument' element is required for 'ssorole' type");
					}
					newlyCreated = getService().createSSORoleSubscription(mailingListKey, ssoRole, mode);
					break;
				case ssoapp:
					String ssoapp = newSub.getTypeArgument();
					if (StringUtils.isBlank(ssoapp)) { throw400("The 'typeArgument' element is required for 'ssoapp' type"); }
					newlyCreated = getService().createSsoAppSubscription(mailingListKey, ssoapp, mode);
					break;
				default:
					// we already checked the type, this is pretty much impossible unless someone changes the enum
			}
		} catch (WebApplicationException wae) {
			throw wae;
		} catch (Exception e) {
			throw500(e);
		}
		
//		URI build = info.getAbsolutePathBuilder().path(newlyCreated.key()).build(mailingListKey);
		returnValue = new R2009V1Converter().convert(newlyCreated);
//		returnValue.setXlinkType(TypeType.SIMPLE);
//		returnValue.setXlinkHref(build.toString());
		return Response.status(Status.CREATED).entity(returnValue).build();
	}
	
	
	
	@GET
	@Path("/{mailingListKey}/subscriptions/{subscriptionKey}")
	@Produces(CONTENTYPE_TEXT_XML)
	public Response getSubscription(
			@PathParam("mailingListKey") String mailingListKey,
			@PathParam("subscriptionKey") String subscriptionKey,
			@Context UriInfo info) {
		MailingListSubscription subscription = getService().getSubscription(subscriptionKey);
		throw404IfNull(subscription);


		Subscription s = new R2009V1Converter().convert(subscription);		
		return Response.status(Status.OK).entity(s).build();
	}

	
	
	

	@DELETE
	@Path("/{mailingListKey}/subscriptions/{subscriptionKey}")
	@Produces("text/xml")
	public Response deleteSubscription(
			@PathParam("mailingListKey") String mailingListKey,
			@PathParam("subscriptionKey") String subscriptionKey) {
		MailingListSubscription sub = getService().getSubscription(
				subscriptionKey);
		throw404IfNull(sub);
		if (!mailingListKey.equals(sub.getMailingListKey())) {
			throw400("Invalid subscription key for this mailing list");
		}
		boolean deleted = getService().deleteSubscription(subscriptionKey);
		return Response
				.status(deleted ? Status.OK : Status.PRECONDITION_FAILED)
				.build();
	}

	@Override
	protected Logger getLog() {
		return this.log;
	}

	public MailingListsService getService() {
		return mailingListsService;
	}

	public void setService(MailingListsService mailingListsService) {
		this.mailingListsService = mailingListsService;
	}

	private Logger log = LoggerFactory.getLogger(MailingListsRestService.class);
	private MailingListsService mailingListsService;
}
