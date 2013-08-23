package com.railinc.r2dq.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class SimpleExceptionListenerTest {

	private SimpleExceptionListener l;


	@Before
	public void setUp() throws Exception {
		l = new SimpleExceptionListener();
	}

	@Test(expected=IllegalStateException.class)
	public void test_ise_when_no_exception() {
		assertNull(l.getException());
	}

	@Test(expected=IllegalArgumentException.class)
	public void test_iae_when_null_passed_in() {
		l.exceptionThrown(null);
	}
	
	@Test
	public void test_basics() {
		assertTrue(l.getExceptions().isEmpty());
		
		Exception e = new Exception();
		assertFalse(l.hasExceptions());
		
		l.exceptionThrown(e);
		
		assertTrue(l.hasExceptions());
		assertEquals(e, l.getException());
		assertEquals(1, l.getExceptions().size());
		assertEquals(e, l.getExceptions().iterator().next());
	}
	
	@Test
	public void uses_my_logger_with_warn_level() {
		Logger logger = mock(Logger.class);
		l = new SimpleExceptionListener(logger);
		Exception e = new Exception();
		l.exceptionThrown(e);
		
		verify(logger).warn(anyString(),eq(e));
	}
}
