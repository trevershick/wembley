package com.railinc.wembley.legacysvc.domain;

import org.apache.commons.lang.StringUtils;

public class SystemConfigVo implements SystemConfig {

	private String key;
	private String value;
	private String description;

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}

		if(obj == null || !(obj instanceof SystemConfig)) {
			return false;
		}

		SystemConfig config = (SystemConfig)obj;
		return StringUtils.equals(this.key, config.getKey());
	}

	@Override
	public int hashCode() {
		return StringUtils.defaultString(this.key).hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s=%s", key, value);
	}
}
