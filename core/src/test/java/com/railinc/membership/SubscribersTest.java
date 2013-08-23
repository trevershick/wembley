package com.railinc.membership;

import static junit.framework.Assert.assertNull;

import org.junit.Test;

import com.railinc.membership.sso.SSOIHateUsage;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUser;
import com.railinc.wembley.legacysvc.domain.Subscribers;
public class SubscribersTest {
	
	
	
	@Test
	public void test_byUid() {
		Subscribers subscribers = new Subscribers();
		subscribers.add(new SSOUser(new SSOIHateUsage(), "sdtxs01"));
		assertNull(subscribers.byUid(null, null));
		assertNull(subscribers.byUid(null, "xx"));
		assertNull(subscribers.byUid(SSOUser.REALM, null));
		assertNull(subscribers.byUid("unmatched", "unmatched"));
		
	}
}
