package com.railinc.wembley.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-wembley-hibernate.xml","classpath:spring-wembley-h2.xml"})
@TransactionConfiguration(defaultRollback=true)
public class SimpleTest {

	@Autowired
	SessionFactory sessionFactory;
	
	@Test
	public void test() {
		Application a = new Application();
		a.setApplicationId("ID");
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Application>> validate = validator.validate(a);
		if (!validate.isEmpty()) {
			throw new ValidationException();
		}
		
		Session s = sessionFactory.openSession();
		Transaction tx = s.beginTransaction();
		s.save(a);
		tx.commit();
		
		List list = s.createCriteria(Application.class).list();
		assertEquals(1, list.size());

		s.flush();
		s.close();
		
		
	}

}
