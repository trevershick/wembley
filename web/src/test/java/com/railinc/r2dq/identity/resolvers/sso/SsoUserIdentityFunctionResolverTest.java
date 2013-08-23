package com.railinc.r2dq.identity.resolvers.sso;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;

public class SsoUserIdentityFunctionResolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void neverReturnsNull() {
		SsoUserIdentityFunctionResolver r = new SsoUserIdentityFunctionResolver();
		assertNotNull(r.apply(null));
		assertEquals(0, r.apply(null).size());
	}

	@Test
	public void neverReturnsBlank() {
		SsoUserIdentityFunctionResolver r = new SsoUserIdentityFunctionResolver();
		assertNotNull(r.apply(null));
		assertEquals(0, r.apply(null).size());
	}
	
	@Test
	public void returnsTheUserHimself() {
		SsoUserIdentityFunctionResolver r = new SsoUserIdentityFunctionResolver();
		Collection<Identity> apply = r.apply("trever");
		assertNotNull(apply);
		assertEquals(newArrayList(new Identity(IdentityType.SsoId,"trever")), apply);
	}
}
