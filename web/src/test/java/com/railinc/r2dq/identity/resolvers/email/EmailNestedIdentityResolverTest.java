package com.railinc.r2dq.identity.resolvers.email;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.beans.ExceptionListener;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.identity.resolvers.sso.SsoUserIdentityFunctionResolver;
import com.railinc.r2dq.identity.resolvers.sso.SsoUserIdentityResolver;
import com.railinc.r2dq.identity.resolvers.sso.SsoUserRoleIdentityResolver;
import com.railinc.r2dq.sso.SingleSignOn;

public class EmailNestedIdentityResolverTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testApply() {
		SingleSignOn sso = mock(SingleSignOn.class);
		String userLogin = "sdtxs01";
		when(sso.groupsForUser(eq(userLogin), any(ExceptionListener.class))).thenReturn(newArrayList("role1","role2"));
		when(sso.userLoginsByEmail(eq("trever.shick@railinc.com"), any(ExceptionListener.class))).thenReturn(newArrayList(userLogin));
		
		SsoUserIdentityResolver ur1 = new SsoUserIdentityFunctionResolver();
		SsoUserIdentityResolver ur2 = new SsoUserRoleIdentityResolver(sso);
		List<SsoUserIdentityResolver> resolvers = newArrayList(ur1,ur2);
		
		
		EmailToSsoUserLoginTransformer xformer = new EmailToSsoUserLoginTransformer();
		xformer.setSingleSignOn(sso);
		
		EmailNestedIdentityResolver r = new EmailNestedIdentityResolver();
		r.setEmailToSsoUserLoginTransformer(xformer);
		r.setResolvers(resolvers);
		Collection<Identity> ids = r.apply("trever.shick@railinc.com");
		
		Set<Identity> s1 = newHashSet(newArrayList(new Identity(IdentityType.SsoId, userLogin), new Identity(IdentityType.SsoRole,"role1"),new Identity(IdentityType.SsoRole,"role2")));
		Set<Identity> s2 = newHashSet(ids);

		assertEquals(s1.size(),s2.size());
		assertTrue(Sets.difference(s1, s2).isEmpty());
		
		
	}

}
