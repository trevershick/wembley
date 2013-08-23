package com.railinc.r2dq.identity;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.beans.ExceptionListener;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.UserGroup;
import com.railinc.r2dq.identity.expander.IdentityExpander;
import com.railinc.r2dq.identity.expander.LocalUserGroupIdentityExpander;
import com.railinc.r2dq.identity.expander.SingularNoopExpander;
import com.railinc.r2dq.identity.expander.SsoRoleIdentityExpander;
import com.railinc.r2dq.identity.resolvers.email.EmailIdentityResolver;
import com.railinc.r2dq.identity.resolvers.email.EmailNestedIdentityResolver;
import com.railinc.r2dq.identity.resolvers.email.EmailToSsoUserLoginTransformer;
import com.railinc.r2dq.identity.resolvers.sso.SsoUserIdentityFunctionResolver;
import com.railinc.r2dq.identity.resolvers.sso.SsoUserIdentityResolver;
import com.railinc.r2dq.sso.SingleSignOn;
import com.railinc.r2dq.sso.SsoRoleType;
import com.railinc.r2dq.usergroup.UserGroupService;


public class IdentityServiceTest {


	@Test
	public void test_returns_fed_in_ssoids() {
		List<IdentityExpander> idxs = newArrayList();
		idxs.add(new SsoRoleIdentityExpander());
		idxs.add(new LocalUserGroupIdentityExpander());
		idxs.add(new SingularNoopExpander());
		
		BasicIdentityService bis = new BasicIdentityService();
		bis.setIdentityExpanders(idxs);
		
		Collection<Identity> usersAndOrGroups = newArrayList(Identity.forSsoId("sdtxs01"));
		Collection<Identity> userids = bis.resolveToSingularIdentities(usersAndOrGroups);
		
		assertEquals(1, userids.size());
		assertEquals(Identity.forSsoId("sdtxs01"), userids.iterator().next());
		
		
	}

	@Test
	public void test_resolve_sso_roles() {
		SingleSignOn sso = mock(SingleSignOn.class);
		when(sso.usersInGroup(eq("R2DQSUPPORT"),(ExceptionListener) any())).thenReturn(newArrayList("sdtxs01","SDTXSA2"));
		
		List<IdentityExpander> idxs = newArrayList();
		idxs.add(new SsoRoleIdentityExpander(sso));
		idxs.add(new LocalUserGroupIdentityExpander());
		idxs.add(new SingularNoopExpander());
		
		BasicIdentityService bis = new BasicIdentityService();
		bis.setIdentityExpanders(idxs);
		
		
		Collection<Identity> usersAndOrGroups = newArrayList(Identity.forSsoRole(SsoRoleType.Support));
		Collection<Identity> userids = bis.resolveToSingularIdentities(usersAndOrGroups);
		
		assertEquals(2, userids.size());
		assertTrue(userids.contains(Identity.forSsoId("sdtxs01")));
		assertTrue(userids.contains(Identity.forSsoId("SDTXSA2")));
	}
	
	
	@Test
	public void test_resolve_user_groups() {
		UserGroupService sso = mock(UserGroupService.class);
		UserGroup ug1 = new UserGroup();
		ug1.addUser("sdtxs01");
		ug1.addUser("SDTXSA2");
		
		when(sso.get("group1")).thenReturn(ug1);
		
		List<IdentityExpander> idxs = newArrayList();
		idxs.add(new SsoRoleIdentityExpander());
		idxs.add(new LocalUserGroupIdentityExpander(sso));
		idxs.add(new SingularNoopExpander());
		
		BasicIdentityService bis = new BasicIdentityService();
		bis.setIdentityExpanders(idxs);
		
		
		Collection<Identity> usersAndOrGroups = newArrayList(Identity.forLocalUserGroup("group1"));
		Collection<Identity> userids = bis.resolveToSingularIdentities(usersAndOrGroups);
		
		assertEquals(2, userids.size());
		assertTrue(userids.contains(Identity.forSsoId("sdtxs01")));
		assertTrue(userids.contains(Identity.forSsoId("SDTXSA2")));
	}


	@Test
	public void test_resolves_removes_redundancy() {
		SingleSignOn sso = mock(SingleSignOn.class);
		when(sso.userLoginsByEmail(eq("trever.shick@railinc.com"), (ExceptionListener) anyObject())).thenReturn(newArrayList("sdtxs01"));
		
		List<IdentityExpander> idxs = newArrayList();
		idxs.add(new SingularNoopExpander());
		
		
		List<SsoUserIdentityResolver> ssoUserIdentityResolvers = newArrayList();
		ssoUserIdentityResolvers.add(new SsoUserIdentityFunctionResolver());
		
		EmailToSsoUserLoginTransformer xformer = new EmailToSsoUserLoginTransformer();
		xformer.setSingleSignOn(sso);
		EmailNestedIdentityResolver r = new EmailNestedIdentityResolver();
		r.setEmailToSsoUserLoginTransformer(xformer);
		r.setResolvers(ssoUserIdentityResolvers);
		List<EmailIdentityResolver> emailResolvers = newArrayList();
		emailResolvers.add(r);
		
		

		BasicIdentityService bis = new BasicIdentityService();
		bis.setIdentityExpanders(idxs);
		bis.setEmailResolvers(emailResolvers);
		
		
		Collection<Identity> usersAndOrGroups = newArrayList(
				Identity.forSsoId("sdtxs01"),
				Identity.forExternalEmailUser("trever.shick@railinc.com"));
		
		Collection<Identity> userids = bis.resolveToSingularIdentities(usersAndOrGroups);
		
		assertEquals("The email address was redundant, should just return sdtxs01", 1, userids.size());
		assertEquals(Identity.forSsoId("sdtxs01") , userids.iterator().next());
	}


}
