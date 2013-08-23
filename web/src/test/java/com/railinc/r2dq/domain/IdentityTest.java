package com.railinc.r2dq.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IdentityTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEqualsObject() {
		assertEquals(new Identity(IdentityType.SsoRole,"role1"),new Identity(IdentityType.SsoRole,"role1"));
	}

}
