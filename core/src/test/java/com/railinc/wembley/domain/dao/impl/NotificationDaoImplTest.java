package com.railinc.wembley.domain.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.railinc.wembley.legacy.domain.dao.NotificationDaoImpl;

public class NotificationDaoImplTest {

	
	@Test
	public void testTransformWildcard() {
		NotificationDaoImpl n = new NotificationDaoImpl();
		assertEquals("te%st", n.transformWildcard("te*st"));
		assertEquals("%te%st%", n.transformWildcard("**te****st**"));
		
		assertEquals("%te_st%", n.transformWildcard("**te?st**"));
		assertEquals("%te_st%", n.transformWildcard("**te??st**"));
	}

}
