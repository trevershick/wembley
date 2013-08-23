package com.railinc.wembley.domain.dao.impl;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.railinc.wembley.legacy.domain.dao.BaseDaoImpl;

public class BaseDaoImplTest  {

	static class TestImpl extends BaseDaoImpl {

		@Override
		protected Logger getLog() {
			return LoggerFactory.getLogger(TestImpl.class);
		}
		
	}

	@Test
	public void testTableName() {
		TestImpl testImpl = new TestImpl();
		assertEquals("", testImpl.getSchema());
		assertEquals("T", testImpl.tableName("T"));
		
		testImpl.setSchema("DAOTEST");
		assertEquals("DAOTEST", testImpl.getSchema());
		assertEquals("DAOTEST.T", testImpl.tableName("T"));
		

		testImpl.setSchema("DAOTEST.");
		assertEquals("DAOTEST", testImpl.getSchema());
		assertEquals("DAOTEST.T", testImpl.tableName("T"));
		
		
		
		
	}

}
