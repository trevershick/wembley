package com.railinc.wembley.api.address;

import java.util.Collection;

import com.railinc.wembley.api.Intent;
import com.railinc.wembley.api.retry.Retryable;

public interface AddressResolver {
	/**
	 * resolve the provided address an return addresses for the given intent
	 * 
	 * @param address
	 * @param forIntent
	 * @return
	 */
	Collection<Address> resolve(Address address, Intent forIntent);
	/**
	 * from an address, return the types of intents the resolver knows about.
	 * @param t
	 * @return
	 */
	Collection<Intent> probableIntents(Address t);
	
	/**
	 * Can the resolver resolve addresses for the given intent from the specified address?
	 */
	boolean supports(Address t, Intent forIntent);
	/**
	 * Does this address resolver understand the supplied address?
	 * @param a
	 * @return
	 */
	boolean supports(Address a);
}
