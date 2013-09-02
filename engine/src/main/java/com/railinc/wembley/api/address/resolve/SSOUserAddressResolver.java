package com.railinc.wembley.api.address.resolve;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.util.Collection;
import java.util.List;

import com.railinc.wembley.api.Intent;
import com.railinc.wembley.api.address.Address;
import com.railinc.wembley.api.address.AddressResolver;
import com.railinc.wembley.api.address.EmailAddress;
import com.railinc.wembley.api.address.SSOUserAddress;

public class SSOUserAddressResolver implements AddressResolver {

	@Override
	public Collection<? super Address> resolve(Address address, Intent forIntent) {
		SSOUserAddress ssouser = (SSOUserAddress) address;
		List<? super Address> x = newArrayListWithCapacity(1);
		x.add(new EmailAddress(ssouser.toString() + "@wherever.com"));
		return x;
	}

	@Override
	public Collection<Intent> probableIntents(Address t) {
		return newArrayList(Intent.Email);
	}

	@Override
	public boolean supports(Address t, Intent forIntent) {
		return supports(t) && Intent.Email == forIntent;
	}

	@Override
	public boolean supports(Address a) {
		return SSOUserAddress.class.isAssignableFrom(a.getClass());
	}

}
