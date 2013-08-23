package com.railinc.wembley.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This simple class provides a way to extract some information from a supposed xml 
 * value.
 * @author sdtxs01
 *
 */
public class EventBruteForceParser {
	private final String APPID_PATTERN_STRING = "appId>(.+)</(?:.+:)?appId";
	private final Pattern APPID_PATTERN = Pattern.compile(APPID_PATTERN_STRING, Pattern.DOTALL);

	private final String CORRELID_PATTERN_STRING = "correlationId>(.+)</(?:.+:)?correlationId";
	private final Pattern CORRELID_PATTERN = Pattern.compile(CORRELID_PATTERN_STRING, Pattern.DOTALL);
	/**
	 * 
	 * @param eventXml
	 * @return null if an app id can't be found
	 */
	public String extractAppId(String eventXml) {
		return matchIt(eventXml, APPID_PATTERN);
	}
	/**
	 * 
	 * @param eventXml
	 * @return null if a correlation id cannot be found
	 */
	public String extractCorrelationId(String eventXml) {
		return matchIt(eventXml, CORRELID_PATTERN);
	}
	/**
	 * @return null if patterns' group(1) is an empty string or not found
	 * @param in
	 * @param p
	 * @return
	 */
	private String matchIt(String in, Pattern p) {
		Matcher matcher = p.matcher(in);
		if (matcher.find()) {
			String value = matcher.group(1).trim();
			if (value.length() > 0) {
				return value;
			}
		}
		return null;		
	}
}

