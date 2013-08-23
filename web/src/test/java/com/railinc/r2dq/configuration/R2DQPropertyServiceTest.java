package com.railinc.r2dq.configuration;

import static org.junit.Assert.assertEquals;

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

import com.railinc.r2dq.domain.ConfigurationProperty;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-r2dq-h2.xml", "classpath:spring-r2dq-hibernate.xml" })
@TransactionConfiguration
@Transactional
public class R2DQPropertyServiceTest {

	private R2DQPropertyServiceImpl service;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Before
	public void setup(){
		service = new R2DQPropertyServiceImpl();
		
		service.setSessionFactory(sessionFactory);
	}
	

	@Test
	public void all() {
		ConfigurationProperty ug1 = new ConfigurationProperty();
		ug1.setCode("xx1");
		ug1.setValue("v1");

		ConfigurationProperty ug2 = new ConfigurationProperty();
		ug2.setCode("xx2");
		ug2.setValue("v1");

		service.save(ug1);
		service.save(ug2);
		
		Collection<ConfigurationProperty> all = service.all(null);
		assertEquals(2, all.size());
		
		all = service.all("xx1");
		assertEquals(1, all.size());
		
		service.delete(ug1);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().evict(ug1);
		sessionFactory.getCurrentSession().evict(ug2);
		
		
		
	}
	



}
