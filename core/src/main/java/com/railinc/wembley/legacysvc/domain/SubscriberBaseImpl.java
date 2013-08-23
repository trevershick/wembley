package com.railinc.wembley.legacysvc.domain;



public class SubscriberBaseImpl implements Subscriber {//, Comparable<Subscriber> {
	private String uid;

	private String deliveryArgument;
	private Delivery delivery;

	private String realm;
	
	public SubscriberBaseImpl(String realm, String userId, Delivery delivery, String deliveryArg) {
		this.uid = userId;
		this.realm = realm;
		this.delivery = delivery;
		this.deliveryArgument = deliveryArg;
	}
	public SubscriberBaseImpl(String realm, String userId) {
		this.uid = userId;
		this.realm = realm;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SubscriberBaseImpl)) {
			return false;
		}
		
		SubscriberBaseImpl rhs = (SubscriberBaseImpl) obj;
		
		return uid.equals(rhs.uid);
	}


	public String toString() {
		return realm + "\\" + uid;
	}
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	
	public Delivery delivery() {
		return delivery;
	}

	public String deliveryArgument() {
		return deliveryArgument;
	}
	
	public String realm() {
		return realm;
	}
	public String uid() {
		return uid;
	}
	public void setDeliveryArgument(String deliveryArgument) {
		this.deliveryArgument = deliveryArgument;
	}
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

}
