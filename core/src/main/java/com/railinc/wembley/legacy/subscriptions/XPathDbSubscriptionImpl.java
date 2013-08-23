package com.railinc.wembley.legacy.subscriptions;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XPathDbSubscriptionImpl extends XPathSubscriptionImpl {

	private static final Logger log = LoggerFactory.getLogger(XPathDbSubscriptionImpl.class);

	private String subscriptionUid;
	private boolean active = true;
	private String createdUser;
	private Date createdTimstamp;
	private String modifiedUser;
	private Date modifiedTimestamp;

	public XPathDbSubscriptionImpl(String appId) {
		super(appId);
		if(log.isDebugEnabled()) {
			log.debug(String.format("Instantiating XPathDbSubscriptionImpl with AppId %s", appId));
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreatedTimstamp() {
		return createdTimstamp;
	}

	public void setCreatedTimstamp(Date createdTimstamp) {
		this.createdTimstamp = createdTimstamp;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getModifiedTimestamp() {
		return modifiedTimestamp;
	}

	public void setModifiedTimestamp(Date modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}

	public String getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public String getSubscriptionUid() {
		return subscriptionUid;
	}

	public void setSubscriptionUid(String subscriptionUid) {
		this.subscriptionUid = subscriptionUid;
	}

	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(obj == null || !(obj instanceof XPathDbSubscriptionImpl)) {
			return false;
		}
		XPathDbSubscriptionImpl sub = (XPathDbSubscriptionImpl)obj;
		return this.subscriptionUid == null ? sub.getSubscriptionUid() == null :
			this.subscriptionUid.equals(sub.getSubscriptionUid());
	}

	public int hashCode() {
		return this.subscriptionUid == null ? 0 : this.subscriptionUid.hashCode();
	}
}
