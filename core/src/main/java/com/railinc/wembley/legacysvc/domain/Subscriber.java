package com.railinc.wembley.legacysvc.domain;

public interface Subscriber {
	Delivery delivery();
	String deliveryArgument();
	String uid();
	String realm();
}
