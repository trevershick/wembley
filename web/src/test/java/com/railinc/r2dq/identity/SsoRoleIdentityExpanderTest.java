package com.railinc.r2dq.identity;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.beans.ExceptionListener;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.identity.expander.SsoRoleIdentityExpander;
import com.railinc.r2dq.sso.SingleSignOn;

public class SsoRoleIdentityExpanderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testExpand_null_ignores_allbut_localgroup() {
		SsoRoleIdentityExpander ex = new SsoRoleIdentityExpander();
		// dont' setup the suer gorups ervice. it sould never talk to it anyhow
		
		assertEquals(0, ex.expand(Identity.forSsoId("test")).size());
		assertEquals(0, ex.expand(Identity.forLocalUserGroup("test")).size());
		assertEquals(0, ex.expand(Identity.forExternalEmailUser("trever.shick@railinc.com")).size());
		assertEquals(0, ex.expand(null).size());
	}
	
	@Test
	public void testExpand_expands_role() {

		SingleSignOn sso = mock(SingleSignOn.class);
		
		when(sso.usersInGroup(eq("group1"), any(ExceptionListener.class))).thenReturn(newArrayList("user1","user2"));
		
		
		// execute
		SsoRoleIdentityExpander ex = new SsoRoleIdentityExpander();
		ex.setSingleSignOn(sso);

		
		Collection<Identity> expand = ex.expand(Identity.forSsoRole("group1"));
		assertEquals(2, expand.size());
		assertTrue(expand.contains(Identity.forSsoId("user1")));
		assertTrue(expand.contains(Identity.forSsoId("user2")));
		verify(sso).usersInGroup(eq("group1"), any(ExceptionListener.class));
	}

}
