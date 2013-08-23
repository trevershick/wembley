package com.railinc.r2dq.responsibility;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.Responsibility;
import com.railinc.r2dq.domain.RuleNumberType;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.util.PagedCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-r2dq-h2.xml", "classpath:spring-r2dq-hibernate.xml" })
@TransactionConfiguration
@Transactional
public class ResponsibilityServiceImplTest {
	@Autowired
	SessionFactory f;


	private ResponsibilityServiceImpl si;

	@Before
	public void setUp() throws Exception {
		si = new ResponsibilityServiceImpl();
		si.setSessionFactory(f);

	}


	@Test
	public void testAll() {
		setupSearchResponsibilities(2);

		PagedCollection<Responsibility> all = si.all(null);
		assertEquals(2, all.size());
	}

	@Test
	public void testAll_Search_freeText() {
		final Responsibility one = responsibilityTemplate(1);

		ResponsibilityCriteria c = new ResponsibilityCriteria();
		c.setFreeText(one.getResponsiblePersonId());
		test_search_should_match(1, 5, c);
	}


	



	@Test
	public void testAll_Search_sourceSystem() {
		final Responsibility one = responsibilityTemplate(1);
		one.setSourceSystem(createSourceSystem("1"));

		final Responsibility two = responsibilityTemplate(2);
		two.setSourceSystem(createSourceSystem("2"));

		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		ResponsibilityCriteria c = new ResponsibilityCriteria();
		c.setSourceSystem(one.getSourceSystem());
		PagedCollection<Responsibility> all = si.all(c);
		assertEquals( 1, all.size() );
	}



	@Test
	public void testAll_Search_ruleNumberType() {
		final Responsibility one = responsibilityTemplate(1);
		one.setSourceSystem(createSourceSystem("1"));
		one.setRuleNumberType(RuleNumberType.RANGE);

		final Responsibility two = responsibilityTemplate(2);
		two.setSourceSystem(createSourceSystem("2"));
		two.setRuleNumberType(RuleNumberType.SINGLE);
		
		
		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		ResponsibilityCriteria c = new ResponsibilityCriteria();
		c.setRuleNumberType(RuleNumberType.SINGLE);
		PagedCollection<Responsibility> all = si.all(c);
		assertEquals( 1, all.size() );
	}

	

	@Test
	public void testAll_Search_ruleNumberFrom() {
		final Responsibility one = responsibilityTemplate(1);
		one.setRuleNumberFrom(3L);

		final Responsibility two = responsibilityTemplate(2);
		two.setRuleNumberFrom(5L);
		
		
		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		ResponsibilityCriteria c = new ResponsibilityCriteria();
		c.setRuleNumberFrom(3L);
		PagedCollection<Responsibility> all = si.all(c);
		assertEquals( 1, all.size() );
	}

	
	@Test
	public void testAll_Search_ruleNumberThru() {
		final Responsibility one = responsibilityTemplate(1);
		one.setRuleNumberThru(3L);

		final Responsibility two = responsibilityTemplate(2);
		two.setRuleNumberThru(5L);
		
		
		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		ResponsibilityCriteria c = new ResponsibilityCriteria();
		c.setRuleNumberThru(3L);
		PagedCollection<Responsibility> all = si.all(c);
		assertEquals( 1, all.size() );
	}
	
	@Test
	public void testAll_Search_ruleNumber() {
		final Responsibility one = responsibilityTemplate(1);
		one.setRuleNumberFrom(3L);

		final Responsibility two = responsibilityTemplate(2);
		two.setRuleNumberFrom(5L);
		
		
		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		ResponsibilityCriteria c = new ResponsibilityCriteria();
		c.setRuleNumber(3L);
		PagedCollection<Responsibility> all = si.all(c);
		assertEquals( 1, all.size() );
	}

	@Test
	public void testAll_Search_person() {
		final Responsibility one = responsibilityTemplate(1);
		

		ResponsibilityCriteria c = new ResponsibilityCriteria();
		c.setPerson(one.getResponsiblePersonId());
		test_search_should_match(1, 5, c);
	}

	
	@Test
	public void testAll_Search_personType() {
		final Responsibility one = responsibilityTemplate(1);
		one.setResponsiblePersonType(IdentityType.AskSourceSystem);

		final Responsibility two = responsibilityTemplate(2);
		two.setResponsiblePersonType(IdentityType.LocalGroup);
		
		
		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		ResponsibilityCriteria c = new ResponsibilityCriteria();
		c.setPersonType(IdentityType.LocalGroup);
		PagedCollection<Responsibility> all = si.all(c);
		assertEquals( 1, all.size() );
	}


	

	
	@SuppressWarnings("unchecked")
	public void test_search_should_match(int shouldMatch, int examples, ResponsibilityCriteria c) {
		
		List<Responsibility> list = f.getCurrentSession().createCriteria(Responsibility.class).list();
		for (Responsibility de : list) {
			f.getCurrentSession().delete(de);
		}
		f.getCurrentSession().flush();
		list = f.getCurrentSession().createCriteria(Responsibility.class).list();
		assertEquals(0, list.size());

		setupSearchResponsibilities(examples);
		list = f.getCurrentSession().createCriteria(Responsibility.class).list();
		assertEquals(examples, list.size());

		
		PagedCollection<Responsibility> all = si.all(c);
		assertEquals("criteria should return " + shouldMatch + " records", shouldMatch, all.size());
	}

	@SuppressWarnings("unused")
	private void evict(Object... objs) {
		for (Object o : objs) {
			f.getCurrentSession().evict(o);
		}

	}

	private Responsibility createResponsibility(SourceSystem ss, long suffix) {
		Responsibility de1 = responsibilityTemplate(suffix);
		de1.setSourceSystem(ss);
		Session s = f.getCurrentSession();
		s.save(de1);
		s.flush();
		return de1;
	}

	private Responsibility responsibilityTemplate(long ruleNumber) {
		String suffix = String.valueOf(ruleNumber);
		if (ruleNumber == Long.MIN_VALUE) {
			suffix = "";
		}
		Responsibility de1 = new Responsibility();
		de1.setResponsiblePerson(new Identity(IdentityType.SsoId, "sso" + suffix));
		de1.setRuleNumberFrom(ruleNumber);
		de1.setRuleNumberThru(ruleNumber + 1);
		de1.setRuleNumberType(RuleNumberType.RANGE);
		
		return de1;
	}


	
	private SourceSystem createSourceSystem(String suffix) {
		SourceSystem object = (SourceSystem) f.getCurrentSession().get(SourceSystem.class, "id" + suffix);
		if (object != null) {
			return object;
		}
		
		SourceSystem m1 = new SourceSystem();
		m1.setIdentifier("id" + suffix);
		m1.setName("name" + suffix);
		Session s = f.getCurrentSession();
		s.save(m1);
		s.flush();
		return m1;
	}

	
	@Test
	public void testSave() {
		SourceSystem s1 = createSourceSystem("1");
		Responsibility e = createResponsibility(s1, 1);
		e.setNoteText("new attribute value");

		si.save(e);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		Responsibility actual = (Responsibility) f.getCurrentSession().get(Responsibility.class, e.getId());
		assertNotSame(e, actual);
		assertEquals("new attribute value", actual.getNoteText());
	}

	@Test
	public void testDelete() {
		SourceSystem s1 = createSourceSystem("1");
		Responsibility e = createResponsibility(s1, 1);
		e.setNoteText("new attribute value");

		si.save(e);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		Responsibility actual = (Responsibility) f.getCurrentSession().get(Responsibility.class, e.getId());
		si.delete(actual);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		actual = (Responsibility) f.getCurrentSession().get(Responsibility.class, e.getId());
		assertTrue(actual.isDeleted());
	}

	@Test
	public void testGet() {
		SourceSystem s1 = createSourceSystem("1");
		
		Responsibility e = createResponsibility(s1, 1);
		f.getCurrentSession().evict(e);

		Responsibility actual = (Responsibility) f.getCurrentSession().get(Responsibility.class, e.getId());
		assertNotNull(actual);
		assertNotSame(e, actual);
	}

	@Test
	public void testUndelete() {
		SourceSystem s1 = createSourceSystem("1");
		Responsibility e = createResponsibility(s1, 1);
		e.setNoteText("new attribute value");

		si.save(e);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		Responsibility actual = (Responsibility) f.getCurrentSession().get(Responsibility.class, e.getId());
		si.delete(actual);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);
		actual = (Responsibility) f.getCurrentSession().get(Responsibility.class, e.getId());
		assertTrue("Deleted now", actual.isDeleted());
		f.getCurrentSession().evict(actual);

		actual = (Responsibility) f.getCurrentSession().get(Responsibility.class, e.getId());
		si.undelete(actual);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		actual = (Responsibility) f.getCurrentSession().get(Responsibility.class, e.getId());
		assertFalse("no longer deleted", actual.isDeleted());

	}
	private List<Responsibility> setupSearchResponsibilities(int count) {
		Session currentSession = f.getCurrentSession();
		List<Responsibility> results = newArrayList();
		for (int i = 0; i < count; i++) {


			Responsibility e = createResponsibility(createSourceSystem(String.valueOf(i)), i);
			currentSession.evict(e);
			results.add(e);
		}
		return results;
	}

}
