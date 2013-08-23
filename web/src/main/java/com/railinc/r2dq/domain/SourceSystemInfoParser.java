package com.railinc.r2dq.domain;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;

/**
 * Parses the raw source system information and returns a map (at this point)
 * input example : FIRST_NAME : ( Janell ) || LAST_NAME : (Lindell ) || COMPANY
 * : (Global Ethanol ) || EMAIL : (jlindell@globalethanolservices.com ) || PHONE
 * : (+5158862222175)
 * 
 * @author trevershick
 */
public class SourceSystemInfoParser {
	// private static final Pattern FIELD_REGEX =
	// Pattern.compile("([A-Z])+ : (\\((.+)\\))");
	public static final Pattern pattern = Pattern.compile("\\((.*)\\)");

	public Map<String, String> parse(String in) {
		Map<String, String> r = newHashMap();
		if (isBlank(in)) {
			return r;
		}
		try {
			Map<String, String> x = Splitter.on("||").trimResults().withKeyValueSeparator(':').split(in);
			for (Entry<String, String> s : x.entrySet()) {
				String newKey = trimToEmpty(s.getKey());
				String value = trimToEmpty(s.getValue());
				if (isBlank(value))
					continue;
				Matcher matcher = pattern.matcher(value);
				if (matcher.find()) {
					value = matcher.group(1);
				}
				if (isBlank(value))
					continue;
				r.put(newKey, trimToEmpty(value));
			}
		} catch (java.lang.IllegalArgumentException iae) {

		}

		return r;
	}
}
