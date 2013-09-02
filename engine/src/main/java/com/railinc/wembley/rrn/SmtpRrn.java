package com.railinc.wembley.rrn;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class SmtpRrn extends AbstractRrn {

	public SmtpRrn(String emailAddress) {
		this.resourceType = "address";
		this.resource = emailAddress;
		this.resourceType = "address";
	}

	public SmtpRrn(String account, String resourceType, String resource,
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
		return "smtp";
	}

	@Override
	public boolean isLogical() {
		return false;
	}

		
}
