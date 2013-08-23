package com.railinc.r2dq.dataexception;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.dataexception.event.DataExceptionImplementedEvent;
import com.railinc.r2dq.dataexception.implementation.ImplementDataExceptionService;
import com.railinc.r2dq.domain.ApprovalDisposition;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.InboundMessage;
import com.railinc.r2dq.domain.InboundSource;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.log.SystemAuditLogTraceEvent;
import com.railinc.r2dq.messages.MessageService;
import com.railinc.r2dq.util.PagedCollection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-test-r2dq-h2.xml", "classpath:spring-r2dq-hibernate.xml" })
@TransactionConfiguration
@Transactional
public class DataExceptionServiceImplTest {
	@Autowired
	SessionFactory f;

	private DataExceptionServiceImpl si;
	private ApplicationContext applicationContext;
	private MessageService messageService;
	private ImplementDataExceptionService implementDataExceptionService;

	@Before
	public void setUp() throws Exception {
		si = new DataExceptionServiceImpl();
		si.setSessionFactory(this.f);
		applicationContext = mock(ApplicationContext.class);
		messageService = mock(MessageService.class);
		implementDataExceptionService = mock(ImplementDataExceptionService.class);
		si.setApplicationContext(applicationContext);
		si.setMessageService(messageService);
		si.setImplementDataExceptionService(implementDataExceptionService);

	}


	@Test
	public void testAll() {
		setupSearchExceptions(2);

		PagedCollection<DataException> all = si.all(null);
		assertEquals(2, all.size());
	}

	@Test
	public void testAll_Search_freeText() {
		final DataException one = exceptionTemplate(1);
		exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setFreeText(one.getMdmAttributevalue());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setFreeText(one.getMdmObjectAttribute());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setFreeText(one.getMdmObjectType());
		test_search_should_match(1, 5, c);

		
		c = new DataExceptionCriteria();
		c.setFreeText(one.getSourceSystemKeyColumn());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setFreeText(one.getSourceSystemKey());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setFreeText(one.getSourceSystemObjectData());
		test_search_should_match(1, 5, c);


		c = new DataExceptionCriteria();
		c.setFreeText(one.getSourceSystemValue());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setFreeText(one.getResponsiblePerson().getId());
		test_search_should_match(1, 5, c);
		

	}

	
	@Test
	public void testAll_Search_MdmAttributevalue() {
		final DataException one = exceptionTemplate(1);
		final DataException all = exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setMdmAttributevalue(one.getMdmAttributevalue());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setMdmAttributevalue(all.getMdmAttributevalue()).useLike();
		test_search_should_match(5, 5, c);
	}

	@Test
	public void testAll_Search_MdmObjectAttribute() {
		final DataException one = exceptionTemplate(1);
		final DataException all = exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setMdmObjectAttribute(one.getMdmObjectAttribute());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setMdmObjectAttribute(all.getMdmObjectAttribute()).useLike();
		test_search_should_match(5, 5, c);
	}


	@Test
	public void testAll_Search_MdmObjectType() {
		final DataException one = exceptionTemplate(1);
		final DataException all = exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setMdmObjectType(one.getMdmObjectType());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setMdmObjectType(all.getMdmObjectType()).useLike();
		test_search_should_match(5, 5, c);
	}
	

	@Test
	public void testAll_Search_sourceSystemKeyColumn() {
		final DataException one = exceptionTemplate(1);
		final DataException all = exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setSourceSystemKeyColumn(one.getSourceSystemKeyColumn());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setSourceSystemKeyColumn(all.getSourceSystemKeyColumn()).useLike();
		test_search_should_match(5, 5, c);
	}
	
	@Test
	@DirtiesContext
	public void testAll_Search_sourceSystemKeyValue() {
		final DataException one = exceptionTemplate(1);
		final DataException all = exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setSourceSystemKey(one.getSourceSystemKey());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setSourceSystemKey(all.getSourceSystemKey()).useLike();
		test_search_should_match(5, 5, c);
	}
	

	@Test
	@DirtiesContext
	public void testAll_Search_sourceSystemObjectData() {
		final DataException one = exceptionTemplate(1);
		final DataException all = exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setSourceSystemObjectData(one.getSourceSystemObjectData());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setSourceSystemObjectData(all.getSourceSystemObjectData()).useLike();
		test_search_should_match(5, 5, c);
	}


	@Test
	@DirtiesContext
	public void testAll_Search_sourceSystemValue() {
		final DataException one = exceptionTemplate(1);
		final DataException all = exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setSourceSystemValue(one.getSourceSystemValue());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setSourceSystemValue(all.getSourceSystemValue()).useLike();
		test_search_should_match(5, 5, c);
	}
	
	@Test
	@DirtiesContext
	public void testAll_Search_person() {
		final DataException one = exceptionTemplate(1);
		final DataException all = exceptionTemplate(Long.MIN_VALUE);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setPerson(one.getResponsiblePerson().getId());
		test_search_should_match(1, 5, c);

		c = new DataExceptionCriteria();
		c.setPerson(all.getResponsiblePerson().getId()).useLike();
		test_search_should_match(5, 5, c);
	}

	@Test
	@DirtiesContext
	public void testAll_Search_sourceSystem() {
		final DataException one = exceptionTemplate(1);
		one.setSource(createMessage("1"));
		one.setSourceSystem(createSourceSystem("1"));

		final DataException two = exceptionTemplate(2);
		two.setSource(createMessage("2"));
		two.setSourceSystem(createSourceSystem("2"));

		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setSourceSystem(one.getSourceSystem());
		PagedCollection<DataException> all = si.all(c);
		assertEquals( 1, all.size() );
	}

	@Test
	@DirtiesContext
	public void testAll_Search_ruleNumber() {
		final DataException one = exceptionTemplate(1);

		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setRuleNumber(one.getRuleNumber());
		test_search_should_match(1, 5, c);
	}


	@Test
	@DirtiesContext
	public void testAll_Search_personType() {
		final DataException one = exceptionTemplate(1);
		one.setSource(createMessage("1"));
		one.setSourceSystem(createSourceSystem("1"));
		one.setResponsiblePerson(new Identity(IdentityType.AskSourceSystem,null));

		final DataException two = exceptionTemplate(2);
		two.setSource(createMessage("2"));
		two.setSourceSystem(createSourceSystem("2"));
		two.setResponsiblePerson(new Identity(IdentityType.LocalGroup,"g1"));

		f.getCurrentSession().saveOrUpdate(one);
		f.getCurrentSession().saveOrUpdate(two);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(one);
		f.getCurrentSession().evict(two);
		
		DataExceptionCriteria c = new DataExceptionCriteria();
		c.setPersonType(one.getResponsiblePerson().getType());
		PagedCollection<DataException> all = si.all(c);
		assertEquals( 1, all.size() );
	}

	

	
	
	
	

	
	@SuppressWarnings("unchecked")
	public void test_search_should_match(int shouldMatch, int examples, DataExceptionCriteria c) {
		List<DataException> list = f.getCurrentSession().createCriteria(DataException.class).list();
		for (DataException de : list) {
			f.getCurrentSession().delete(de);
		}
		f.getCurrentSession().flush();
		list = f.getCurrentSession().createCriteria(DataException.class).list();
		assertEquals(0, list.size());

		setupSearchExceptions(examples);
		list = f.getCurrentSession().createCriteria(DataException.class).list();
		assertEquals(examples, list.size());

		
		PagedCollection<DataException> all = si.all(c);
		assertEquals("criteria should return " + shouldMatch + " records", shouldMatch, all.size());
	}

	private DataException createException(InboundMessage m1, SourceSystem ss, long suffix) {
		DataException de1 = exceptionTemplate(suffix);
		de1.setSource(m1);
		de1.setSourceSystem(ss);
		Session s = f.getCurrentSession();
		s.save(de1);
		s.flush();
		return de1;
	}

	public DataException exceptionTemplate(long ruleNumber) {
		String suffix = String.valueOf(ruleNumber);
		if (ruleNumber == Long.MIN_VALUE) {
			suffix = "";
		}
		DataException de1 = new DataException();
		de1.setMdmExceptionId(ruleNumber);
		de1.setResponsiblePerson(new Identity(IdentityType.SsoId, "sso" + suffix));
		de1.setSourceSystemValue("ssvalue" + suffix);
		de1.setSourceSystemKey("sskeyval" + suffix);
		de1.setSourceSystemKeyColumn("sskeycol" + suffix);
		de1.setSourceSystemObjectData("ssobjdata" + suffix);
		de1.setRuleNumber(ruleNumber);
		de1.setMdmObjectType("mdmobjtype" + suffix);
		de1.setMdmObjectAttribute("mdmobjattr" + suffix);
		de1.setMdmAttributeValue("mdmattvalue" + suffix);
		return de1;
	}

	public InboundMessage createMessage(String suffix, Session session) {
		InboundMessage m1 = new InboundMessage();
		m1.setSource(InboundSource.Unknown);
		m1.setData("data" + suffix);
		session.save(m1);
		session.flush();
		return m1;
	}
	public InboundMessage createMessage(String suffix) {
		Session s = f.getCurrentSession();
		return createMessage(suffix, s);
	}

	public SourceSystem createSourceSystem(String suffix) {
		Session s = f.getCurrentSession();
		return createSourceSystem(suffix, s);
	}	
	
	public SourceSystem createSourceSystem(String suffix, Session session) {
		SourceSystem object = (SourceSystem) session.get(SourceSystem.class, "id" + suffix);
		if (object != null) {
			return object;
		}
		
		SourceSystem m1 = new SourceSystem();
		m1.setIdentifier("id" + suffix);
		m1.setName("name" + suffix);
		
		session.save(m1);
		session.flush();
		return m1;
	}

	
	@Test
	@DirtiesContext
	public void testSave() {
		InboundMessage m1 = createMessage("1");
		SourceSystem s1 = createSourceSystem("1");
		DataException de1 = exceptionTemplate(1);
		de1.setSource(m1);
		de1.setSourceSystem(s1);
		de1.setMdmAttributeValue("new attribute value");
		de1.setId(null);
		si.save(de1);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(de1);

		DataException actual = (DataException) f.getCurrentSession().get(DataException.class, de1.getId());
		
		assertNotSame(de1, actual);
		assertEquals("new attribute value", actual.getMdmAttributevalue());
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext).publishEvent(systemAuditLog.capture());
		assertEquals("CREATE_DATA_EXCEPTION", systemAuditLog.getValue().getAction());
		assertEquals(de1.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(de1.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(de1.getSource().getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(m1.getIdentifier().toString(), systemAuditLog.getValue().getSourceEntityId());
	}

	@Test
	@DirtiesContext
	public void testDelete() {
		InboundMessage m1 = createMessage("1");
		SourceSystem s1 = createSourceSystem("1");
		DataException e = createException(m1, s1, 1);
		e.setMdmAttributeValue("new attribute value");

		si.save(e);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		DataException actual = (DataException) f.getCurrentSession().get(DataException.class, e.getId());
		si.delete(actual);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		actual = (DataException) f.getCurrentSession().get(DataException.class, e.getId());
		assertTrue(actual.isDeleted());
		
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		assertEquals("DELETE_DATA_EXCEPTION", systemAuditLog.getValue().getAction());
		assertEquals(e.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(e.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(e.getSource().getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(m1.getIdentifier().toString(), systemAuditLog.getValue().getSourceEntityId());
	}

	@Test
	@DirtiesContext
	public void testGet() {
		InboundMessage m1 = createMessage("1");
		SourceSystem s1 = createSourceSystem("1");
		
		DataException e = createException(m1, s1, 1);
		f.getCurrentSession().evict(e);

		DataException actual = (DataException) f.getCurrentSession().get(DataException.class, e.getId());
		assertNotNull(actual);
		assertNotSame(e, actual);
	}

	@Test
	@DirtiesContext
	public void testUndelete() {
		InboundMessage m1 = createMessage("1");
		SourceSystem s1 = createSourceSystem("1");
	
		DataException e = createException(m1, s1, 1);
		e.setMdmAttributeValue("new attribute value");

		si.save(e);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		DataException actual = (DataException) f.getCurrentSession().get(DataException.class, e.getId());
		si.delete(actual);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);
		actual = (DataException) f.getCurrentSession().get(DataException.class, e.getId());
		assertTrue("Deleted now", actual.isDeleted());
		f.getCurrentSession().evict(actual);

		actual = (DataException) f.getCurrentSession().get(DataException.class, e.getId());
		si.undelete(actual);
		f.getCurrentSession().flush();
		f.getCurrentSession().evict(e);

		actual = (DataException) f.getCurrentSession().get(DataException.class, e.getId());
		assertFalse("no longer deleted", actual.isDeleted());
		
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		assertEquals("UNDELETE_DATA_EXCEPTION", systemAuditLog.getValue().getAction());
		assertEquals(e.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(e.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(e.getSource().getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(m1.getIdentifier().toString(), systemAuditLog.getValue().getSourceEntityId());

	}
	
	@Test
	@DirtiesContext
	public void testMarkSourceAsProcessed(){
		DataException dataException = new DataException();
		dataException.setId(123L);
		InboundMessage source = new InboundMessage();
		source.setData("data");
		source.setSource(InboundSource.MDMException);
		source.setIdentifier(3L);
		this.f.getCurrentSession().save(source);
		dataException.setSource(source);
		
		si.markSourceAsProcessed(dataException);
		verify(messageService, times(1)).markAsProcessed(dataException.getSource());
	}
	
	@Test
	@DirtiesContext
	public void testApprove_withManualImplementation(){
		InboundMessage m1 = createMessage("1");
		SourceSystem s1 = createSourceSystem("1");
		DataException e = createException(m1, s1, 1);
		e.setImplementationType(ImplementationType.Manual);
		
		si.approve(e, "Approve Manual Implementation");
		
		assertEquals(ApprovalDisposition.Approved, e.getApprovalDisposition());
		ArgumentCaptor<DataExceptionImplementedEvent> dEIE = ArgumentCaptor.forClass(DataExceptionImplementedEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(dEIE.capture());
		assertNotNull(dEIE.getValue());
		
		verify(implementDataExceptionService, never()).implement(e);
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		assertEquals("MANUAL_IMPLMNT_DATA_EXCPTN", systemAuditLog.getValue().getAction());
		assertEquals(e.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(e.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(e.getSource().getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(m1.getIdentifier().toString(), systemAuditLog.getValue().getSourceEntityId());
		
	}
	
	@Test
	@DirtiesContext
	public void testApprove_withAutomatedImplementation_doSendNotificationToSourceSystemToImplement(){
		InboundMessage m1 = createMessage("1");
		SourceSystem s1 = createSourceSystem("1");
		DataException e = createException(m1, s1, 1);
		e.setImplementationType(ImplementationType.Automated);
		si.approve(e, "Approve Manual Implementation");
		
		assertEquals(ApprovalDisposition.Approved, e.getApprovalDisposition());
		ArgumentCaptor<DataExceptionImplementedEvent> dEIE = ArgumentCaptor.forClass(DataExceptionImplementedEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(dEIE.capture());
		assertNotNull(dEIE.getValue());
		
		verify(implementDataExceptionService).implement(e);
		
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		assertEquals("NOTFY_SS_TO_IMPLMNT_DATA_EXCPTN", systemAuditLog.getValue().getAction());
		assertEquals(e.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(e.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(e.getSource().getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(m1.getIdentifier().toString(), systemAuditLog.getValue().getSourceEntityId());
		
	}
	
	@Test
	@DirtiesContext
	public void testDisApprove_withAutomatedImplementation_doNotSendNotificationToSourceSystem(){
		InboundMessage m1 = createMessage("1");
		SourceSystem s1 = createSourceSystem("1");
		DataException e = createException(m1, s1, 1);
		e.setImplementationType(ImplementationType.Automated);
		
		si.disapprove(e, "Disapprove Automated Implementation");
		assertEquals(ApprovalDisposition.Disapproved, e.getApprovalDisposition());
		
		ArgumentCaptor<DataExceptionImplementedEvent> dEIE = ArgumentCaptor.forClass(DataExceptionImplementedEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(dEIE.capture());
		assertNotNull(dEIE.getValue());
		
		verify(implementDataExceptionService, never()).implement(e);
		
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		assertEquals("REJECT_DATA_EXCEPTION", systemAuditLog.getValue().getAction());
		assertEquals(e.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(e.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(e.getSource().getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(m1.getIdentifier().toString(), systemAuditLog.getValue().getSourceEntityId());
	}
	
	@Test
	@DirtiesContext
	public void testIgnore_withAutomatedImplementation_doNotSendNotificationToSourceSystem(){
		InboundMessage m1 = createMessage("1");
		SourceSystem s1 = createSourceSystem("1");
		DataException e = createException(m1, s1, 1);
		e.setImplementationType(ImplementationType.Automated);
		
		si.ignore(e, "Disapprove Automated Implementation");
		assertEquals(ApprovalDisposition.Ignored, e.getApprovalDisposition());
		
		ArgumentCaptor<DataExceptionImplementedEvent> dEIE = ArgumentCaptor.forClass(DataExceptionImplementedEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(dEIE.capture());
		assertNotNull(dEIE.getValue());
		
		verify(implementDataExceptionService, never()).implement(e);
		
		ArgumentCaptor<SystemAuditLogTraceEvent> systemAuditLog = ArgumentCaptor.forClass(SystemAuditLogTraceEvent.class);
		verify(applicationContext, atLeastOnce()).publishEvent(systemAuditLog.capture());
		assertEquals("IGNORE_DATA_EXCEPTION", systemAuditLog.getValue().getAction());
		assertEquals(e.getClass().getSimpleName(), systemAuditLog.getValue().getEntityName());
		assertEquals(e.getId().toString(), systemAuditLog.getValue().getEntityId());
		assertEquals(e.getSource().getClass().getSimpleName(), systemAuditLog.getValue().getSourceEntityName());
		assertEquals(m1.getIdentifier().toString(), systemAuditLog.getValue().getSourceEntityId());
	}
	
	private List<DataException> setupSearchExceptions(int count) {
		Session currentSession = f.getCurrentSession();
		List<DataException> results = newArrayList();
		for (int i = 0; i < count; i++) {


			DataException e = createException(createMessage(String.valueOf(i)), createSourceSystem(String.valueOf(i)), i);
			currentSession.evict(e.getSource());
			currentSession.evict(e);
			results.add(e);
		}
		return results;
	}

}
