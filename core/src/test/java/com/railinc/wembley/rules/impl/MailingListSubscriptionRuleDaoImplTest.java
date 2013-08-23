package com.railinc.wembley.rules.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.railinc.wembley.domain.dao.impl.HypersonicDbTest;
import com.railinc.wembley.legacy.domain.dao.DaoConstants;
import com.railinc.wembley.legacy.rules.MailingListSubscriptionRuleBase;
import com.railinc.wembley.legacy.rules.MailingListSubscriptionRules;
import com.railinc.wembley.legacy.rules.SubscriberRuleCondition;
import com.railinc.wembley.legacy.rules.SubscriptionRuleAction;
import com.railinc.wembley.legacy.rules.dao.MailingListSubscriptionRuleDaoImpl;
import com.railinc.wembley.legacy.rules.dao.SubscriptionRulesFactory;

public class MailingListSubscriptionRuleDaoImplTest extends HypersonicDbTest {

	private MailingListSubscriptionRuleDaoImpl dao;

	@Before
	public void setUp() throws Exception {
		dao = new MailingListSubscriptionRuleDaoImpl();
		dao.setDataSource(getDataSource());
		dao.setRulesFactory(new SubscriptionRulesFactory());
		getJdbcTemplate().execute("DELETE FROM " + DaoConstants.SUBRULES_TABLENAME_WO_SCHEMA);
	}

	@After
	public void tearDown() throws Exception {
		getJdbcTemplate().execute("DELETE FROM " + DaoConstants.SUBRULES_TABLENAME_WO_SCHEMA);
	}

	@Test
	public void testGetMailingListSubscriptionRules() {
		
		String sql2 = "INSERT INTO SUBSCRIPTION_RULES ( RULE_ID, RULE_SEQ, CONDITION, CONDITION_ARG, ACTION, ACTION_ARG,RULE_TYPE,CREATED_TIMESTAMP ) VALUES ( 'BNSFRULE', 1, 'DELIVERY_ARGUMENT', '@bnsf.com', 'SUBSTITUTE', 'admin@bnsf.com',  'MAILINGLIST',?)";
		String sql1 = "INSERT INTO SUBSCRIPTION_RULES ( RULE_ID, RULE_SEQ, CONDITION, CONDITION_ARG, ACTION, ACTION_ARG,RULE_TYPE,CREATED_TIMESTAMP ) VALUES ( 'BNSFRULE', 2, 'DELIVERY_ARGUMENT', 'admin@bnsf.com', 'AGGREGATE', '1', 'MAILINGLIST',?)"; 
		
		getJdbcTemplate().update(sql1, new Object[]{new Date()});
		getJdbcTemplate().update(sql2, new Object[]{new Date()});
		
		MailingListSubscriptionRules rules = dao.getMailingListSubscriptionRules();
		assertEquals(2, rules.size());
		
		MailingListSubscriptionRuleBase m = rules.get(0);
		assertEquals("BNSFRULE", m.ruleId());
		assertEquals(1, m.ruleSequence());
		assertEquals("MAILINGLIST", m.ruleType());
		assertEquals(SubscriberRuleCondition.DELIVERY_ARGUMENT, m.condition());
		assertEquals("@bnsf.com", m.conditionArgument());
		assertEquals("admin@bnsf.com", m.actionArgument());
		assertEquals(SubscriptionRuleAction.SUBSTITUTE, m.action());

		m = rules.get(1);
		assertEquals("BNSFRULE", m.ruleId());
		assertEquals(2, m.ruleSequence());
		assertEquals("MAILINGLIST", m.ruleType());
		assertEquals(SubscriberRuleCondition.DELIVERY_ARGUMENT, m.condition());
		assertEquals("admin@bnsf.com", m.conditionArgument());
		assertEquals("1", m.actionArgument());
		assertEquals(SubscriptionRuleAction.AGGREGATE, m.action());

	}

}
