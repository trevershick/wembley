package com.railinc.r2dq.message;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.GenericMessage;
import com.railinc.r2dq.domain.InboundSource;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.YesNo;
import com.railinc.r2dq.domain.views.RawInboundMessageView;
import com.railinc.r2dq.integration.Queue;
import com.railinc.r2dq.log.SystemAuditLogTraceEvent;
import com.railinc.r2dq.messages.MessageSearchCriteria;
import com.railinc.r2dq.messages.MessageServiceImpl;
import com.railinc.r2dq.util.PagedCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-r2dq-h2.xml", "classpath:spring-r2dq-hibernate.xml" })
@TransactionConfiguration
@Transactional
public class MessageServiceImplTest {
	@Autowired
	SessionFactory f;


	private MessageServiceImpl si;
	private ApplicationContext applicationContext;

	@Before
	public void setUp() throws Exception {
		si = new MessageServiceImpl();
		si.setSessionFactory(f);
		applicationContext = mock(ApplicationContext.class);
		si.setApplicationContext(applicationContext);

	}

	@Test
	public void test_send_message() {
		Queue q = mock(Queue.class);
		si.setQueue(q);
	
		si.sendMessage("test");
		verify(q).sendMessage("test");
	}

	
	@Test
	public void test_get() {
		final InboundMessage one = responsibilityTemplate(1);
		one.setProcessed(YesNo.Y);
		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().flush();
		long id = one.getIdentifier();
		
		f.getCurrentSession().evict(one);
		
		GenericMessage rawInboundMessage = si.get(id);
		assertNotNull("got it", rawInboundMessage);
		
	}
	
	
	
	@Test
	public void testAll() {
		setupSearchResponsibilities(2);
		PagedCollection<RawInboundMessageView> all = si.all(null);
		assertEquals(2, all.size());
	}
	
	

	@Test
	public void test_search_data() {
		final InboundMessage exampleRecordFromTemplate = responsibilityTemplate(1);


		MessageSearchCriteria c = new MessageSearchCriteria();
		c.addData(exampleRecordFromTemplate.getData());
		test_search_should_match(1, 5, c);
	}


	


	@Test
	public void testAll_search_processed() {
		final InboundMessage one = responsibilityTemplate(1);
		one.setProcessed(YesNo.Y);
		final InboundMessage two = responsibilityTemplate(2);
		two.setProcessed(YesNo.N);
		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);

		

		MessageSearchCriteria c = new MessageSearchCriteria();
		c.setProcessed(true);
		
		PagedCollection<RawInboundMessageView> all = si.all(c);
		assertEquals("Should only find one of the records", 1, all.size());
	}

	
	@Test
	public void testAll_Search_source() {
		final InboundMessage one = responsibilityTemplate(1);
		one.setSource(InboundSource.MDMException);
		

		final InboundMessage two = responsibilityTemplate(2);
		two.setSource(InboundSource.Unknown);
		
		
		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		MessageSearchCriteria c = new MessageSearchCriteria();
		c.addInboundSource(InboundSource.Unknown);
		
		PagedCollection<RawInboundMessageView> all = si.all(c);
		assertEquals( 1, all.size() );
	}


	

	
	@SuppressWarnings("unchecked")
	public void test_search_should_match(int shouldMatch, int examples, MessageSearchCriteria c) {
		
		List<InboundMessage> list = f.getCurrentSession().createCriteria(InboundMessage.class).list();
		for (InboundMessage de : list) {
			f.getCurrentSession().delete(de);
		}
		f.getCurrentSession().flush();
		list = f.getCurrentSession().createCriteria(InboundMessage.class).list();
		assertEquals(0, list.size());

		setupSearchResponsibilities(examples);
		list = f.getCurrentSession().createCriteria(InboundMessage.class).list();
		assertEquals(examples, list.size());

		
		PagedCollection<RawInboundMessageView> all = si.all(c);
		assertEquals("criteria should return " + shouldMatch + " records", shouldMatch, all.size());
	}

	@SuppressWarnings("unused")
	private void evict(Object... objs) {
		for (Object o : objs) {
			f.getCurrentSession().evict(o);
		}

	}


	private InboundMessage responsibilityTemplate(long ruleNumber) {
		String suffix = String.valueOf(ruleNumber);
		if (ruleNumber == Long.MIN_VALUE) {
			suffix = "";
		}
		InboundMessage de1 = new InboundMessage();
		de1.setData("data" + suffix);
		de1.setSource(InboundSource.MDMException);
		return de1;
	}

	@Test
	public void testSave() {

		InboundMessage e = responsibilityTemplate(1);
		e.setData("new attribute value");

		si.save(e);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		InboundMessage actual = (InboundMessage) f.getCurrentSession().get(InboundMessage.class, e.getIdentifier());
		assertNotSame(e, actual);
		assertEquals("new attribute value", actual.getData());
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext).publishEvent(systemAuditLog.capture());
		assertEquals(e.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(e.getIdentifier().toString(), systemAuditLog.getValue().getEntityId());
		assertNull(systemAuditLog.getValue().getSourceEntityName());
		assertNull(systemAuditLog.getValue().getSourceEntityId());
	}

	@Test
	public void testDelete() {
		InboundMessage e = responsibilityTemplate(1);
		e.setData("new attribute value");

		si.save(e);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		InboundMessage actual = (InboundMessage) f.getCurrentSession().get(InboundMessage.class, e.getIdentifier());
		si.delete(actual);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		actual = (InboundMessage) f.getCurrentSession().get(InboundMessage.class, e.getIdentifier());
		assertNull("Should be gone.", actual);
		
	}

	@Test
	public void testGet() {
		InboundMessage e = responsibilityTemplate(1);
		this.f.getCurrentSession().save(e);
		this.f.getCurrentSession().flush();
		this.f.getCurrentSession().evict(e);
		
		InboundMessage actual = (InboundMessage) f.getCurrentSession().get(InboundMessage.class, e.getIdentifier());
		assertNotNull(actual);
		assertNotSame(e, actual);
	}
	
	
	@Test
	public void testMarkAsProcessed(){
		InboundMessage inboundMessage = new InboundMessage();
		inboundMessage.setData("data");
		inboundMessage.setSource(InboundSource.MDMException);
		inboundMessage.setIdentifier(3L);
		si.markAsProcessed(inboundMessage);
		assertTrue(inboundMessage.isProcessed());
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext).publishEvent(systemAuditLog.capture());
		assertEquals("MARK_SOURCE_AS_PROCESSED", systemAuditLog.getValue().getAction());
		assertEquals(inboundMessage.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(inboundMessage.getIdentifier().toString(), systemAuditLog.getValue().getEntityId());
	}

	private List<InboundMessage> setupSearchResponsibilities(int count) {
		Session currentSession = f.getCurrentSession();
		List<InboundMessage> results = newArrayList();
		for (int i = 0; i < count; i++) {

			InboundMessage e = responsibilityTemplate(i);
			currentSession.save(e);
			currentSession.flush();
			currentSession.evict(e);
			results.add(e);
		}
		return results;
	}

}
