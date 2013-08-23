package com.railinc.wembley.legacy.domain.impl.sso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

public class SSORestClientImpl implements SSO {
	
	/**
	 * http://localhost:9080/SSOServices/sso/v1
	 */
	private String baseUrl = "http://datasvcs.dev.railinc.com/SSOServices/sso/v1";
	private NamespaceContext ctx;
	private SAXParserFactory saxParserFactory;
	private HttpClient httpClient;
	
	

	public SSORestClientImpl() {
		ctx = new NamespaceContext();
		ctx.register("http://resources.sso.railinc.com/v1","sso");
		
		saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setNamespaceAware(true);

	}
	

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public List<String> getModuleIds() {
		String url = getBaseUrl().concat("/modules?getRoles=false");
		return getListOfStrings(url, "moduleId");
	}

	public List<String> getAppsForUser(String uid) {
		String url = getBaseUrl().concat("/resources/user/").concat(uid);
		return getListOfStrings(url, "resourceId");
	}


	public List<String> getRolesForApp(String appName) {
		String url = getBaseUrl().concat("/modules/").concat(appName).concat("?getRoles=true");
		return getListOfStrings(url, "roleId");
	}


	public List<String> getRolesForUser(String uid) {
		String url = getBaseUrl().concat("/users/").concat(uid).concat("?getRoles=true").concat("&includeHierarchy=false");
		return getListOfStrings(url, "role");
	}
	
	public synchronized HttpClient getHttpClient() {
		if (this.httpClient == null) {
			this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		}
		return this.httpClient;
	}
	public synchronized void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}


	private List<String> getListOfStrings(String url, String elementName) {
		GetMethod getMethod = new GetMethod(url);
		InputStream responseBodyAsStream = null;
		try {
			int executeMethod = getHttpClient().executeMethod(getMethod);
			if (HttpServletResponse.SC_OK == executeMethod) {
				responseBodyAsStream = getMethod.getResponseBodyAsStream();

				SAXParser newSAXParser = saxParserFactory.newSAXParser();
				StringListHandler stringListHandler = new StringListHandler(elementName,false);
				newSAXParser.parse(responseBodyAsStream, stringListHandler);
				responseBodyAsStream.close();
				return stringListHandler.getValues();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(responseBodyAsStream);
			getMethod.releaseConnection();
		}	
		return new ArrayList<String>();
	}
	
	private List<SSOUserInfo> getListOfUsers(String url, String userEnvelopeElementName) {
		HttpClient httpClient = getHttpClient();
		GetMethod getMethod = new GetMethod(url);
		InputStream responseBodyAsStream = null;
		try {
			int executeMethod = httpClient.executeMethod(getMethod);
			if (HttpServletResponse.SC_OK == executeMethod) {
				responseBodyAsStream = getMethod.getResponseBodyAsStream();

				SAXParser newSAXParser = saxParserFactory.newSAXParser();
				SSORestUserSaxHandler stringListHandler = new SSORestUserSaxHandler(userEnvelopeElementName);
				newSAXParser.parse(responseBodyAsStream, stringListHandler);
				responseBodyAsStream.close();
				return stringListHandler.getUsers();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(responseBodyAsStream);
			getMethod.releaseConnection();
		}	
		return new ArrayList<SSOUserInfo>();
	}


	public List<SSOUserInfo> getUsersInRole(String roleName) {
		String url = getBaseUrl().concat("/users/roles/").concat(roleName);
		List<SSOUserInfo> listOfUsers = getListOfUsers(url, "user");
		List<SSOUserInfo> active = new ArrayList<SSOUserInfo>();
		for (SSOUserInfo u : listOfUsers) {
			if (null == u.getStatus() || "Active".equalsIgnoreCase(u.getStatus())) {
				active.add(u);
			}
		}
		return active;
	}

	public List<String> getPropertyForUser( String userId, String property ) {
		
		String url = getBaseUrl().concat("/users/").concat( userId );
		return getListOfStrings( url, property );
	}
	
	public List<SSOUserInfo> getAllActiveUsers(){
		String url = getBaseUrl().concat("/users");
		return getListOfUsers(url,"userEmail");
	}
}
