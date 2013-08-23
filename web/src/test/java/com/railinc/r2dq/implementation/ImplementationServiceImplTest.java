package com.railinc.r2dq.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;

import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Implementation;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.RuleNumberType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.implementation.ImplementationServiceImpl;

public class ImplementationServiceImplTest {
	private ImplementationServiceImpl service;
	private SessionFactory sessionFactory;
	
	@Before
	public void setup(){
		service = new ImplementationServiceImpl();
		sessionFactory = mock(SessionFactory.class);
		service.setSessionFactory(sessionFactory);
	}
	
	@Test
	public void testGetImplementationType_withSourceSystemNoMatchingRule_returnDefaultImplementation(){
		List<Implementation> all = new ArrayList<Implementation>();
		SourceSystem sso = new SourceSystem("SSO","Single Sign On");
		all.add(implementation(null,RuleNumberType.DEFAULT, null, null,ImplementationType.Manual, 10000));
		all.add(implementation(sso, RuleNumberType.DEFAULT, null, null, ImplementationType.Manual, 900));
		all.add(implementation(sso, RuleNumberType.SINGLE, 1L, null, ImplementationType.ForceStormDrain, 600));
		
		Session hibernateSession = mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(hibernateSession);
		Criteria criteria= mock(Criteria.class);
		when(hibernateSession.createCriteria(Implementation.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(all);
		
		
		ImplementationType respon = service.getImplementationType(buildDataException(sso, null));
		assertNotNull(respon);
		assertEquals(ImplementationType.Manual, respon);
	}
	
	
	@Test
	public void testGetImplementationType_withNoMatchingSourceSystemAndMatchingRule_returnStromDrainImplementationType(){
		List<Implementation> all = new ArrayList<Implementation>();
		SourceSystem sso = new SourceSystem("SSO","Single Sign On");
		all.add(implementation(null,RuleNumberType.DEFAULT, null, null,ImplementationType.Manual, 10000));
		all.add(implementation(sso, RuleNumberType.DEFAULT, null, null, ImplementationType.PassThrough, 900));
		all.add(implementation(null, RuleNumberType.SINGLE, 2L, null, ImplementationType.Manual, 700));
		all.add(implementation(sso, RuleNumberType.SINGLE, 1L, null, ImplementationType.Manual, 600));
		
		Session hibernateSession = mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(hibernateSession);
		Criteria criteria= mock(Criteria.class);
		when(hibernateSession.createCriteria(Implementation.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(all);
		
		ImplementationType respon = service.getImplementationType(buildDataException(new SourceSystem("FUR", ""), 2L));
		assertNotNull(respon);
		assertEquals(ImplementationType.StormDrain, respon);
	}
	
	@Test
	public void testGetImplementationType_withNoSourceSystemAndNoMatchingRule_returnStromDrainImplementationType(){
		List<Implementation> all = new ArrayList<Implementation>();
		SourceSystem sso = new SourceSystem("SSO","Single Sign On");
		all.add(implementation(null,RuleNumberType.DEFAULT, null, null,ImplementationType.Manual, 10000));
		all.add(implementation(sso, RuleNumberType.DEFAULT, null, null, ImplementationType.PassThrough, 900));
		all.add(implementation(null, RuleNumberType.SINGLE, 2L, null, ImplementationType.Manual, 700));
		all.add(implementation(sso, RuleNumberType.SINGLE, 1L, null, ImplementationType.Manual, 600));

		Session hibernateSession = mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(hibernateSession);
		Criteria criteria= mock(Criteria.class);
		when(hibernateSession.createCriteria(Implementation.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(all);
		
		ImplementationType respon = service.getImplementationType(buildDataException(new SourceSystem("FUR", ""), 5L));
		assertNotNull(respon);
		assertEquals(ImplementationType.StormDrain, respon);
	}
	
	@Test
	public void testGetResposibility_withSourceSystemAndRuleInRange_returnImplementationType(){
		List<Implementation> all = new ArrayList<Implementation>();
		SourceSystem sso = new SourceSystem("SSO","Single Sign On");
		
		all.add(implementation(sso, RuleNumberType.RANGE, 2L, 7l, ImplementationType.Manual, 750));
		all.add(implementation(null,RuleNumberType.DEFAULT, null, null,ImplementationType.Manual, 10000));
		all.add(implementation(sso, RuleNumberType.DEFAULT, null, null, ImplementationType.Manual, 900));
		all.add(implementation(null, RuleNumberType.SINGLE, 2L, null, ImplementationType.Manual, 700));
		all.add(implementation(sso, RuleNumberType.SINGLE, 1L, null, ImplementationType.Manual, 600));
		
		Session hibernateSession = mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(hibernateSession);
		Criteria criteria= mock(Criteria.class);
		when(hibernateSession.createCriteria(Implementation.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(all);
		
		ImplementationType respon = service.getImplementationType(buildDataException(new SourceSystem("SSO", ""), 5L));
		assertNotNull(respon);
		assertEquals(ImplementationType.Manual, respon);
		
	}
	
	private Implementation implementation(SourceSystem sourceSystem, RuleNumberType ruleNumberType,Long ruleNumberFrom, Long ruleNumberThru,ImplementationType implementationType, int precedence) {
		Implementation r = new Implementation();
		r.setType(implementationType);
		r.setRuleNumberType(ruleNumberType);
		r.setRuleNumberFrom(ruleNumberFrom);
		r.setRuleNumberThru(ruleNumberThru);
		r.setSourceSystem(sourceSystem);
		r.setPrecedence(precedence);
		return r;
	}
	
	private DataException buildDataException(SourceSystem sourceSystem, Long ruleNumber){
		DataException data = new DataException();
		data.setSourceSystem(sourceSystem);
		data.setRuleNumber(ruleNumber);
		return data;
	}
}
