package com.railinc.r2dq.domain;


import static org.junit.Assert.assertEquals;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:spring-test-r2dq-h2.xml",
		"classpath:spring-r2dq-hibernate.xml"
})
@TransactionConfiguration
@Transactional
public class SourceSystemTest {
	@Autowired
	SessionFactory sessionFactory;
	
	@Test
	public void equals_only_uses_identifier() {
		SourceSystem s1 = new SourceSystem("id","name");
		s1.setDeleted(YesNo.Y);
		s1.setOutboundQueue("q1");
		
		SourceSystem s2 = new SourceSystem("id","name2");
		s2.setDeleted(YesNo.N);
		s2.setOutboundQueue("q2");

		assertEquals("equals uses id only", s1, s2);
	}
	@Test
	public void hashcode_only_uses_identifier() {
		SourceSystem s1 = new SourceSystem("id","name");
		s1.setDeleted(YesNo.Y);
		s1.setOutboundQueue("q1");
		
		SourceSystem s2 = new SourceSystem("id","name2");
		s2.setDeleted(YesNo.N);
		s2.setOutboundQueue("q2");
		
		assertEquals("equals uses id only", s1, s2);
	}
	
	@Test
	public void testBasicPersistence() {
		SourceSystem s1 = new SourceSystem("id","name");
		s1.setDeleted(YesNo.Y);
		s1.setOutboundQueue("q1");
		
		SourceSystem s2 = new SourceSystem("id2","name2");
		s2.setDeleted(YesNo.N);
		s2.setOutboundQueue("q2");
		
		Session session = sessionFactory.getCurrentSession();
		session.save(s1);
		session.save(s2);
		session.flush();
		session.evict(s1);
		session.evict(s2);
		
		SourceSystem retrieved = (SourceSystem) session.createCriteria(SourceSystem.class)
			.add(Restrictions.eq(SourceSystem.PROPERTY_ID, "id"))
			.list().get(0);
		assertEquals("Saved and pulled back source system", s1, retrieved);
	}
}
