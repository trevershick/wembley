package com.railinc.r2dq.sourcesystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.sso.SsoRoleType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-r2dq-h2.xml", "classpath:spring-r2dq-hibernate.xml" })
@TransactionConfiguration
@Transactional
public class SourceSystemServiceTest {
	
	private SourceSystemServiceImpl service;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Before
	public void setup(){
		service = new SourceSystemServiceImpl();
		
		service.setSessionFactory(sessionFactory);
	}
	

	@Test
	public void active() {
		SourceSystem ug1 = new SourceSystem();
		ug1.setIdentifier("xx1");

		SourceSystem ug2 = new SourceSystem();
		ug2.setIdentifier("xx2");

		service.save(ug1);
		service.save(ug2);
		
		Collection<SourceSystem> all = service.all(null);
		assertEquals(2, all.size());
		
		service.delete(ug1);
		assertTrue(ug1.isDeleted());
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().evict(ug1);
		sessionFactory.getCurrentSession().evict(ug2);

		Collection<SourceSystem> active = service.active();
		assertEquals(1, active.size());
	}
	@Test
	public void all() {
		SourceSystem ug1 = new SourceSystem();
		ug1.setIdentifier("xx1");

		SourceSystem ug2 = new SourceSystem();
		ug2.setIdentifier("xx2");

		service.save(ug1);
		service.save(ug2);
		
		Collection<SourceSystem> all = service.all(null);
		assertEquals(2, all.size());
		
		all = service.all("xx1");
		assertEquals(1, all.size());
		
		service.delete(ug1);
		assertTrue(ug1.isDeleted());
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().evict(ug1);
		sessionFactory.getCurrentSession().evict(ug2);
		
		
		SourceSystem SourceSystem = service.get("xx1");
		assertNotNull(SourceSystem);
		assertTrue(SourceSystem.isDeleted());
		
		service.undelete(SourceSystem);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().evict(SourceSystem);
		
		SourceSystem = service.get("xx1");
		assertFalse(SourceSystem.isDeleted());
		
	}
	

	@Test
	public void getOrCreate() {
		SourceSystem ug1 = new SourceSystem();
		ug1.setIdentifier("xx1");

		service.save(ug1);
		
		Collection<SourceSystem> all = service.all(null);
		assertEquals(1, all.size());

		
		SourceSystem ns = service.getOrCreate("xxx");
		all = service.all(null);
		assertEquals(2, all.size());

		
		assertEquals("xxx", ns.getName());
		assertEquals("xxx", ns.getIdentifier());
		assertEquals(IdentityType.SsoRole, ns.getDataSteward().getType());
		assertEquals(SsoRoleType.Super.roleId(), ns.getDataSteward().getId());
		
		
	}

}
