package com.railinc.r2dq.domain;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SourceSystemInfoParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParse() {
		String s = "FIRST_NAME : ( Janell ) || LAST_NAME : (Lindell ) || COMPANY : (Global Ethanol ) || EMAIL : (jlindell@globalethanolservices.com ) || PHONE : (+5158862222175)";
		Map<String, String> v = new SourceSystemInfoParser().parse(s);
		
		assertEquals("Janell", v.get("FIRST_NAME"));
		assertEquals("Lindell", v.get("LAST_NAME"));
		assertEquals("Global Ethanol", v.get("COMPANY"));
		assertEquals("jlindell@globalethanolservices.com", v.get("EMAIL"));
		assertEquals("+5158862222175", v.get("PHONE"));
	
	}

}
