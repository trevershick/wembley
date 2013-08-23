package com.railinc.wembley.legacy.rules.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.railinc.wembley.legacy.domain.dao.BaseDaoImpl;
import com.railinc.wembley.legacy.domain.dao.DaoConstants;
import com.railinc.wembley.legacy.rules.MailingListSubscriptionRuleBase;
import com.railinc.wembley.legacy.rules.MailingListSubscriptionRules;
import com.railinc.wembley.legacy.rules.SubscriberRuleCondition;
import com.railinc.wembley.legacy.rules.SubscriptionRuleAction;

public class MailingListSubscriptionRuleDaoImpl extends BaseDaoImpl 
	implements MailingListSubscriptionRuleDao, DaoConstants {

	private final Logger log = LoggerFactory.getLogger( MailingListSubscriptionRuleDaoImpl.class );

	private SubscriptionRulesFactory rulesFactory = null;
	
	private final ParameterizedRowMapper<MailingListSubscriptionRuleBase> appRowMapper =
		new ParameterizedRowMapper<MailingListSubscriptionRuleBase>() {
			
			public MailingListSubscriptionRuleBase mapRow( ResultSet arg0, int arg1 ) throws SQLException {
				
				String ruleId = arg0.getString( SUBRULES_COLUMN_RULE_ID );
				int ruleSeq = arg0.getInt( SUBRULES_COLUMN_RULE_SEQ );
				String ruleType = arg0.getString( SUBRULES_COLUMN_RULE_TYPE );
				SubscriberRuleCondition condition = SubscriberRuleCondition.valueOf( arg0.getString( SUBRULES_COLUMN_CONDITION ) );
				String conditionArg = arg0.getString( SUBRULES_COLUMN_CONDITION_ARG );
				SubscriptionRuleAction action = SubscriptionRuleAction.valueOf( arg0.getString( SUBRULES_COLUMN_ACTION ) );
				String actionArg = arg0.getString( SUBRULES_COLUMN_ACTION_ARG );
				
				MailingListSubscriptionRuleBase rule =
					rulesFactory.create( ruleId, ruleSeq, ruleType, condition, conditionArg, action, actionArg);
				
				return rule;
			}
	};
	
	public MailingListSubscriptionRules getMailingListSubscriptionRules() {

		String sql = String.format( "SELECT * FROM %s WHERE RULE_TYPE = 'MAILINGLIST' ORDER BY RULE_ID, RULE_SEQ", tableName() );
		List<MailingListSubscriptionRuleBase> rules = getSimpleJdbcTemplate().query( sql, appRowMapper );

		return new MailingListSubscriptionRules( rules );
	}

	@Override
	protected Logger getLog() {
		return log;
	}

	public String tableName() {
		return super.tableName( SUBRULES_TABLENAME_WO_SCHEMA );
	}

	public void setRulesFactory(SubscriptionRulesFactory rulesFactory) {
		this.rulesFactory = rulesFactory;
	}
}
