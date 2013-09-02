package com.railinc.wembley.api.address.resolve;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.Collections;

import com.railinc.wembley.api.Intent;
import com.railinc.wembley.api.address.Address;
import com.railinc.wembley.api.address.AddressResolver;
import com.railinc.wembley.api.address.EmailAddress;

public class EmailAddressResolver implements AddressResolver {

	@Override
	public Collection<Address> resolve(Address address, Intent forIntent) {
		if (supports(address,forIntent)) {
			return newArrayList(address);	
		} 
		return Collections.emptyList();
		
	}

	@Override
	public Collection<Intent> probableIntents(Address t) {
		return newArrayList(Intent.Email);
	}

	@Override
	public boolean supports(Address a, Intent forIntent) {
		return EmailAddress.class == a.getClass() && Intent.Email == forIntent;
	}

	@Override
	public boolean supports(Address a) {
		return EmailAddress.class == a.getClass();
	}

}
