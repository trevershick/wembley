package com.railinc.r2dq.usergroups;

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

import com.railinc.r2dq.domain.UserGroup;
import com.railinc.r2dq.usergroup.UserGroupServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-r2dq-h2.xml", "classpath:spring-r2dq-hibernate.xml" })
@TransactionConfiguration
@Transactional
public class UserGroupServiceTest {
	
	private UserGroupServiceImpl service;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Before
	public void setup(){
		service = new UserGroupServiceImpl();
		
		service.setSessionFactory(sessionFactory);
	}
	
	
	@Test
	public void all() {
		UserGroup ug1 = new UserGroup();
		ug1.setIdentifier("xx1");

		UserGroup ug2 = new UserGroup();
		ug2.setIdentifier("xx2");

		service.save(ug1);
		service.save(ug2);
		
		Collection<UserGroup> all = service.all(null);
		assertEquals(2, all.size());
		
		all = service.all("xx1");
		assertEquals(1, all.size());
		
		service.delete(ug1);
		assertTrue(ug1.isDeleted());
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().evict(ug1);
		sessionFactory.getCurrentSession().evict(ug2);
		
		
		UserGroup userGroup = service.get("xx1");
		assertNotNull(userGroup);
		assertTrue(userGroup.isDeleted());
		
		service.undelete(userGroup);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().evict(userGroup);
		
		userGroup = service.get("xx1");
		assertFalse(userGroup.isDeleted());
		
	}
	
	

	@Test
	public void getGroupsForUser() {
		UserGroup ug1 = new UserGroup();
		ug1.setIdentifier("ug1");
		ug1.addUser("sdtxs01");
		
		UserGroup ug2 = new UserGroup();
		ug2.setIdentifier("ug2");

		UserGroup ug3 = new UserGroup();
		ug3.setIdentifier("ug3");
		ug3.addUser("sdtxs01");
		
		service.save(ug1);
		service.save(ug2);
		service.save(ug3);
		
		Collection<UserGroup> groupsForUser = service.groupsForUser("sdtxs01");
		assertEquals(" 1 and 3 should have come back", 2, groupsForUser.size());
		assertTrue("1 shoudl have been returned", groupsForUser.contains(ug1));
		assertTrue("3 shoudl have been returned", groupsForUser.contains(ug3));
	}

}
