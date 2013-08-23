package com.railinc.common.configuration;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

public class ConfigurationPropertySupport {

	private final String[] TRUE_VALUES = {"1","hai","t","true","y","yes"};
	private final Supplier<String> supplier;


	public ConfigurationPropertySupport(Supplier<String> valueSupplier) {
		Preconditions.checkNotNull(valueSupplier, "supplier cannot be null");
		this.supplier = valueSupplier;
	}
	
	public static final Collection<String> split(String value) {
		return Lists.newArrayList(Splitter.on(',').omitEmptyStrings().trimResults().split(value));
	}
	
	public boolean asBoolean(boolean defaultValue) {
		// if it's not set at all, then return the default value.
		if (StringUtils.isBlank(value())) {
			return defaultValue;
		}
		
		// otherwise return whether or not the value is in the TRUE_VALUES constant.
		return Arrays.binarySearch(TRUE_VALUES, String.valueOf(value()).toLowerCase()) >= 0;
	}

	public Collection<String> asCollectionFromCsv(String defaultCsv) {
		if (StringUtils.isBlank(value())) {
			return split(defaultCsv);
		}
		return split(value());
	}


	private String value() {
		return this.supplier.get();
	}

	public int asInt(int defaultValue) {
		try {
			return Integer.parseInt(value());
		} catch (Exception e) {
			return defaultValue;
		}
	}

}
