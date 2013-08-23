package com.railinc.r2dq.identity.resolvers.email;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;

public class EmailUserIdentityFunctionResolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void neverReturnsNull() {
		EmailIdentityFunctionResolver r = new EmailIdentityFunctionResolver();
		assertNotNull(r.apply(null));
		assertEquals(0, r.apply(null).size());
	}

	@Test
	public void neverReturnsBlank() {
		EmailIdentityFunctionResolver r = new EmailIdentityFunctionResolver();
		assertNotNull(r.apply(null));
		assertEquals(0, r.apply(null).size());
	}
	
	@Test
	public void returnsTheUserHimself() {
		EmailIdentityFunctionResolver r = new EmailIdentityFunctionResolver();
		Collection<Identity> apply = r.apply("trever@shick.com");
		assertNotNull(apply);
		assertEquals(newArrayList(new Identity(IdentityType.ExternalEmailUser,"trever@shick.com")), apply);
	}
}
