package com.railinc.wembley.legacy.services.findusrail;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.parsing.SourceExtractor;

import com.railinc.wembley.api.core.NotificationServiceRemoteException;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContact;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactMsg;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactRequest;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactResponse;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactResponseHeaderMsg;
import com.railinc.wembley.legacy.services.findusrail.model.FindUsRailContactResponseMsg;

public class FindUsRailContactServiceImpl implements FindUsRailContactService, SourceExtractor {

	public static final String REQUEST_NS = "http://www.railinc.com/internal/schemas/cca/r2010v1/contactinformationrequest";
	public static final String RESPONSE_NS = "http://www.railinc.com/internal/schemas/cca/r2010v1/contactinformationresponse";
	private static final Logger log = LoggerFactory.getLogger(FindUsRailContactServiceImpl.class);
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; //"2008-07-15T14:13:05.041-04:00";
	private static final String FIND_US_RAIL_WEB_SERVICE_URL_PROP_KEY = "findUsRailServiceUrl";

	private String appId;
	private WebServiceTemplate wsTemplate;
	private XMLInputFactory xmlInputFactory;
	private FindUsRailContactServiceRequestMarshaller marshaller;
	private Properties notifServProperties;

	public FindUsRailContactServiceImpl(String appId) {
		this.appId = appId;
		this.xmlInputFactory = XMLInputFactory.newInstance();
		log.info("Initializing FindUsRailContactServiceImpl for AppID " + appId);
	}

	public FindUsRailContactResponse getFindUsRailContacts(FindUsRailContactRequest request) {

		log.info("Making web service request to FindUsRail Contact Service");
		FindUsRailContactResponse response = null;
		String url = getFindUsRailWebServiceUrl();

		if(wsTemplate == null || marshaller == null || url == null) {
			log.error(String.format("FindUsRailContactServiceImpl is missing the required dependencies of either the wsTemplate %s or the request marshaller %s " +
					"or the Service URL is not defined %s, Notifications for FindUsRail contacts cannot be delivered!", wsTemplate, marshaller, notifServProperties));
			throw new NotificationServiceRemoteException("FindUsRailContactServiceImpl is missing the required dependencies");
		} else {

			if(request != null) {
				try {
					String xml = marshaller.marshalFindUsRailContactServiceRequest(Long.valueOf(1), request);
					if(log.isDebugEnabled()) {
						log.debug(String.format("Calling the FindUsRail Contact Service with the Following XML: \n%s", xml));
					}
					StreamSource src = new StreamSource(new StringReader(xml));

					Object obj = this.wsTemplate.sendSourceAndReceive(url, src, this);

					if(obj != null && obj instanceof FindUsRailContactResponse) {
						response = (FindUsRailContactResponse)obj;
					}

					if(log.isDebugEnabled()) {
						log.debug(String.format("FindUsRailContacts Response: %s", obj));
					}
				} catch (WebServiceException e) {
					log.error("WebServiceException trying to invoke the FindUsRail web service", e);
					throw new NotificationServiceRemoteException(e);
				}

			} else {
				log.warn("FindUsRailContactServiceImpl was called with a null request, no notifications will be delivered");
			}
		}

		return response;
	}

	private String getFindUsRailWebServiceUrl() {
		return this.notifServProperties == null ? null : (String)this.notifServProperties.get(FIND_US_RAIL_WEB_SERVICE_URL_PROP_KEY);
	}

	public String getAppId() {
		return appId;
	}

	public Object extractData(Source src) throws IOException, TransformerException {

		log.debug("Parsing FindUsRailContact Service Response");

		FindUsRailContactResponseMsg response = new FindUsRailContactResponseMsg();
		FindUsRailContactResponseHeaderMsg responseHeader = new FindUsRailContactResponseHeaderMsg();
		List<FindUsRailContact> contacts = new ArrayList<FindUsRailContact>();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		sdf.setLenient(true);

		try {
			XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(src);
			while(reader.hasNext()) {
				reader.next();
				if(reader.isStartElement()) {
					if(reader.getNamespaceURI().equals(RESPONSE_NS)) {
						if(reader.getLocalName().equals("ContactIndividual")) {
							FindUsRailContact contact = parseContact(reader);
							contacts.add(contact);
						} else if(reader.getLocalName().equals("MessageHeader")) {
							parseMessageHeader(reader, responseHeader, sdf);
						} else if(reader.getLocalName().equals("ResultStatus")) {
							parseResultStatus(reader, responseHeader);
						}
					}
				}
			}

			reader.close();

			response.setResponseHeader(responseHeader);
			response.setContacts(contacts);

		} catch (XMLStreamException e) {
			log.error("Error parsing FindUsRailContact Service Response, Will not Deliver Notifications For This Event", e);
			response = null;
		}

		return response;
	}

	private void parseMessageHeader(XMLStreamReader reader, FindUsRailContactResponseHeaderMsg responseHeader, SimpleDateFormat sdf) throws XMLStreamException {
		Long id = null;
		String idStr = null;
		Date timestamp = null;
		String timestampStr = null;
		String softwareComponentId = null;
		String softwareVersion = null;

		while(reader.hasNext()) {
			reader.next();
			if(reader.isStartElement()) {
				if(reader.getNamespaceURI().equals(RESPONSE_NS)) {
					if(reader.getLocalName().equals("MessageID")) {
						idStr = reader.getElementText();
					} else if(reader.getLocalName().equals("MessageCreationTimestamp")) {
						timestampStr = reader.getElementText();
					} else if(reader.getLocalName().equals("SoftwareComponentID")) {
						softwareComponentId = reader.getElementText();
					} else if(reader.getLocalName().equals("SoftwareVersion")) {
						softwareVersion = reader.getElementText();
					}
				}
			} else if(reader.isEndElement()) {
				if(reader.getNamespaceURI().equals(RESPONSE_NS)) {
					if(reader.getLocalName().equals("MessageHeader")) {
						break;
					}
				}
			}
		}

		if(idStr != null) {
			try {
				id = Long.valueOf(idStr);
			} catch (NumberFormatException e) {
				log.warn(String.format("The FindUsRailContactService returned an invalid MessageId that could not be parsed %s", idStr), e);
			}
		}

		if(timestampStr != null) {
			try {
				timestampStr = timestampStr.replaceAll(":\\d\\d$", "00");
				timestamp = sdf.parse(timestampStr);
			} catch (ParseException e) {
				log.warn(String.format("The FindUsRailContactService returned an invalid MessageCreationTimestamp that could not be parsed %s", timestampStr), e);
			}
		}

		responseHeader.setMessageId(id);
		responseHeader.setMessageTimestamp(timestamp);
		responseHeader.setSoftwareComponentId(softwareComponentId);
		responseHeader.setSoftwareVersion(softwareVersion);
	}

	private void parseResultStatus(XMLStreamReader reader, FindUsRailContactResponseHeaderMsg responseHeader) throws XMLStreamException {

		String code = null;
		String desc = null;

		while(reader.hasNext()) {
			reader.next();
			if(reader.isStartElement()) {
				if(reader.getNamespaceURI().equals(RESPONSE_NS)) {
					if(reader.getLocalName().equals("ResultCode")) {
						code = reader.getElementText();
					} else if(reader.getLocalName().equals("ResultDescription")) {
						desc = reader.getElementText();
					}
				}
			} else if(reader.isEndElement()) {
				if(reader.getNamespaceURI().equals(RESPONSE_NS)) {
					if(reader.getLocalName().equals("ResultStatus")) {
						break;
					}
				}
			}
		}

		responseHeader.setResultCode(code);
		responseHeader.setResultDescription(desc);
	}

	private FindUsRailContact parseContact(XMLStreamReader reader) throws XMLStreamException {
		FindUsRailContactMsg contact = new FindUsRailContactMsg();
		String email = null;
		String id = null;

		while(reader.hasNext()) {
			reader.next();
			if(reader.isStartElement()) {
				if(reader.getNamespaceURI().equals(RESPONSE_NS)) {
					if(reader.getLocalName().equals("ID")) {
						id = reader.getElementText();
					} else if(reader.getLocalName().equals("EmailAddress")) {
						email = reader.getElementText();
					}
				}
			} else if(reader.isEndElement()) {
				if(reader.getNamespaceURI().equals(RESPONSE_NS)) {
					if(reader.getLocalName().equals("ContactIndividual")) {
						break;
					}
				}
			}
		}

		contact.setContactId(id);
		contact.setEmailAddress(email);

		if(log.isDebugEnabled()) {
			log.debug(String.format("Parsed FindUsRail Contact %s", contact));
		}

		return contact;
	}

	public void setMarshaller(FindUsRailContactServiceRequestMarshaller marshaller) {
		this.marshaller = marshaller;
	}

	public void setWsTemplate(WebServiceTemplate wsTemplate) {
		this.wsTemplate = wsTemplate;
	}

	public void setNotifServProperties(Properties notifServProperties) {
		this.notifServProperties = notifServProperties;
	}
}
