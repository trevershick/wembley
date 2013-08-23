package com.railinc.wembley.domain.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.railinc.wembley.legacy.domain.dao.DaoConstants;
import com.railinc.wembley.legacy.domain.dao.MailingListDaoImpl;
import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;

public class MailingListDaoImplTest extends HypersonicDbTest {

	private MailingListDaoImpl dao;

	@Before
	public void setUp() throws Exception {
		dao = new MailingListDaoImpl();
		dao.setDataSource(getDataSource());
		getJdbcTemplate().execute("DELETE FROM " + dao.MAILINGLIST_TABLE_NAME_WO_SCHEMA);
		getJdbcTemplate().execute("DELETE FROM " + DaoConstants.APPLICATION_TABLE_NAME_WO_SCHEMA);
	}

	@After
	public void tearDown() throws Exception {
		getJdbcTemplate().execute("DELETE FROM " + dao.MAILINGLIST_TABLE_NAME_WO_SCHEMA);
		getJdbcTemplate().execute("DELETE FROM " + DaoConstants.APPLICATION_TABLE_NAME_WO_SCHEMA);
	}

	@Test
	public void testGetMailingLists() {
		
		assertEquals(0, dao.getMailingLists().size());
		MailingListVo m = new MailingListVo();
		m.setApplication("UNITX");
		m.setShortName("My SHort Name");
		dao.createMailingList(m);
		
		assertEquals(1, dao.getMailingLists().size());
		
		
	}

	@Test
	public void testGetMailingListIds() {
		assertEquals(0, dao.getMailingListIds().size());
		MailingListVo m = new MailingListVo();
		m.setApplication("UNITX");
		m.setShortName("My SHort Name");
		dao.createMailingList(m);
		
		assertEquals(1, dao.getMailingListIds().size());
		String id = dao.getMailingListIds().get(0);
		m = dao.getMailingList(id);
		assertEquals("UNITX", m.getApplication());
		
	}
	
	@Test
	public void make_sure_log_is_there() {
		assertNotNull(dao.getLog());
	}

	@Test
	public void testGetMailingListStringString_Missing() {
		MailingListsVo mailingList = dao.getMailingList("Missing", "Missing");
		assertNotNull(mailingList);
	}

	
	@Test
	public void testGetMailingListStringString() {
		MailingListVo m = new MailingListVo();
		m.setApplication("UNITX");
		m.setType("MyType");
		m.setShortName("My SHort Name");
		dao.createMailingList(m);
		MailingListsVo mailingList = dao.getMailingList("UNITX", "MyType");
		assertNotNull(mailingList);
	}

	@Test
	public void testCreateMailingList() {
		MailingListVo m = new MailingListVo();
		m.setApplication("MyApp");
		m.setType("MyType");
		m.setShortName("MyShortName");
		m.setDescription("MyDesc");
		m.setFromAddress("my@my.com");
		m.setTitle("MyTitle");
		
		dao.createMailingList(m);
		MailingListVo m2 = dao.getMailingList(m.getKey());
		assertEquals(m.getApplication(), m2.getApplication());
		assertEquals(m.getType(), m2.getType());
		assertEquals(m.getShortName(), m2.getShortName());
		assertEquals(m.getDescription(), m2.getDescription());
		assertEquals(m.getFromAddress(), m2.getFromAddress());
		assertEquals(m.getTitle(), m2.getTitle());
		
	}

	@Test
	public void testDeleteMailingList() {
		MailingListVo m = new MailingListVo();
		m.setApplication("UNITX");
		m.setShortName("My SHort Name");
		dao.createMailingList(m);
		
		MailingListVo mailingList = dao.getMailingList(m.getKey());
		assertNotNull(mailingList);

		dao.deleteMailingList(mailingList.getKey());
		
		mailingList = dao.getMailingList(m.getKey());
		assertNull(mailingList);
		
	}

	@Test
	public void testGetMailingListByShortName_Missing() {
		MailingListVo mailingList = dao.getMailingListByShortName("Missing");
		assertNull(mailingList);
	}
	
	@Test
	public void testGetMailingListByShortName() {
		MailingListVo m = new MailingListVo();
		m.setApplication("UNITX");
		m.setShortName("My SHort Name");
		
		dao.createMailingList(m);
		
		MailingListVo mailingList = dao.getMailingListByShortName("My SHort Name");
		assertNotNull(mailingList);

		dao.deleteMailingList(mailingList.getKey());
		
		mailingList = dao.getMailingList(m.getKey());
		assertNull(mailingList);
	}

	@Test
	public void testUpdateMailingList() {
		String key = null;
		MailingListVo m = new MailingListVo();
		m.setApplication("UNITX");
		m.setShortName("My SHort Name");
		dao.createMailingList(m);
		key= m.getKey();
		
		m.setActive(false);
		m.setApplication("NEWAPP");
		m.setDescription("New Desc");
		m.setFromAddress("new@from.com");
		m.setShortName("Short Name");
		m.setKey("newKey");
		m.setTitle("new title");
		m.setType("new type");
		
		dao.updateMailingList(key, m);
		
		assertNull(dao.getMailingList(key));
		
		MailingListVo m2 = dao.getMailingList("newKey");
		assertNotNull(m2);
		assertEquals(m.getApplication(), m2.getApplication());
		assertEquals(m.getType(), m2.getType());
		assertEquals(m.getShortName(), m2.getShortName());
		assertEquals(m.getDescription(), m2.getDescription());
		assertEquals(m.getFromAddress(), m2.getFromAddress());
		assertEquals(m.getTitle(), m2.getTitle());
		assertFalse(m2.isActive());

	}

}
