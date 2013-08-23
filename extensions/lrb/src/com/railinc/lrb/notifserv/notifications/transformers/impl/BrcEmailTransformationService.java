package com.railinc.lrb.notifserv.notifications.transformers.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.railinc.wembley.core.exception.NotificationServiceException;
import com.railinc.wembley.event.model.DeliverySpec;
import com.railinc.wembley.event.model.EventParameter;
import com.railinc.wembley.event.model.impl.EmailDeliverySpecVo;
import com.railinc.wembley.event.model.impl.EventVo;
import com.railinc.wembley.event.model.impl.FindUsRailDeliverySpecVo;
import com.railinc.wembley.event.model.impl.MqDeliverySpecVo;
import com.railinc.wembley.event.model.impl.SSORoleDeliverySpecVo;
import com.railinc.wembley.event.model.impl.SSOUserDeliverySpecVo;
import com.railinc.wembley.notifications.model.Notification;
import com.railinc.wembley.notifications.transformers.TransformationService;

public class BrcEmailTransformationService implements TransformationService {

	private static final Logger log = LoggerFactory.getLogger(BrcEmailTransformationService.class);
	private static final String MESSAGE_TYPE_PARAM_NAME = "BrcMessageType";
	private static final String BRC_APPROVAL_REQUESTED_MSG_TYPE = "brcApprovalRequested";
	private static final String LRB_URL_PARAM_NAME = "lrbUrl";

	private static final String APP_ID = "LRB";
	private List<Class<? extends DeliverySpec>> deliverySpecs;
	private Map<String, String> emailTemplates;
	private String brcApprovalRequestedWebMsgType = "brcApprovalRequestedWeb";
	private String brcApprovalRequestedMqMsgType = "brcApprovalRequestedMq";
	private Pattern lrbUrlPattern = Pattern.compile("\\$\\{lrbUrl\\}");
	private Pattern locoInitPattern = Pattern.compile("\\$\\{locoInit\\}");
	private Pattern locoNumPattern = Pattern.compile("\\$\\{locoNum\\}");
	
	//HEAT #: 772275
	private Pattern billingPartyPattern = Pattern.compile("\\$\\{BillingParty\\}");
	private Pattern billedPartyPattern = Pattern.compile("\\$\\{BilledParty\\}");
	private Pattern approvalReasonPattern = Pattern.compile("\\$\\{approvalReason\\}");
	
	private Pattern repairDatePattern = Pattern.compile("\\$\\{repairDate\\}");
	private Pattern rejectionReasonPattern = Pattern.compile("\\$\\{rejectionReason\\}");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	private Map<String, String> namespaceUris;
	private XPathExpression locomotiveInitXPath;
	private XPathExpression locomotiveNumXPath;
	private XPathExpression repairDateXPath;
	private XPathExpression rejectionReasonXPath;
	
	//HEAT #: 772275
	private XPathExpression billingPartyXPath;
	private XPathExpression billedPartyXPath;
	private XPathExpression approvedReasonXPath;

	public BrcEmailTransformationService() {
		super();
		this.deliverySpecs = new ArrayList<Class<? extends DeliverySpec>>(4);
		this.deliverySpecs.add(EmailDeliverySpecVo.class);
		this.deliverySpecs.add(FindUsRailDeliverySpecVo.class);
		this.deliverySpecs.add(SSORoleDeliverySpecVo.class);
		this.deliverySpecs.add(SSOUserDeliverySpecVo.class);
		initXpathExpressions();
		log.info("Initialized BrcEmailTransformationService");
	}

	private final void initXpathExpressions() {

		this.namespaceUris = new HashMap<String, String>();
		namespaceUris.put("req", "http://schemas.railinc.com/eq/lrb/2008R1V1/LrbBillingRepairApprovalRequestDocument");
		namespaceUris.put("resp", "http://schemas.railinc.com/eq/lrb/2008R1V1/LrbBillingRepairApprovalResponseDocument");
		namespaceUris.put("brc", "http://schemas.railinc.com/eq/lrb/2008R1V1/LrbBillingRepair");
		namespaceUris.put("lrb", "http://schemas.railinc.com/eq/lrb/2008R1V1/LrbCommon");
		namespaceUris.put("rail", "http://schemas.railinc.com/cm/20081021/RailincCommon");

		XPathFactory xpathFactory = XPathFactory.newInstance();

		NamespaceContext ctx = new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				return namespaceUris.get(prefix);
			}
			public String getPrefix(String namespaceURI) {
				String prefix = null;
				Iterator<Map.Entry<String, String>> it = namespaceUris.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry<String, String> entry = it.next();
					if(StringUtils.equals(entry.getValue(), namespaceURI)) {
						prefix = entry.getKey();
						break;
					}
				}
				return prefix;
			}
			public Iterator<String> getPrefixes(String namespaceURI) {
				List<String> prefixes = new ArrayList<String>();
				Iterator<Map.Entry<String, String>> it = namespaceUris.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry<String, String> entry = it.next();
					if(StringUtils.equals(entry.getValue(), namespaceURI)) {
						prefixes.add(entry.getKey());
						break;
					}
				}
				return prefixes.iterator();
			}
		};

		XPath xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(ctx);
		try {
			this.locomotiveInitXPath = xpath.compile("//brc:BillingRepairHeader/brc:LocomotiveInitial/text()");
			this.locomotiveNumXPath = xpath.compile("//brc:BillingRepairHeader/brc:LocomotiveNumber/text()");
			this.repairDateXPath = xpath.compile("//brc:BillingRepairHeader/brc:RepairDate/text()");
			this.rejectionReasonXPath = xpath.compile("/resp:BillingRepairApprovalResponse/resp:BillingRepair/resp:BillingRepairResponse/resp:RejectedReason/text()");
			
			//HEAT #: 772275
			this.billingPartyXPath = xpath.compile("//brc:BillingRepairHeader/brc:BillingParty/text()");
			this.billedPartyXPath = xpath.compile("//brc:BillingRepairHeader/brc:BilledParty/text()");
			this.approvedReasonXPath = xpath.compile("//resp:BillingRepairApprovalResponse/resp:BillingRepair/resp:BillingRepairResponse/resp:ApprovedReason/text()");
			
			log.info("Initalized BrcEmailTransformationService XPath Expressions");
		} catch (XPathExpressionException e) {
			throw new NotificationServiceException("Failed to evalulate XPath expressions", e);
		}
	}

	public List<Class<? extends DeliverySpec>> getDeliverySpecTypes() {
		return this.deliverySpecs;
	}

	public String getAppId() {
		return APP_ID;
	}

	public Notification transformNotification(Notification not) {

		Notification notif = getNotification(not);
		String messageType = getMessageType(not);
		String emailTemplate = this.emailTemplates == null ? null : this.emailTemplates.get(messageType);

		if(log.isDebugEnabled()) {
			log.debug("Transforming the Notification " + not);
			log.debug("Message Type: " + messageType);
			log.debug("Email Template: '+ emailTemplate");
		}

		debugBody(not);
		
		if(emailTemplate != null) {
			emailTemplate = this.lrbUrlPattern.matcher(emailTemplate).replaceAll(getLrbUrl(not));
			emailTemplate = this.locoInitPattern.matcher(emailTemplate).replaceAll(getXPathValue(this.locomotiveInitXPath, not));
			emailTemplate = this.locoNumPattern.matcher(emailTemplate).replaceAll(getXPathValue(this.locomotiveNumXPath, not));
			emailTemplate = this.repairDatePattern.matcher(emailTemplate).replaceAll(getRepairDateValue(this.repairDateXPath, not));
			
			//HEAT #: 772275
			emailTemplate = this.billingPartyPattern.matcher(emailTemplate).replaceAll(getXPathValue(this.billingPartyXPath, not));
			emailTemplate = this.billedPartyPattern.matcher(emailTemplate).replaceAll(getXPathValue(this.billedPartyXPath, not));
			
			if (messageType.contains("Approved"))
				emailTemplate = this.approvalReasonPattern.matcher(emailTemplate).replaceAll(getXPathValue(this.approvedReasonXPath, not));
			else
				emailTemplate = this.rejectionReasonPattern.matcher(emailTemplate).replaceAll(getXPathValue(this.rejectionReasonXPath, not));
			
			EventVo event = (EventVo)notif.getEvent();
			event.setTextBody(emailTemplate);

			if(log.isDebugEnabled()) {
				log.debug("Final Email Message = " + emailTemplate);
			}
		}

		return notif;
	}

	private Notification getNotification(Notification not) {
		try {
			return (Notification)not.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("System error - could not clone notification", e);
		}
	}
	
	private void debugBody(Notification not) {
		try {
			Element e = (Element)not.getEvent().getEventBody().getBodyRoot();
			StringWriter out = new StringWriter();
			TransformerFactory f = TransformerFactory.newInstance();
			Transformer transformer = f.newTransformer();
			transformer.transform(new DOMSource(e), new StreamResult(out));
			String xml = out.getBuffer().substring(0, out.getBuffer().length());
			log.info("Transforming xml: ");
			log.info(xml);
			
		} catch (Exception e) {
			log.error("Error debugging notifcation body root", e);
		}
	}
	
	private String getMessageType(Notification not) {
		String messageType = null;
		if(not != null && not.getEvent() != null && not.getEvent().getEventHeader() != null && not.getEvent().getEventHeader().getEventParams() != null) {
			messageType = getEventParam(not.getEvent().getEventHeader().getEventParams(), MESSAGE_TYPE_PARAM_NAME);
			if(BRC_APPROVAL_REQUESTED_MSG_TYPE.equals(messageType)) {
				if(isMqReceiver(not)) {
					messageType = this.brcApprovalRequestedMqMsgType;
				} else {
					messageType = this.brcApprovalRequestedWebMsgType;
				}
			}
		}
		return messageType;
	}

	private boolean isMqReceiver(Notification not) {
		boolean mqRecv = false;

		if(not.getEvent().getEventHeader().getDeliverySpecs() != null && not.getEvent().getEventHeader().getDeliverySpecs().getDeliverySpecs() != null) {
			Iterator<? extends DeliverySpec> specs = not.getEvent().getEventHeader().getDeliverySpecs().getDeliverySpecs().iterator();
			while(specs.hasNext()) {
				DeliverySpec spec = specs.next();
				if(spec instanceof MqDeliverySpecVo) {
					mqRecv = true;
					break;
				}
			}
		}

		return mqRecv;
	}

	private String getLrbUrl(Notification not) {
		return StringUtils.defaultString(getEventParam(not.getEvent().getEventHeader().getEventParams(), LRB_URL_PARAM_NAME));
	}

	private String getXPathValue(XPathExpression xpathExpr, Notification not) {
		String value = null;
		if(not.getEvent().getEventBody() != null && not.getEvent().getEventBody().getBodyRoot() != null) {
			try {
				value = StringUtils.defaultString(xpathExpr.evaluate(not.getEvent().getEventBody().getBodyRoot()));
			} catch (XPathExpressionException e) {
				throw new NotificationServiceException("Failed to evaluate XPath epression for event", e);
			}
		}
		if(log.isDebugEnabled()) {
			log.debug(String.format("XPath Value for %s=%s", xpathExpr.toString(), value));
		}
		return StringUtils.defaultString(value);
	}

	private String getRepairDateValue(XPathExpression xpathExpr, Notification not) {
		String value = null;
		String rawValue = getXPathValue(xpathExpr, not);
		
		if(!StringUtils.isEmpty(rawValue)) {
			try {
				XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(StringUtils.trim(rawValue));
				if(cal != null) {
					value = dateFormat.format(cal.toGregorianCalendar().getTime());
				}
			} catch (Throwable e) {
				log.error(String.format("Failed to get XMLGregorianCalendar for RepairDate: %s - %s", rawValue, xpathExpr.toString()), e);
			}
		}
		if(log.isDebugEnabled()) {
			log.debug(String.format("XPath Value for %s=%s", xpathExpr.toString(), value));
		}
		return StringUtils.defaultString(value);
	}

	public void setEmailTemplates(Map<String, InputStream> emailTemplates) {
		if(this.emailTemplates == null) {
			this.emailTemplates = new HashMap<String, String>(emailTemplates == null ? 0 : emailTemplates.size());
		}
		for(Map.Entry<String, InputStream> entry : emailTemplates.entrySet()) {
			this.emailTemplates.put(entry.getKey(), readEmailTemplate(entry.getValue()));
		}
	}

	private String readEmailTemplate(InputStream in) {
		StringBuffer file = new StringBuffer();
		if(in != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			try {
				String line = reader.readLine();
				while(line != null) {
					file.append(line + "\n");
					line = reader.readLine();
				}
			} catch (IOException e) {
				throw new NotificationServiceException(e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					//Empty
				}
			}
		}
		return file.toString();
	}

	public void setBrcApprovalRequestedMqMsgType(String brcApprovalRequestedMqMsgType) {
		this.brcApprovalRequestedMqMsgType = brcApprovalRequestedMqMsgType;
	}
	public void setBrcApprovalRequestedWebMsgType(String brcApprovalRequestedWebMsgType) {
		this.brcApprovalRequestedWebMsgType = brcApprovalRequestedWebMsgType;
	}

	private String getEventParam(List<? extends EventParameter> params, String paramName) {
		String value = null;
		if(params != null) {
			for(EventParameter param : params) {
				if(paramName.equals(param.getParamName())) {
					value = param.getParamValue();
					break;
				}
			}
		}
		return value;
	}
}
