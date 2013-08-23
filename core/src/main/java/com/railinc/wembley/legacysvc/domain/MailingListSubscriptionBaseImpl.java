package com.railinc.wembley.legacysvc.domain;

import java.sql.Date;


public abstract class MailingListSubscriptionBaseImpl implements MailingListSubscription {
	private Delivery delivery;
	private String deliveryArgument;
	private SubscriptionMode mode;
	private String mailingListKey;
	private String uid;
	private Date lastModified;
	
	
	protected MailingListSubscriptionBaseImpl(String uid, Delivery d, String dArg, SubscriptionMode mode, String mailingListKey) {
		this.uid = uid;
		this.delivery = d;
		this.deliveryArgument = dArg;
		this.mode = mode;
		this.mailingListKey = mailingListKey;
	}
	
	
	public Delivery delivery() {
		return delivery;
	}
	public String deliveryArgument() {
		return deliveryArgument;
	}
	public Subscribers execute() {
		Subscribers hashSet = new Subscribers();
		execute(hashSet);
		return hashSet;
	}
	
	public boolean explicit() {
		return false;
	}

	public Date getLastModified() {
		return lastModified;
	}
	
	public String getMailingListKey() {
		return this.mailingListKey;
	}

	public String key() {
		return uid;
	}

	public SubscriptionMode mode() {
		return this.mode;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public Date lastModified() {
		return getLastModified();
	}
	@Override
	public String toString() {
		return new StringBuilder().append("delivery=").append(delivery).append(",arg=").append(deliveryArgument).append(", mode=").append(mode).toString();
	}

}
