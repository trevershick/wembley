package com.railinc.r2dq.dataexception;

import static org.apache.commons.lang.StringUtils.lowerCase;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import org.apache.commons.lang.WordUtils;

import com.google.common.base.Function;

public class TaskCapitalizer implements Function<String, String> {

	@Override
	public String apply(String input) {
		return WordUtils.capitalizeFully(lowerCase(trimToEmpty(input)).replaceAll("_", " "));
	}

}
