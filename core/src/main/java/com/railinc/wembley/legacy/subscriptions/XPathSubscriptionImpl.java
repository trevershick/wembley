package com.railinc.wembley.legacy.subscriptions;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.api.event.DeliverySpec;
import com.railinc.wembley.api.event.Event;

public class XPathSubscriptionImpl implements Subscription {

	private static final Logger log = LoggerFactory.getLogger(XPathSubscriptionImpl.class);

	private String appId;
	private DeliverySpec deliverySpec;
	private String deliveryTiming;
	private String xpathStr;
	private XPath xpath;

	public XPathSubscriptionImpl(String appId) {
		this.appId = appId;
		if(log.isDebugEnabled()) {
			log.debug(String.format("Instantiating XPathSubscriptionImpl with AppId %s", appId));
		}
	}

	public boolean isMatch(Event event) {
		boolean match = false;
		if(xpath != null && event != null && event.getEventBody() != null && event.getEventBody().getBodyRoot() != null) {
			try {
				match = ((Boolean)xpath.evaluate(this.xpathStr, event.getEventBody().getBodyRoot(), XPathConstants.BOOLEAN)).booleanValue();
			} catch (XPathExpressionException e) {
				log.error(String.format("Error executing isMatch with XPath %s against event %s", xpathStr, event.getEventBody().getBodyRoot()), e);
			}
		}
		return match;
	}

	public String getXpathString() {
		return xpathStr;
	}

	public void setXpathString(String xpathStr) {
		this.xpathStr = xpathStr;

	}

	public DeliverySpec getDeliverySpec() {
		return deliverySpec;
	}

	public void setDeliverySpec(DeliverySpec deliverySpec) {
		this.deliverySpec = deliverySpec;
	}

	public String getAppId() {
		return appId;
	}

	public XPath getXpath() {
		return xpath;
	}

	public void setXpath(XPath xpath) {
		this.xpath = xpath;
	}

	public String getDeliveryTiming() {
		return deliveryTiming;
	}

	public void setDeliveryTiming(String deliveryTiming) {
		this.deliveryTiming = deliveryTiming;
	}
}
