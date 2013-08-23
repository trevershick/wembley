package com.railinc.wembley.domain.impl.basic;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.railinc.wembley.legacy.domain.impl.basic.BasicUser;
import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUser;
import com.railinc.wembley.legacysvc.domain.Delivery;

public class BasicUserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEqualsObject() {
		BasicUser basicUser = new BasicUser(Delivery.EMAIL, "trevershick@yahoo.com");
		SSOUser ssoUser = new SSOUser(EasyMock.createNiceMock(SSO.class),"sdtxs01", Delivery.EMAIL,"trevershick@yahoo.com" );
		TestCase.assertTrue(false == basicUser.equals(ssoUser));
		
		
	}

}
