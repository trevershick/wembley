package com.railinc.wembley.domain.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.railinc.wembley.legacy.domain.dao.DaoConstants;
import com.railinc.wembley.legacy.domain.dao.EventDaoImpl;
import com.railinc.wembley.legacysvc.domain.EventVo;

public class EventDaoImplTest extends HypersonicDbTest {

	private EventDaoImpl dao;

	@Before
	public void setUp() throws Exception {
		dao = new EventDaoImpl();
		dao.setDataSource(getDataSource());
		getJdbcTemplate().execute("DELETE FROM " + DaoConstants.EVENTS_TABLE_NAME_WO_SCHEMA);		
	}

	@After
	public void tearDown() throws Exception {
		getJdbcTemplate().execute("DELETE FROM " + DaoConstants.EVENTS_TABLE_NAME_WO_SCHEMA);
	}

	@Test
	public void testGetEvent() {
		
		String xml = "<x></x>";
		String uuid = UUID.randomUUID().toString();
		
		String sql = "INSERT INTO " 
			+ DaoConstants.EVENTS_TABLE_NAME_WO_SCHEMA 
			+ " (APP_ID, EVENT_XML, EVENT_UID, RETRY_COUNT, STATE, STATE_TIMESTAMP, CORRELATION_ID)"
			+ " VALUES (?,?,?,?,?,?,?)";
		
		getJdbcTemplate().update(sql, new Object[]{"UNIT1", xml, uuid, 0,"NEW",new Date() , "1234"});
		EventVo event = dao.getEvent(uuid);
		assertNotNull(event);
		
		assertEquals(uuid, event.getEventUid());
		assertEquals(0, event.getRetryCount());
		assertEquals("NEW", event.getState());
		assertEquals(xml, new String(event.getContents()));
		assertEquals("1234", event.getCorrelationId());
		assertEquals("UNIT1", event.getAppId());
		assertNull("Send Afeter wasn't set is shoudl be null", event.getSendAfter());
	}

	@Test
	public void testGetEvent_withDateAfter() {
		
		String xml = "<x></x>";
		String uuid = UUID.randomUUID().toString();
		
		Date dt = new Date();
		
		String sql = "INSERT INTO " 
			+ DaoConstants.EVENTS_TABLE_NAME_WO_SCHEMA 
			+ " (APP_ID, EVENT_XML, EVENT_UID, RETRY_COUNT, STATE, STATE_TIMESTAMP, CORRELATION_ID, SEND_AFTER)"
			+ " VALUES (?,?,?,?,?,?,?,?)";
		
		getJdbcTemplate().update(sql, new Object[]{"UNIT1", xml, uuid, 0,"NEW",new Date() , "1234", dt});
		EventVo event = dao.getEvent(uuid);
		assertNotNull(event);
		
		assertEquals(uuid, event.getEventUid());
		assertEquals(0, event.getRetryCount());
		assertEquals("NEW", event.getState());
		assertEquals(xml, new String(event.getContents()));
		assertEquals("1234", event.getCorrelationId());
		assertEquals("UNIT1", event.getAppId());
		assertEquals("Dates Should Match (SEND_AFTER)", dt, event.getSendAfter());
	}

	
	
	@Test
	public void testGetEventByCorrelationId() {
		String xml = "<x></x>";
		String uuid = UUID.randomUUID().toString();
		
		String sql = "INSERT INTO " 
			+ DaoConstants.EVENTS_TABLE_NAME_WO_SCHEMA 
			+ " (APP_ID, EVENT_XML, EVENT_UID, RETRY_COUNT, STATE, STATE_TIMESTAMP, CORRELATION_ID)"
			+ " VALUES (?,?,?,?,?,?,?)";
		
		getJdbcTemplate().update(sql, new Object[]{"UNIT1", xml, uuid, 0,"NEW",new Date() , "1234"});
		EventVo event = dao.getEventByCorrelationId("UNIT1", "1234");
		assertNotNull(event);
		
		assertEquals(uuid, event.getEventUid());
		assertEquals(0, event.getRetryCount());
		assertEquals("NEW", event.getState());
		assertEquals(xml, new String(event.getContents()));
		assertEquals("1234", event.getCorrelationId());
		assertEquals("UNIT1", event.getAppId());
	}

	
	@Test
	public void testGetEventByCorrelationId_morethan_one_exists_pulls_jsut_one() {
		String xml = "<x></x>";
		String uuid = UUID.randomUUID().toString();
		
		String sql = "INSERT INTO " 
			+ DaoConstants.EVENTS_TABLE_NAME_WO_SCHEMA 
			+ " (APP_ID, EVENT_XML, EVENT_UID, RETRY_COUNT, STATE, STATE_TIMESTAMP, CORRELATION_ID)"
			+ " VALUES (?,?,?,?,?,?,?)";
		
		getJdbcTemplate().update(sql, new Object[]{"UNIT1", xml, uuid, 0,"NEW",new Date() , "1234"});
		getJdbcTemplate().update(sql, new Object[]{"UNIT1", xml, UUID.randomUUID().toString(), 0,"NEW",new Date() , "1234"});
		EventVo event = dao.getEventByCorrelationId("UNIT1", "1234");
		assertNotNull(event);
		
	}

}
