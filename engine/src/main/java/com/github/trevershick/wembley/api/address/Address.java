package com.github.trevershick.wembley.api.address;

import java.util.Collection;

import com.github.trevershick.wembley.api.Intent;
import com.github.trevershick.wembley.rrn.Rrn;

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
