package com.railinc.r2dq.log;

import static org.junit.Assert.*;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.railinc.r2dq.domain.SystemAuditLog;
import com.railinc.r2dq.domain.SystemAuditLog.AuditLogType;
import com.railinc.r2dq.domain.views.SystemAuditLogView;
import com.railinc.r2dq.util.PagedCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-test-r2dq-h2.xml", "/spring-r2dq-hibernate.xml", "/spring-r2dq-si-audit-log.xml", "/spring-test-r2dq-services.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SystemAuditLogServiceImplUTest {
	
	private SystemAuditLogServiceImpl systemAuditLogService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Before
	public void setup(){
		systemAuditLogService = new SystemAuditLogServiceImpl();
		systemAuditLogService.setSessionFactory(sessionFactory);
	}
	
	@Test
	public void testFindLogsByNameAndId() {
		
		Session session = SessionFactoryUtils.getSession(getSessionFactory(), true);
		
		createSystemAuditLog(session, "100", "TestLog1", null, null, AuditLogType.TRACE);
		
		SystemAuditLogSearchCriteria criteria = new SystemAuditLogSearchCriteria();
		criteria.setEntityId("100");
		criteria.setEntityName("TestLog1");
		criteria.setDirection(Direction.Up);
		PagedCollection<SystemAuditLogView> logs = systemAuditLogService.findLogsByNameAndId(criteria);
		assertEquals(1, logs.size());
		criteria.clearAll();
		
		
		criteria.setEntityId("101");
		criteria.setEntityName("TestLog1");
		criteria.setDirection(Direction.Up);
		logs = systemAuditLogService.findLogsByNameAndId(criteria);
		assertEquals(0, logs.size());
		criteria.clearAll();
		
		criteria.setEntityId("100");
		criteria.setEntityName("TestLog2");
		criteria.setDirection(Direction.Up);
		logs = systemAuditLogService.findLogsByNameAndId(criteria);
		assertEquals(0, logs.size());
		criteria.clearAll();
		
		criteria.setEntityId("101");
		criteria.setEntityName("TestLog2");
		criteria.setDirection(Direction.Up);
		logs = systemAuditLogService.findLogsByNameAndId(criteria);
		assertEquals(0, logs.size());
		
		//TODO: empty criteria should return all records but it is throwing an database exception in QueryHelper.query
		criteria.setEntityId("");
		criteria.setEntityName("");
		criteria.setDirection(Direction.Up);
		logs = systemAuditLogService.findLogsByNameAndId(criteria);
		assertEquals(0, logs.size());
		criteria.clearAll();
		
	}
	
	@Test
	public void testFindLogsByNameAndId_Null_Criteria() {
		//TODO: null criteria should return all records but it is throwing an database exception in QueryHelper.query
		Session session = SessionFactoryUtils.getSession(getSessionFactory(), true);
		
		createSystemAuditLog(session, "100", "TestLog1", null, null, AuditLogType.TRACE);
		createSystemAuditLog(session, "101", "TestLog2", null, null, AuditLogType.TRACE);
		SystemAuditLogSearchCriteria criteria = new SystemAuditLogSearchCriteria();
		criteria.setEntityId(null);
		criteria.setEntityName(null);
		criteria.setDirection(Direction.Up);
		PagedCollection<SystemAuditLogView> logs = systemAuditLogService.findLogsByNameAndId(criteria);
		assertEquals(2, logs.size());
	}
	
	
	@Test 
	public void testFindLogByMasterId() {
		
		Session session = SessionFactoryUtils.getSession(getSessionFactory(), true);
		
		SystemAuditLog l1 = createSystemAuditLog(session, "200", "TestLog1", null, null, AuditLogType.TRACE);
		SystemAuditLog l2 = createSystemAuditLog(session, "201", "TestLog2", null, null, AuditLogType.TRACE);
		
		SystemAuditLogView log =  systemAuditLogService.findLogByMasterId(l1.getId());
		
		assertEquals("200", log.getEntityId());
		assertEquals("TestLog1", log.getEntityName());
		
		log =  systemAuditLogService.findLogByMasterId(l2.getId());
		assertEquals("201", log.getEntityId());
		assertEquals("TestLog2", log.getEntityName());
		
		log =  systemAuditLogService.findLogByMasterId(Long.MAX_VALUE);
		assertNull(log);
		
	}
	
	@Test 
	public void testLinkLogHistory() {
		
		Session session = SessionFactoryUtils.getSession(getSessionFactory(), true);
		
		createSystemAuditLog(session, "100", "Source", null, null, AuditLogType.TRACE);
		createSystemAuditLog(session, "200", "FirstLink", "100", "Source", AuditLogType.TRACE);
		createSystemAuditLog(session, "300", "SecondLink", "200", "FirstLink", AuditLogType.TRACE);
		SystemAuditLogSearchCriteria criteria = new SystemAuditLogSearchCriteria();
		criteria.setEntityId("200");
		criteria.setEntityName("FirstLink");
		
		PagedCollection<SystemAuditLogView> logs = systemAuditLogService.linkLogHistory(criteria);
		assertEquals(3, logs.size());
		criteria.clearAll();
		
		criteria.setEntityId("100");
		criteria.setEntityName("Source");
		
		logs = systemAuditLogService.linkLogHistory(criteria);
		assertEquals(3, logs.size());
		criteria.clearAll();
		
		criteria.setEntityId("300");
		criteria.setEntityName("SecondLink");
		
		logs = systemAuditLogService.linkLogHistory(criteria);
		assertEquals(3, logs.size());
		criteria.clearAll();
		
		criteria.setEntityId("301");
		criteria.setEntityName("NotThere");
		
		logs = systemAuditLogService.linkLogHistory(criteria);
		assertEquals(0, logs.size());
		criteria.clearAll();
	}
		
	private SystemAuditLog createSystemAuditLog(Session session, String entityId, String entityName, 
			String srcEntityId, String srcEntityName, AuditLogType type) {
		
		SystemAuditLog log = new SystemAuditLog();
		log.setEntityId(entityId);
		log.setEntityName(entityName);
		log.setSourceEntityId(srcEntityId);
		log.setSourceEntityName(srcEntityName);
		log.setAction("TEST_ACTION");
		log.setCause("Cause I said so!");
		log.setCreatedDate(new Date());
		log.setDetails("details");

		if (type.toString().equalsIgnoreCase("TRACE")) {
			log.setType(AuditLogType.TRACE);
		} else {
			log.setType(AuditLogType.ERROR);
		}
		
		systemAuditLogService.save(log);
		session.flush();
		session.evict(log);
		return log;
	}

}
