package com.railinc.wembley.rrn;

import java.util.Map;

import com.google.common.collect.Maps;


		
abstract class AbstractRrn implements Rrn {
	protected String account;
	protected String resourceType;
	protected String resource;
	protected final Map<String,String> params = Maps.newHashMap();
	protected final Map<String,String> attributes = Maps.newHashMap();
	
	public abstract String getService();
	
	public String getRrn() {
		return rrn();
	}

	public String getAccount() {
		return account;
	}
	public String getResourceType() {
		return resourceType;
	}
	public String getResource() {
		return resource;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
}
