package com.railinc.common.configuration;

import java.util.Collection;

public interface ConfigurationProperty {


	public String getCode();
	public String getName();
	public String getValue();
	public String getDescription();
	public boolean asBoolean(boolean defaultValue);
	public Collection<String> asCollectionFromCsv(String defaultCsv);
	public int asInt(int defaultValue);

}
