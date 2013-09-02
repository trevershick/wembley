package com.railinc.wembley.rrn;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class SsoUserRrn extends AbstractRrn {

	public SsoUserRrn(String userId) {
		this.resourceType = "user";
		this.resource = userId;
	}

	public SsoUserRrn(String account, String resourceType, String resource,
			Map<String, String> params) {
		this.account = account;
		this.resourceType = StringUtils.defaultIfBlank(resourceType, "address");
		this.resource = resource;
		this.params.putAll(params);
	}
	

	@Override
	public String rrn() {
		return format("rrn:%s:%s:%s:%s", getService(), trimToEmpty(getAccount()), trimToEmpty(getResourceType()), getResource());
	}

	
	public String toString() {
		return this.rrn();
	}

	@Override
	public String getService() {
		return "sso";
	}

	@Override
	public boolean isLogical() {
		return true;
	}

		
}
