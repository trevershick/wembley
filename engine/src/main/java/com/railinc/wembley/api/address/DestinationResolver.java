package com.railinc.wembley.api.address;

import com.railinc.wembley.api.Intent;
import com.railinc.wembley.api.destination.Destination;
import com.railinc.wembley.api.retry.Retryable;

public interface DestinationResolver extends Retryable {
	Iterable<Destination> resolve(Address address, Intent forIntent);
	/**
	 * from an address, return the types of intents you can support
	 * @param t
	 * @return
	 */
	Iterable<Intent> probableIntents(Address t);
	
	boolean supports(Address t, Intent forIntent);
}
