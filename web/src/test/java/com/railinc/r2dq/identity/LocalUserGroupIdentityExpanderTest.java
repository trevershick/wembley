package com.railinc.r2dq.identity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.UserGroup;
import com.railinc.r2dq.identity.expander.LocalUserGroupIdentityExpander;
import com.railinc.r2dq.usergroup.UserGroupService;

public class LocalUserGroupIdentityExpanderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testExpand_null_ignores_allbut_localgroup() {
		LocalUserGroupIdentityExpander ex = new LocalUserGroupIdentityExpander();
		// dont' setup the suer gorups ervice. it sould never talk to it anyhow
		
		assertEquals(0, ex.expand(Identity.forSsoId("test")).size());
		assertEquals(0, ex.expand(Identity.forSsoRole("test")).size());
		assertEquals(0, ex.expand(Identity.forExternalEmailUser("trever.shick@railinc.com")).size());
		assertEquals(0, ex.expand(null).size());
	}
	
	@Test
	public void testExpand_expands_user_group() {
		UserGroup fixtureUserGroup = new UserGroup();
		fixtureUserGroup.setIdentifier("group1");
		fixtureUserGroup.addUser("user1");
		fixtureUserGroup.addUser("user2");

		UserGroupService groupService = mock(UserGroupService.class);
		when(groupService.get("group1")).thenReturn(fixtureUserGroup);
		
		
		// execute
		LocalUserGroupIdentityExpander ex = new LocalUserGroupIdentityExpander();
		ex.setUserGroupService(groupService);

		
		Collection<Identity> expand = ex.expand(Identity.forLocalUserGroup("group1"));
		assertEquals(2, expand.size());
		assertTrue(expand.contains(Identity.forSsoId("user1")));
		assertTrue(expand.contains(Identity.forSsoId("user2")));
		verify(groupService).get("group1");
	}

}
