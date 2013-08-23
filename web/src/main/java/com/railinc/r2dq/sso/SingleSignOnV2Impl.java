package com.railinc.r2dq.sso;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.trimToEmpty;
import static org.apache.commons.lang.StringUtils.trimToNull;

import java.beans.ExceptionListener;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.railinc.serviceability.monitoring.ComponentStatus;
import com.railinc.serviceability.monitoring.MonitoredComponent;


public class SingleSignOnV2Impl implements SingleSignOn, MonitoredComponent, SingleSignOnV2ImplMBean {
	public static final TimeUnit DEFAULT_RECHECK_AFTER_TIMEUNIT = TimeUnit.SECONDS;
	public static final long DEFAULT_RECHECK_AFTER_TIMEUNIT_VALUE = 30;
	
	
	private TimeUnit recheckAfterUnit = DEFAULT_RECHECK_AFTER_TIMEUNIT;
	private long recheckAfterDuration = DEFAULT_RECHECK_AFTER_TIMEUNIT_VALUE;

	private static final String MONITORED_COMPONENT_NAME = "ssoclient";

	private Logger logger = LoggerFactory.getLogger(SingleSignOnV2Impl.class);
	
	private HttpClient httpClient;
	private final SAXParserFactory factory;
	
	private String dataServicesUri;
	private String testResourcePath = "/rest/2.0/sso/applications/R2DQ";
	/**
	 * used to track when the service is down.
	 */
	private final AtomicBoolean down = new AtomicBoolean(false);
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
			new ThreadFactoryBuilder().setDaemon(true).setNameFormat("SingleSignOnClientPauser-%d").build());
	


	public String getTestResourcePath() {
		return testResourcePath;
	}

	public void setTestResourcePath(String testResourcePath) {
		this.testResourcePath = testResourcePath;
	}

	public String getDataServicesUri() {
		return dataServicesUri;
	}
	
	@Required
	public void setDataServicesUri(String dataServicesUri) {
		this.dataServicesUri = dataServicesUri;
	}

	public SingleSignOnV2Impl() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		this.factory = factory;
	}
	
	

	public Collection<String> groupsForUser(String userLogin) {
		return groupsForUser(userLogin,null);
	}
	
	@Override
	public Collection<String> groupsForUser(String userLogin, ExceptionListener orNull) {
		if (isBlank(userLogin)) { return Collections.emptyList(); }
		SingleSignOnUser user = getUser(userLogin, true, orNull);
		return Optional.fromNullable(user).or(new SingleSignOnUser()).roleIds();
	}

	public Collection<String> userLoginsByEmail(String email) {
		return userLoginsByEmail(email);
	}
	
	public Collection<String> usersInGroup(String groupId) {
		return usersInGroup(groupId,null);
	}
	
	@Override
	public Collection<String> usersInGroup(String groupId, ExceptionListener orNull) {
		Multimap<String, String> c = ArrayListMultimap.create();
		byRole(c, groupId);
		includeContactDetails(c);
		final Set<String> userLogins = Sets.newHashSet();
		Predicate<SingleSignOnUser> callback = new Predicate<SingleSignOnUser>() {
			@Override
			public boolean apply(SingleSignOnUser input) {
				userLogins.add(input.getId());return true;
			}
		};
		usersByCriteria(c, callback, orNull);
		return userLogins;
	}

	@Override
	public Collection<SingleSignOnUser> usersByEmail(String emailAddress, ExceptionListener orNull) {
		Multimap<String, String> c = ArrayListMultimap.create();
		byEmail(c, emailAddress);
		includeContactDetails(c);
		returnPermissions(c, false);
		final Set<SingleSignOnUser> users = Sets.newHashSet();
		Predicate<SingleSignOnUser> callback = new Predicate<SingleSignOnUser>() {
			@Override
			public boolean apply(SingleSignOnUser input) {
				users.add(input);return true;
			}};
		usersByCriteria(c, callback, orNull);
		return users;
	}

	@Override
	public SingleSignOnUser userByLogin(String userLogin, ExceptionListener orNull) {
		URI uri = null;
		try {
			uri = new URI(String.format("%s/rest/2.0/sso/users/%s?inc=contact", getDataServicesUri(), userLogin));
		} catch (URISyntaxException e) {
			notifyOfException(orNull,e);
			return null;
		}
		SingleSignOnUserXmlHandler handler = new SingleSignOnUserXmlHandler();
		getXml(uri, handler, orNull);
		return handler.user();
	}
	
	@Override
	public Collection<String> userLoginsByEmail(String email, ExceptionListener orNull) {
		Multimap<String, String> c = ArrayListMultimap.create();
		byEmail(c, email);
		includeContactDetails(c);
		returnPermissions(c, false);
		final Set<String> userLogins = Sets.newHashSet();
		Predicate<SingleSignOnUser> callback = new Predicate<SingleSignOnUser>() {
			@Override
			public boolean apply(SingleSignOnUser input) {
				userLogins.add(input.getId());return true;
			}
		};
		usersByCriteria(c, callback, orNull);
		return userLogins;
	}
	
	public SingleSignOnUser getUser(String loginId, boolean returnPermissions) {
		return getUser(loginId, returnPermissions,null);
	}	
	
	public SingleSignOnUser getUser(String loginId, boolean returnPermissions, ExceptionListener orNull) {
		URI uri = null;
		try {
			uri = new URI(String.format("%s/rest/2.0/sso/users/%s?inc=contact&inc=%s", getDataServicesUri(), loginId ,returnPermissions ? "permissions":""));
		} catch (URISyntaxException e) {
			notifyOfException(orNull,e);
			return null;
		}

		
		SingleSignOnUserXmlHandler handler = new SingleSignOnUserXmlHandler();
		getXml(uri, handler, orNull);
		return handler.user();
	}
	

	

	




	public synchronized HttpClient getHttpClient() {
		if(this.httpClient == null) {
			logger.debug("Creating default thread safe HttpClient");
			this.httpClient = new HttpClient();
			this.httpClient.setHttpConnectionManager(new MultiThreadedHttpConnectionManager());
		}
		
		return this.httpClient;
	}
	

	public synchronized void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public void markUp() {
		down.set(true);
	}

	public boolean isDown() {
		return down.get();
	}

	private void notifyOfException(ExceptionListener orNull, Exception e) {
		if (orNull != null) {
			orNull.exceptionThrown(e);
		}
	}

	private int getXml(URI uri, DefaultHandler handler,ExceptionListener listenerOrNull) {
		int responseCode = Integer.MIN_VALUE;
		if (down.get()) {
			return HttpServletResponse.SC_SERVICE_UNAVAILABLE;
		}
		try {
			logger.debug("GET {}", uri);
			GetMethod get = new GetMethod(uri.toString());
			get.setRequestHeader("Accept", "application/xml");
			responseCode = getHttpClient().executeMethod(get);
			logger.debug("GET {} Response Code {}", uri, responseCode);
			
			InputStream responseBodyAsStream = get.getResponseBodyAsStream();
			SAXParser parser = parser();
			parser.parse(responseBodyAsStream, handler);

			logger.debug("Returning {}", responseCode);
		} catch (UnknownHostException uhe) {
			down.set(true);
			this.executor.schedule(new Runnable() {
				@Override
				public void run() {
					down.set(false);
				}}, recheckAfterDuration, recheckAfterUnit);
			
			
		} catch (SAXAbortionException sae) {
			// this is ok, we're aborting from the sax handler
			// because more results are not wanted
			// see SingleSignOnHandler
		} catch (Exception e) {
			logger.error("An error occurred calling " + uri, e);
			notifyOfException(listenerOrNull,e);
		}
		return responseCode;
	}

	private SAXParser parser() {
		try {
			return factory.newSAXParser();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}



	@Override
	public String getName() {
		return MONITORED_COMPONENT_NAME;
	}

	@Override
	public ComponentStatus check() {
		if (isDown()) {
			return ComponentStatus.error("marked as down");
		}
		final AtomicReference<Exception> exceptionThrown = new AtomicReference<Exception>();
		ExceptionListener el = new ExceptionListener() {
			@Override
			public void exceptionThrown(Exception arg0) {
				exceptionThrown.set(arg0);
			}};
			
		URI uri = null;
		try {
			uri = new URI(String.format("%s%s", getDataServicesUri(), getTestResourcePath()));
			int i = getXml(uri, null, el);
			if (i != HttpServletResponse.SC_OK) {
				return ComponentStatus.error(uri + " returned " + i);
			}
			return ComponentStatus.ok();
		} catch (URISyntaxException e) {
			notifyOfException(el,e);
			return null;
		}
	}


	private void usersByCriteria(Multimap<String, String> params, Predicate<SingleSignOnUser> callback, ExceptionListener orNull) {
		URI uri = null;
		try {
			String uriFormat = "%s/rest/2.0/sso/search/users?";
			StringBuilder urisb = new StringBuilder(String.format(uriFormat, getDataServicesUri()));
			
			List<String> ps = newArrayList();
			for (String key : params.keySet()) {
				for (String v : params.get(key)) {
					try {
						ps.add(Joiner.on('=').join(key,URLEncoder.encode(v, "UTF-8")));
					} catch (UnsupportedEncodingException e) {
						notifyOfException(orNull, e);
					}
				}
			}
			
			String queryString = Joiner.on("&").join(ps);
			
			uri = new URI(urisb.append(queryString).toString());
		} catch (URISyntaxException e) {
			notifyOfException(orNull,e);
		}

		
		SingleSignOnUserXmlHandler handler = new SingleSignOnUserXmlHandler(callback);
		getXml(uri, handler, orNull);
	}

	private void byEmail(Multimap<String, String> c, String email) {
		if (isBlank(email)) {
			return;
		}
		c.put("eml", trimToEmpty(email));
	}

//	private void byMark(Multimap<String, String> c, String entity) {
//		if (isBlank(entity)) {
//			return;
//		}
//		// et = entity (or mark)
//		c.put("et", trimToEmpty(entity));
//	}

	private void byRole(Multimap<String, String> c, String roleId) {
		// prm = permission (assigned role)
		c.put("prm", roleId);
	}

	private void returnPermissions(Multimap<String, String> c, boolean onlyIf) {
		if (onlyIf) {
			returnPermissions(c);
		}
	}
	
	private void returnPermissions(Multimap<String, String> c) {
		c.put("inc","permissions");
	}
	
//	private void includeContactDetails(Multimap<String, String> c, boolean onlyIf) {
//		if (onlyIf) {
//			includeContactDetails(c);
//		}
//	}
	private void includeContactDetails(Multimap<String, String> c) {
		c.put("inc","contact");
	}

	@Override
	public String emailForUser(String userLogin, ExceptionListener orNull) {
		logger.debug("getting email for user {}", userLogin);
		SingleSignOnUser user = getUser(userLogin, false);
		return (user == null) ? null : trimToNull(user.getEmail());
	}

	public TimeUnit getRecheckAfterUnit() {
		return recheckAfterUnit;
	}

	public void setRecheckAfterUnit(TimeUnit recheckAfterUnit) {
		this.recheckAfterUnit = recheckAfterUnit;
	}

	public long getRecheckAfterDuration() {
		return recheckAfterDuration;
	}

	public void setRecheckAfterDuration(long recheckAfterDuration) {
		this.recheckAfterDuration = recheckAfterDuration;
	}

	@Override
	public String getRecheckUnit() {
		return this.recheckAfterUnit.name();
	}

	@Override
	public void setRecheckUnit(String value) {
		this.setRecheckAfterUnit(TimeUnit.valueOf(value));
	}

	

}
