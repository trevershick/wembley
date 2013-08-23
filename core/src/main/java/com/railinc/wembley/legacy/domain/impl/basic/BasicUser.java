package com.railinc.wembley.legacy.domain.impl.basic;

import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.SubscriberBaseImpl;
/**
 * Basic users are only identified by their devlivery mechanism and argument.
 * There is no linkage to any other system whatsoever
 * @author sdtxs01
 *
 */
public class BasicUser extends SubscriberBaseImpl {
	public BasicUser(Delivery delivery, String deliveryArg) {
		// if we pass null here then NPEs can occur when dealing with the uid.
		// stick the email in the uid
		super("",deliveryArg,delivery, deliveryArg);
	}
	
	public String toString() {
		return String.valueOf(this.deliveryArgument());
	}

}
