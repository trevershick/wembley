package com.railinc.r2dq.identity.resolvers.sso;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.UserGroup;
import com.railinc.r2dq.usergroup.UserGroupService;

public class SsoUserLocalGroupIdentityResolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void neverReturnsNull() {
		SsoUserLocalGroupIdentityResolver r = new SsoUserLocalGroupIdentityResolver();
		assertNotNull(r.apply(null));
		assertEquals(0, r.apply(null).size());
	}

	@Test
	public void neverReturnsBlank() {
		SsoUserLocalGroupIdentityResolver r = new SsoUserLocalGroupIdentityResolver();
		assertNotNull(r.apply(null));
		assertEquals(0, r.apply(null).size());
	}
	
	@Test
	public void returnsTheRolesFromSso() {
		UserGroupService sso = mock(UserGroupService.class);
		
		when(sso.groupsForUser(eq("trever"))).thenReturn(newArrayList(ug("role1"),ug("role2")));
		SsoUserLocalGroupIdentityResolver r = new SsoUserLocalGroupIdentityResolver();
		r.setUserGroupService(sso);
		
		Collection<Identity> apply = r.apply("trever");
		assertNotNull(apply);
		
		
		assertTrue(Iterables.elementsEqual(
				newArrayList(new Identity(IdentityType.LocalGroup,"role1"),new Identity(IdentityType.LocalGroup,"role2")), 
				apply));
	}

	private UserGroup ug(String string) {
		UserGroup u = new UserGroup();
		u.setIdentifier(string);
		return u;
	}
	

}
