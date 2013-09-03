package com.railinc.wembley.api.address;

import java.util.Collection;

import com.railinc.wembley.api.Intent;
import com.railinc.wembley.rrn.Rrn;

public interface Address {
	Rrn toRrn();
	
	/**
	 * from an address, return the types of intents the resolver knows about.
	 * @param t
	 * @return
	 */
	Collection<Intent> probableIntents();
	
	/**
	 * Can the resolver resolve addresses for the given intent from the specified address?
	 */
	boolean supports(Intent forIntent);
	/**
	 * Does this address resolver understand the supplied address?
	 * @param a
	 * @return
	 */
//	boolean supports(Address a);

}
