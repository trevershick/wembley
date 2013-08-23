package com.railinc.r2dq.web;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RoutesTest {
	
	@Test
	public void testSubstitution() {
		
		Routes r = new Routes("x");
		r.register("y", Routes.simpleRoute("/whatever", "/{id}/zero"));
		
		assertEquals("/whatever/idvalue/zero", r.route("y", "idvalue"));
	}
}
