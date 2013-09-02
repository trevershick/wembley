package com.railinc.wembley.api.address;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import com.google.common.base.Preconditions;
import com.railinc.wembley.rrn.Rrn;
import com.railinc.wembley.rrn.SsoUserRrn;

public class SSOUserAddress implements Address {

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
}
