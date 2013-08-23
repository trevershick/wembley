package com.railinc.r2dq.identity.resolvers.sso;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.beans.ExceptionListener;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.sso.SingleSignOn;

public class SsoUserRoleIdentityResolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void neverReturnsNull() {
		SsoUserRoleIdentityResolver r = new SsoUserRoleIdentityResolver();
		assertNotNull(r.apply(null));
		assertEquals(0, r.apply(null).size());
	}

	@Test
	public void neverReturnsBlank() {
		SsoUserRoleIdentityResolver r = new SsoUserRoleIdentityResolver();
		assertNotNull(r.apply(null));
		assertEquals(0, r.apply(null).size());
	}
	
	@Test
	public void returnsTheRolesFromSso() {
		SingleSignOn sso = mock(SingleSignOn.class);
		when(sso.groupsForUser(eq("trever"), any(ExceptionListener.class))).thenReturn(newArrayList("role1","role2"));
		SsoUserRoleIdentityResolver r = new SsoUserRoleIdentityResolver();
		r.setSingleSignOn(sso);
		
		Collection<Identity> apply = r.apply("trever");
		assertNotNull(apply);
		
		
		assertTrue(Iterables.elementsEqual(
				newArrayList(new Identity(IdentityType.SsoRole,"role1"),new Identity(IdentityType.SsoRole,"role2")), 
				apply));
	}
	

}
