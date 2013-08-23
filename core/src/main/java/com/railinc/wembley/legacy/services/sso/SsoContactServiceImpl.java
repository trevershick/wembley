package com.railinc.wembley.legacy.services.sso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.core.NotificationServiceRemoteException;

public class SsoContactServiceImpl implements SsoContactService {

	private static final Logger log = LoggerFactory.getLogger(SsoContactServiceImpl.class);

	private static final String SSO_USER_ROLE_WEB_SERVICE_URL_PROP_KEY = "ssoUserRoleServiceUrl";

	private static final String SSO_USER_ROLE_AND_MARK_WEB_SERVICE_URL_PROP_KEY = "ssoUserRoleAndMarkServiceUrl";

	private static final String SSO_USER_ID_WEB_SERVICE_URL_PROP_KEY = "ssoUserIdServiceUrl";

	private static final String SSO_XML_NAMESPACE_PROP_KEY = "ssoXmlNamespace";

	private String appId;

	private HttpClient rsClient;

	private Properties notifServProperties;

	private XMLInputFactory xmlInputFactory;

	public SsoContactServiceImpl(String appId, HttpClient rsClient) {

		this.appId = appId;
		this.rsClient = rsClient;
		this.xmlInputFactory = XMLInputFactory.newInstance();
		log.info(String.format("Instantiating the SsoContactServiceImpl with AppId %s and rest client %s", appId, rsClient));
	}

	public SsoContactResponse getSsoContactByRole(SsoContactRequest request) {

		SsoContactResponse response = null;

		if (request != null && request.getRole() != null && request.getRole().length() > 0) {
			String url = getSSOUserRoleWebServiceUrl();
			if (rsClient == null) {
				log.error(String.format("[%s] SsoContactServiceImpl is missing the HttpClient. " + "Notifications for SSO contacts cannot be delivered!", appId));
				throw new NotificationServiceRemoteException("SsoContactServiceImpl is missing the required dependencies");
			}

			if (url == null || url.length() == 0) {
				log.error(String.format("[%s] SsoContactServiceImpl SSO Role URL not set. Notifications for SSO " + "contacts cannot be delivered!", appId));
				throw new NotificationServiceRemoteException("SsoContactServiceImpl SSO Role URL is not defined");
			}

			String completeUrl = String.format(url, request.getRole());
			GetMethod roleRequestGetMethod = new GetMethod(completeUrl);

			try {
				int statusCode = rsClient.executeMethod(roleRequestGetMethod);
				if (statusCode == HttpStatus.SC_OK) {
					response = parseResponse(roleRequestGetMethod.getResponseBodyAsStream());
					if (log.isDebugEnabled()) {
						log.debug(String.format("SsoContacts Response: %s", response));
					}
				} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
					log.warn(String.format("[%s] SsoContactServiceImpl received status %d for call to URL %s. " + "Notifications for SSO contacts cannot be delivered!", appId, statusCode, completeUrl));
				} else {
					log.error(String.format("[%s] SsoContactServiceImpl received status %d for call to URL %s. " + "Notifications for SSO contacts cannot be delivered!", appId, statusCode, completeUrl));
					throw new NotificationServiceRemoteException(String.format("SsoContactServiceImpl received status %d", statusCode));
				}
			} catch (IOException ioe) {
				throw new NotificationServiceRemoteException("IOException trying to invoke SSO Services", ioe);
			} finally {
				if (roleRequestGetMethod != null) {
					roleRequestGetMethod.releaseConnection();
				}
			}
		} else {
			log.warn(String.format("[%s] SsoContactServiceImpl was called with an invalid request [%s]. " + "No notifications will be delivered", appId, request));
		}

		return response;
	}

	public SsoContactResponse getSsoContactByRoleAndMark(SsoContactRequest request) {

		SsoContactResponse response = null;

		if (request != null && request.getRole() != null && request.getRole().length() > 0 && request.getMark() != null && request.getMark().length() > 0) {

			String url = getSSOUserRoleAndMarkWebServiceUrl();

			if (rsClient == null) {
				log.error(String.format("[%s] SsoContactServiceImpl is missing the HttpClient.  " + "Notifications for SSO contacts cannot be delivered!", appId));
				throw new NotificationServiceRemoteException("SsoContactServiceImpl is missing the required dependencies");
			}

			if (url == null || url.length() == 0) {
				log.error(String.format("[%s] SsoContactServiceImpl SSO Role And Mark URL not set. Notifications for SSO " + "contacts cannot be delivered!", appId));
				throw new NotificationServiceRemoteException("SsoContactServiceImpl SSO Role and Mark URL is not defined");
			}

			String completeUrl = String.format(url, request.getRole(), request.getMark());
			GetMethod roleAndMarkRequestGetMethod = new GetMethod(completeUrl);

			try {
				int statusCode = rsClient.executeMethod(roleAndMarkRequestGetMethod);

				if (statusCode == HttpStatus.SC_OK) {
					response = parseResponse(roleAndMarkRequestGetMethod.getResponseBodyAsStream());
					if (log.isDebugEnabled()) {
						log.debug(String.format("SsoContacts Response: %s", response));
					}
				} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
					log.warn(String.format("[%s] SsoContactServiceImpl received status %d for call to URL %s. " + "Notifications for SSO contacts cannot be delivered!", appId, statusCode, completeUrl));
				} else {
					log.error(String.format("[%s] SsoContactServiceImpl received status %d for call to URL %s. " + "Notifications for SSO contacts cannot be delivered!", appId, statusCode, completeUrl));
					throw new NotificationServiceRemoteException(String.format("SsoContactServiceImpl received status %d", statusCode));
				}

			} catch (IOException ioe) {
				throw new NotificationServiceRemoteException("IOException trying to invoke SSO Services", ioe);
			} finally {
				if (roleAndMarkRequestGetMethod != null) {
					roleAndMarkRequestGetMethod.releaseConnection();
				}
			}
		} else {
			log.warn(String.format("[%s] SsoContactServiceImpl was called with an invalid request [%s]. " + "No notifications will be delivered", appId, request));
		}

		return response;
	}

	public SsoContactResponse getSsoContactByUserId(SsoContactRequest request) {

		SsoContactResponse response = null;

		if (request != null && request.getUserId() != null && request.getUserId().length() > 0) {
			String url = getSSOUserIdWebServiceUrl();

			if (rsClient == null) {
				log.error(String.format("[%s] SsoContactServiceImpl is missing the HttpClient.  " + "Notifications for SSO contacts cannot be delivered!", appId));
				throw new NotificationServiceRemoteException("SsoContactServiceImpl is missing the required dependencies");
			}

			if (url == null || url.length() == 0) {
				log.error(String.format("[%s] SsoContactServiceImpl SSO User ID URL not set. " + "Notifications for SSO contacts cannot be delivered!", appId));
				throw new NotificationServiceRemoteException("SsoContactServiceImpl SSO User ID URL is not defined");
			}

			String completeUrl = String.format(url, request.getUserId());
			GetMethod userRequestGetMethod = new GetMethod(completeUrl);

			try {

				int statusCode = rsClient.executeMethod(userRequestGetMethod);
				if (statusCode == HttpStatus.SC_OK) {
					response = parseResponse(userRequestGetMethod.getResponseBodyAsStream());

					if (log.isDebugEnabled()) {
						log.debug(String.format("SsoContacts Response: %s", response));
					}

				} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
					log.warn(String.format("[%s] SsoContactServiceImpl received status %d for call to URL %s. " + "Notifications for SSO contacts cannot be delivered!", appId, statusCode, completeUrl));
				} else {
					log.error(String.format("[%s] SsoContactServiceImpl received status %d for call to " + "URL %s. Notifications for SSO contacts cannot be delivered!", appId, statusCode, completeUrl));
					throw new NotificationServiceRemoteException(String.format("SsoContactServiceImpl received status %d", statusCode));
				}

			} catch (IOException ioe) {
				throw new NotificationServiceRemoteException("IOException trying to invoke SSO Services", ioe);
			} finally {
				if (userRequestGetMethod != null) {
					userRequestGetMethod.releaseConnection();
				}
			}
		} else {
			log.warn(String.format("[%s] SsoContactServiceImpl was called with an invalid request [%s]. No notifications will be delivered", appId, request));
		}
		
		return response;
	}

	public String getAppId() {
		return appId;
	}

	public void setNotifServProperties(Properties notifServProperties) {
		this.notifServProperties = notifServProperties;
	}

	private SsoContactResponse parseResponse(InputStream inputStream) {

		String namespace = getSSOXmlNamespace();

		log.debug(String.format("Parsing SsoContact Service Response with namespace '%s'", namespace));

		SsoContactResponseMsg response = null;
		List<SsoContact> contacts = new ArrayList<SsoContact>();

		try {

			XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

			while (reader.hasNext()) {

				reader.next();

				if (reader.isStartElement()) {

					if (reader.getNamespaceURI().equals(namespace)) {

						if (reader.getLocalName().equals("user")) {

							SsoContact contact = parseContact(reader, namespace);
							contacts.add(contact);
						}
					}
				}
			}

			reader.close();

			if (contacts.size() > 0) {

				response = new SsoContactResponseMsg();
				response.setContacts(contacts);
			}
		} catch (XMLStreamException e) {

			log.error("Error parsing SsoContact Service Response. Will not deliver notifications for this event", e);
			response = null;
		}

		return response;
	}

	private SsoContact parseContact(XMLStreamReader reader, String namespace) throws XMLStreamException {

		SsoContactMsg contact = new SsoContactMsg();
		String email = null;
		String userId = null;
		
		while (reader.hasNext()) {

			reader.next();

			if (reader.isStartElement()) {

				if (reader.getNamespaceURI().equals(namespace)) {

					if (reader.getLocalName().equals("emailAddress")) {
						email = reader.getElementText();
					}
					else if ( reader.getLocalName().equals( "userId" ) ) {
						userId = reader.getElementText();
					}					
				}
			} else if (reader.isEndElement()) {

				if (reader.getNamespaceURI().equals(namespace)) {

					if (reader.getLocalName().equals("user")) {
						break;
					}
				}
			}
		}

		contact.setEmailAddress(email);
		contact.setUserId(userId);

		if (log.isDebugEnabled()) {
			log.debug(String.format("Parsed SSO Contact %s", contact));
		}

		return contact;
	}

	private String getSSOUserRoleWebServiceUrl() {
		return notifServProperties == null ? null : (String) this.notifServProperties.get(SSO_USER_ROLE_WEB_SERVICE_URL_PROP_KEY);
	}

	private String getSSOUserRoleAndMarkWebServiceUrl() {
		return notifServProperties == null ? null : (String) this.notifServProperties.get(SSO_USER_ROLE_AND_MARK_WEB_SERVICE_URL_PROP_KEY);
	}

	private String getSSOUserIdWebServiceUrl() {
		return notifServProperties == null ? null : (String) this.notifServProperties.get(SSO_USER_ID_WEB_SERVICE_URL_PROP_KEY);
	}

	private String getSSOXmlNamespace() {
		return notifServProperties == null ? null : (String) this.notifServProperties.get(SSO_XML_NAMESPACE_PROP_KEY);
	}	
}
