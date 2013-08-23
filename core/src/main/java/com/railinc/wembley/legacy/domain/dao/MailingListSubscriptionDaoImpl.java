package com.railinc.wembley.legacy.domain.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.railinc.wembley.legacysvc.domain.Delivery;
import com.railinc.wembley.legacysvc.domain.MailingListSubscription;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptionBaseImpl;
import com.railinc.wembley.legacysvc.domain.MailingListSubscriptions;
import com.railinc.wembley.legacysvc.domain.SubscriptionClass;
import com.railinc.wembley.legacysvc.domain.SubscriptionMode;

public class MailingListSubscriptionDaoImpl extends BaseDaoImpl implements MailingListSubscriptionDao,DaoConstants {

	public static final String N_A = "N/A";
	public static final String LIST_TYPE_MAILINGLIST = "MAILINGLIST";
	private final Logger log = LoggerFactory.getLogger(MailingListDaoImpl.class);
	private SubscriptionFactory factory;
	
	public MailingListSubscriptions getSubscriptionsForMailingList(
			String mailingListId) {
		SimpleJdbcTemplate simpleJdbcTemplate = getSimpleJdbcTemplate();
		String sql =
			String.format( "SELECT * FROM %s WHERE LIST_TYPE = ? AND TYPE_ARGUMENT = ? AND ACTIVE_FLAG = ?", tableName() );
		List<MailingListSubscription> query = simpleJdbcTemplate.query(sql, ML_ROWMAPPER, LIST_TYPE_MAILINGLIST, mailingListId, Y);
		return new MailingListSubscriptions(query);
	}


	public SubscriptionFactory getFactory() {
		return factory;
	}


	public void setFactory(SubscriptionFactory factory) {
		this.factory = factory;
	}


	@Override
	protected Logger getLog() {
		return log;
	}
	
	private final ParameterizedRowMapper<MailingListSubscription> ML_ROWMAPPER = 
		new ParameterizedRowMapper<MailingListSubscription>() {
			public MailingListSubscription mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				String mailingListId = arg0.getString(MLSUBSCRIPTION_COLUMN_TYPE_ARGUMENT);
				String mode = arg0.getString(MLSUBSCRIPTION_COLUMN_SUB_MODE);
				String subclass = arg0.getString(MLSUBSCRIPTION_COLUMN_SUB_CLASS);
				String subclassArg = arg0.getString(MLSUBSCRIPTION_COLUMN_SUB_CLASS_ARGUMENT);
				String delivery = arg0.getString(MLSUBSCRIPTION_COLUMN_DELIVERY_MECHANISM);
				String darg = arg0.getString(MLSUBSCRIPTION_COLUMN_DELIVERY_MECHANISM_ARGUMENT);
				String uid = arg0.getString(MLSUBSCRIPTION_COLUMN_SUBSCRIPTION_UID);
				SubscriptionMode m = SubscriptionMode.valueOf(mode);
				Delivery d = Delivery.valueOf(delivery);
				Date lastModified = arg0.getDate(MLSUBSCRIPTION_COLUMN_LAST_MODIFIED);
				MailingListSubscriptionBaseImpl create = getFactory().create(uid, mailingListId, subclass, subclassArg, m, d, darg);
				// i think the create method above should take just subclass and subclassa rg, then the rest can be set
				// via setXXX methods....
				create.setLastModified(lastModified);
				return create;
				
			}
		};

	public boolean deleteSubscription(String subscriptionId) {
		String sql = String.format("DELETE FROM %s WHERE SUBSCRIPTION_UID=?", tableName());
		int update = getJdbcTemplate().update(sql, new Object[]{subscriptionId});
		return (1 == update);
	}


	public String include(final String mailingListId, final SubscriptionClass subscriptionType,
			final String subscriptionTypeArg) {
		return includeOrExclude(mailingListId, subscriptionType, subscriptionTypeArg, SubscriptionMode.INCLUSION);
	}
	public String exclude(final String mailingListId, final SubscriptionClass subscriptionType,
			final String subscriptionTypeArg) {
		return includeOrExclude(mailingListId, subscriptionType, subscriptionTypeArg, SubscriptionMode.EXCLUSION);
	}

	private String includeOrExclude(final String mailingListId, 
			final SubscriptionClass subscriptionType,
			final String subscriptionTypeArg, 
			final SubscriptionMode mode) {
		return includeOrExclude(mailingListId, subscriptionType, subscriptionTypeArg, mode, null);
	}
	
	private String includeOrExclude(final String mailingListId, final SubscriptionClass type,
			final String subscriptionTypeArg, final SubscriptionMode mode, final String deliveryArgument) {

		
		String sql = String.format("INSERT INTO %s (SUBSCRIPTION_UID,LIST_TYPE,TYPE_ARGUMENT,SUB_MODE,SUB_CLASS,SUB_CLASS_ARGUMENT,DELIVERY_MECHANISM,DELIVERY_MECHANISM_ARGUMENT,ACTIVE_FLAG,LAST_MODIFIED) VALUES (?,?,?,?,?,?,?,?,?,?)", tableName());
		final String uid = UUID.randomUUID().toString();


		int updated = getJdbcTemplate().update(sql, new PreparedStatementSetter(){
			public void setValues(PreparedStatement arg0) throws SQLException {
				int col = 1;
				arg0.setString(col++,uid);
				arg0.setString(col++, LIST_TYPE_MAILINGLIST);
				arg0.setString(col++, mailingListId);
				arg0.setString(col++, mode.toString());
				arg0.setString(col++, type.toString());
				arg0.setString(col++, StringUtils.defaultString(subscriptionTypeArg,N_A));
				arg0.setString(col++, Delivery.EMAIL.toString());
				arg0.setString(col++, deliveryArgument);
				arg0.setString(col++, Y);
				arg0.setDate(col++,new java.sql.Date(System.currentTimeMillis()));
			}
		});
		if (0 == updated) {
			throw new IncorrectResultSetColumnCountException(1,0);
		}
		return uid;
	}


	public void updateMode(String subscriptionId, SubscriptionMode mode) {
		if (null == mode) {
			throw new IllegalArgumentException("mode is required");
		}
		String sql = String.format("UPDATE %s SET SUB_MODE=?,LAST_MODIFIED=? WHERE SUBSCRIPTION_UID=?", tableName());
		
		getJdbcTemplate().update(sql, new Object[]{mode.toString(), new java.sql.Date(System.currentTimeMillis()), subscriptionId});
		
	}
	

	public String tableName() {
		return super.tableName(MLSUBSCRIPTION_TABLE_NAME_WO_SCHEMA);
	}


	public void mailingListKeyChanged(String oldKey, String newKey) {
		String sql = String.format("UPDATE %s SET TYPE_ARGUMENT=? WHERE LIST_TYPE=? AND TYPE_ARGUMENT=?", tableName());
		getJdbcTemplate().update(sql, new Object[]{newKey, LIST_TYPE_MAILINGLIST, oldKey});
	}


	public MailingListSubscription getSubscription(String subscriptionKey) {
		if (null == subscriptionKey) { return null; }
		SimpleJdbcTemplate simpleJdbcTemplate = getSimpleJdbcTemplate();
		String sql =
			String.format( "SELECT * FROM %s WHERE SUBSCRIPTION_UID = ?", tableName() );
		try{ 
			return simpleJdbcTemplate.queryForObject(sql, ML_ROWMAPPER, subscriptionKey);
		} catch (IncorrectResultSizeDataAccessException i) {
			return null;
		}
	}


	public MailingListSubscription insert(String mailingListKey,
			SubscriptionMode mode, SubscriptionClass type, String typeArgument,
			String deliveryArgument) {
		String uuid = includeOrExclude(mailingListKey, type, typeArgument, mode, deliveryArgument);
		return getSubscription(uuid);
	}
}
