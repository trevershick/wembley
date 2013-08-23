package com.railinc.r2dq.responsibility;

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
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.Responsibility;
import com.railinc.r2dq.domain.RuleNumberType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.sso.SsoRoleType;

public class ResponsibilityServiceTest {
	
	private ResponsibilityServiceImpl service;
	private SessionFactory sessionFactory;
	
	@Before
	public void setup(){
		service = new ResponsibilityServiceImpl();
		sessionFactory = mock(SessionFactory.class);
		service.setSessionFactory(sessionFactory);
	}
	
	@Test
	public void testGetResposibility_withSourceSystemNoMatchingRule_returnDefaultRuleResponsibility(){
		List<Responsibility> all = new ArrayList<Responsibility>();
		SourceSystem sso = new SourceSystem("SSO","Single Sign On");
		all.add(responsibility(null,RuleNumberType.DEFAULT, null, null,IdentityType.SsoRole,SsoRoleType.DataSteward.roleId(), 10000));
		all.add(responsibility(sso, RuleNumberType.DEFAULT, null, null, IdentityType.SsoId, "sdtxs01", 900));
		all.add(responsibility(sso, RuleNumberType.SINGLE, 1L, null, IdentityType.SsoId, "itvxm01", 600));
		
		Session hibernateSession = mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(hibernateSession);
		Criteria criteria= mock(Criteria.class);
		when(hibernateSession.createCriteria(Responsibility.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(all);
		
		
		Responsibility respon = service.getResponsibility(buildDataException(sso, null));
		assertNotNull(respon);
		assertEquals("sdtxs01", respon.getResponsiblePersonId());
		assertEquals(sso, respon.getSourceSystem());
	}
	
	@Test
	public void testGetResposibility_withNoMatchingSourceSystemAndMatchingRule_returnDefaultSourceSystemAndMatchingRuleResponsibility(){
		List<Responsibility> all = new ArrayList<Responsibility>();
		SourceSystem sso = new SourceSystem("SSO","Single Sign On");
		all.add(responsibility(null,RuleNumberType.DEFAULT, null, null,IdentityType.SsoRole,SsoRoleType.DataSteward.roleId(), 10000));
		all.add(responsibility(sso, RuleNumberType.DEFAULT, null, null, IdentityType.SsoId, "sdtxs01", 900));
		all.add(responsibility(null, RuleNumberType.SINGLE, 2L, null, IdentityType.LocalGroup, "tester", 700));
		all.add(responsibility(sso, RuleNumberType.SINGLE, 1L, null, IdentityType.SsoId, "itvxm01", 600));
		
		Session hibernateSession = mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(hibernateSession);
		Criteria criteria= mock(Criteria.class);
		when(hibernateSession.createCriteria(Responsibility.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(all);
		
		Responsibility respon = service.getResponsibility(buildDataException(new SourceSystem("FUR", ""), 2L));
		assertNotNull(respon);
		assertEquals("tester", respon.getResponsiblePersonId());
		assertEquals(null, respon.getSourceSystem());
		assertEquals(Long.valueOf(2), respon.getRuleNumberFrom());
	}
	
	@Test
	public void testGetResposibility_withNoSourceSystemAndNoMatchingRule_returnDefaultSourceSystemAndDefaultRuleResponsibility(){
		List<Responsibility> all = new ArrayList<Responsibility>();
		SourceSystem sso = new SourceSystem("SSO","Single Sign On");
		all.add(responsibility(null, RuleNumberType.SINGLE, 2L, null, IdentityType.LocalGroup, "tester", 700));
		all.add(responsibility(null,RuleNumberType.DEFAULT, null, null,IdentityType.SsoRole,SsoRoleType.DataSteward.roleId(), 10000));
		all.add(responsibility(sso, RuleNumberType.DEFAULT, null, null, IdentityType.SsoId, "sdtxs01", 900));
		all.add(responsibility(sso, RuleNumberType.SINGLE, 1L, null, IdentityType.SsoId, "itvxm01", 600));

		Session hibernateSession = mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(hibernateSession);
		Criteria criteria= mock(Criteria.class);
		when(hibernateSession.createCriteria(Responsibility.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(all);
		
		Responsibility respon = service.getResponsibility(buildDataException(new SourceSystem("FUR", ""), 5L));
		assertNotNull(respon);
		assertEquals(SsoRoleType.DataSteward.roleId(), respon.getResponsiblePersonId());
		assertEquals(null, respon.getSourceSystem());
		assertEquals(null, respon.getRuleNumberFrom());
	}
	
	@Test
	public void testGetResposibility_withSourceSystemAndRuleInRange_returnDefaultSourceSystemAndDefaultRuleResponsibility(){
		List<Responsibility> all = new ArrayList<Responsibility>();
		SourceSystem sso = new SourceSystem("SSO","Single Sign On");
		all.add(responsibility(null, RuleNumberType.RANGE, 2L, 7l, IdentityType.LocalGroup, "ranger", 750));
		all.add(responsibility(null,RuleNumberType.DEFAULT, null, null,IdentityType.SsoRole,SsoRoleType.DataSteward.roleId(), 10000));
		all.add(responsibility(sso, RuleNumberType.DEFAULT, null, null, IdentityType.SsoId, "sdtxs01", 900));
		all.add(responsibility(null, RuleNumberType.SINGLE, 2L, null, IdentityType.LocalGroup, "tester", 700));
		all.add(responsibility(sso, RuleNumberType.SINGLE, 1L, null, IdentityType.SsoId, "itvxm01", 600));
		
		Session hibernateSession = mock(Session.class);
		when(sessionFactory.getCurrentSession()).thenReturn(hibernateSession);
		Criteria criteria= mock(Criteria.class);
		when(hibernateSession.createCriteria(Responsibility.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(all);
		
		Responsibility respon = service.getResponsibility(buildDataException(new SourceSystem("FUR", ""), 5L));
		assertNotNull(respon);
		assertEquals("ranger", respon.getResponsiblePersonId());
		assertEquals(null, respon.getSourceSystem());
		assertEquals(Long.valueOf(2), respon.getRuleNumberFrom());
		assertEquals(Long.valueOf(7), respon.getRuleNumberThru());
	}
	
	private Responsibility responsibility(SourceSystem sourceSystem, RuleNumberType ruleNumberType,Long ruleNumberFrom, Long ruleNumberThru, IdentityType identityType, String identityId, int precedence) {
		Responsibility r = new Responsibility();
		r.setResponsiblePerson(new Identity(identityType, identityId));
		r.setRuleNumberType(ruleNumberType);
		r.setRuleNumberFrom(ruleNumberFrom);
		r.setRuleNumberThru(ruleNumberThru);
		r.setSourceSystem(sourceSystem);
		r.setResponsiblePersonType(identityType);
		r.setResponsiblePersonId(identityId);
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
