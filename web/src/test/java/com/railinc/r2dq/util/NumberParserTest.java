package com.railinc.r2dq.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.math.NumberRange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NumberParserTest {

	private String inputText;
	private NumberRange range;

	@Parameters
	public static Collection<Object[]> parameters() {
		Object[][] p = {
				{" -9 ,\t-3   ", new NumberRange(-3L,-9L) },
				{"3,9", new NumberRange(3L,9L) },
				{"1", new NumberRange(1L) },
				{"-9", new NumberRange(-9L) },
				{" -10\t\n", new NumberRange(-10L) },
				{"1,2", new NumberRange(1L,2L) },
				{" 3, -9", new NumberRange(3L,-9L) },
				{" 3 ,-9", new NumberRange(3L,-9L) },
				{" -9 ,-3", new NumberRange(-3L,-9L) }
		};
		return Arrays.asList(p);
	}
	
	public NumberParserTest(String in, NumberRange r) {
		this.inputText = in;
		this.range = r;
	}
	
	@Test
	public void parses() {
		try {
			assertEquals(range, NumberRangeParser.parse(inputText));
		} catch (ParseException pe) {
			fail(pe.getMessage());
		}
		
	}
}
