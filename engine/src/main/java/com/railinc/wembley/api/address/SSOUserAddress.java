package com.railinc.wembley.api.address;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import com.railinc.wembley.api.Intent;
import com.railinc.wembley.rrn.Rrn;
import com.railinc.wembley.rrn.SsoUserRrn;

public class SSOUserAddress implements Address {

	private static final List<Intent> PROBABLE_INTENTS = Collections.unmodifiableList(newArrayList(Intent.Email));
	private static final List<Intent> SUPPORTED_INTENTS = Collections.unmodifiableList(newArrayList(Intent.Email, Intent.Phone, Intent.Preview, Intent.RSS, Intent.Rapid));
	
	private String ssoUserId;
	
	
	public SSOUserAddress(String userId) {
		Preconditions.checkArgument(isNotBlank(userId), "userId cannot be blank");
		this.ssoUserId = userId;
	}
	
	
	@Override
	public Rrn toRrn() {
		return new SsoUserRrn(ssoUserId);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ssoUserId == null) ? 0 : ssoUserId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SSOUserAddress other = (SSOUserAddress) obj;
		if (ssoUserId == null) {
			if (other.ssoUserId != null)
				return false;
		} else if (!ssoUserId.equals(other.ssoUserId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return ssoUserId;
	}
	
	@Override
	public Collection<Intent> probableIntents() {
		return PROBABLE_INTENTS;
	}

	@Override
	public boolean supports(Intent forIntent) {
		return SUPPORTED_INTENTS.contains(forIntent);
	}
}
