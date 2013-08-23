package com.railinc.wembley.domain.dao.impl;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.fail;

import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.railinc.wembley.legacy.domain.dao.MailingListSubscriptionDaoImpl;
import com.railinc.wembley.legacy.domain.dao.SubscriptionFactory;
import com.railinc.wembley.legacy.domain.impl.sso.SSO;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserSubscription;
import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.SubscriptionClass;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class MailingListSubscriptionDaoImplTest extends HypersonicDbTest {

	MailingListSubscriptionDaoImpl dao = null;
	private SSO sso;
	
	@Before
	public void setupBefore() {
		dao = new MailingListSubscriptionDaoImpl();
		dao.setDataSource(getDataSource());
		
		sso = EasyMock.createNiceMock(SSO.class);
		SubscriptionFactory f = new SubscriptionFactory();
		f.setSso(sso);
		dao.setFactory(f);
	}
	
	@Test
	public void testDeleteSubscription() {
		String id = dao.include("MLKEY", SubscriptionClass.SSOUser, "EMAIL");

		MailingListSubscription m = dao.getSubscription(id);
		assertNotNull(m);

		dao.deleteSubscription(id);
		m = dao.getSubscription(id);
		assertNull(m);
		
	}

	@Test
	public void testInclude() {
		String id = dao.include("MLKEY", SubscriptionClass.SSOUser, "EMAIL");

		MailingListSubscription m = dao.getSubscription(id);
		assertNotNull(m);
		assertEquals(SubscriptionMode.INCLUSION, m.mode());
		assertEquals("MLKEY", m.getMailingListKey());
		assertEquals(SSOUserSubscription.class, m.getClass());
		assertEquals(Delivery.EMAIL, m.delivery());
	}
	
	

	@Test
	public void testExclude() {
		String id = dao.exclude("MLKEY", SubscriptionClass.SSOUser, "EMAIL");
		MailingListSubscription m = dao.getSubscription(id);
		assertNotNull(m);
		assertEquals(SubscriptionMode.EXCLUSION, m.mode());
		assertEquals("MLKEY", m.getMailingListKey());
		assertEquals(SSOUserSubscription.class, m.getClass());
		assertEquals(Delivery.EMAIL, m.delivery());
	}

	@Test
	public void testUpdateMode() {
		MailingListSubscription s = dao.insert("MLKEY", 
				SubscriptionMode.INCLUSION, 
				SubscriptionClass.SSOUser, "EMAIL", "tshick@hotmail.com");

		MailingListSubscription m = dao.getSubscription(s.key());
		assertNotNull(m);
		assertEquals(SubscriptionMode.INCLUSION, m.mode());
		
		dao.updateMode(s.key(), SubscriptionMode.EXCLUSION);
		m = dao.getSubscription(s.key());
		assertEquals(SubscriptionMode.EXCLUSION, m.mode());

		dao.updateMode(s.key(), SubscriptionMode.INCLUSION);
		m = dao.getSubscription(s.key());
		assertEquals(SubscriptionMode.INCLUSION, m.mode());

		try {
			dao.updateMode(s.key(), null);
			fail("Should throw an iae");
		} catch (IllegalArgumentException iae) {
			// good
		}
	}

	
	
	
	
	@Test
	public void testMailingListKeyChanged() {
		MailingListSubscription s = dao.insert("MLKEY", 
				SubscriptionMode.INCLUSION, 
				SubscriptionClass.SSOUser, "EMAIL", "tshick@hotmail.com");

		MailingListSubscription m = dao.getSubscription(s.key());
		assertEquals("MLKEY", m.getMailingListKey());

		dao.mailingListKeyChanged("MLKEY", "NEWMLKEY");
		m = dao.getSubscription(s.key());
		assertEquals("NEWMLKEY", m.getMailingListKey());
		
		
	}

	@Test
	public void testGetSubscription_Null_Id() {
		assertNull(dao.getSubscription(null));
	}
	
	
	
	@Test
	public void testGetSubscription() {
		MailingListSubscription s = dao.insert("MLKEY", 
				SubscriptionMode.INCLUSION, 
				SubscriptionClass.SSOUser, "EMAIL", "tshick@hotmail.com");

		MailingListSubscription m = dao.getSubscription(s.key());
		assertNotNull(m);
		assertEquals(SubscriptionMode.INCLUSION, m.mode());


		assertEquals("MLKEY", m.getMailingListKey());
		assertEquals(s.key(),m.key());
		assertEquals(SSOUserSubscription.class, m.getClass());
		assertEquals(Delivery.EMAIL, m.delivery());
		assertEquals("tshick@hotmail.com", m.deliveryArgument()); 		
	}

	@Test
	public void testInsert() {
		
		MailingListSubscription s = dao.insert("MLKEY", 
				SubscriptionMode.INCLUSION, 
				SubscriptionClass.SSOUser, "EMAIL", "tshick@hotmail.com");
		Map m = getJdbcTemplate().queryForMap("SELECT * FROM " + dao.tableName() + " WHERE " + dao.MLSUBSCRIPTION_COLUMN_SUBSCRIPTION_UID + "=?", new Object[]{s.key()} );
		assertNotNull(m);
		assertEquals(SubscriptionMode.INCLUSION.toString(), m.get(dao.MLSUBSCRIPTION_COLUMN_SUB_MODE));
		
		
		assertEquals("MLKEY", m.get(dao.MLSUBSCRIPTION_COLUMN_TYPE_ARGUMENT));
		assertEquals(SubscriptionClass.SSOUser.toString(), m.get(dao.MLSUBSCRIPTION_COLUMN_SUB_CLASS));
		assertEquals("EMAIL", m.get(dao.MLSUBSCRIPTION_COLUMN_DELIVERY_MECHANISM));
		assertEquals("tshick@hotmail.com", m.get(dao.MLSUBSCRIPTION_COLUMN_DELIVERY_MECHANISM_ARGUMENT));


		// check exclusions
		s = dao.insert("MLKEY", 
				SubscriptionMode.EXCLUSION, 
				SubscriptionClass.SSOUser, "EMAIL", "tshick@hotmail.com");
		m = getJdbcTemplate().queryForMap("SELECT * FROM " + dao.tableName() + " WHERE " + dao.MLSUBSCRIPTION_COLUMN_SUBSCRIPTION_UID + "=?", new Object[]{s.key()} );
		assertNotNull(m);
		assertEquals(SubscriptionMode.EXCLUSION.toString(), m.get(dao.MLSUBSCRIPTION_COLUMN_SUB_MODE));
		
	}

}
