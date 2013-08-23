package com.railinc.wembley.legacy.domain.dao;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.railinc.wembley.legacy.service.NotificationSearchResults;
import com.railinc.wembley.legacysvc.domain.NotificationVo;

public class NotificationDaoImpl extends BaseDaoImpl implements NotificationDao,DaoConstants {
	private final Logger log = LoggerFactory.getLogger(NotificationDaoImpl.class);


	private final ParameterizedRowMapper<NotificationVo> eventRowMapper = new ParameterizedRowMapper<NotificationVo>() {
		public NotificationVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			NotificationVo event = new NotificationVo();


			Clob xmlClob = rs.getClob("DELIVERY_SPEC");

			try {
				event.setDeliverySpecification(IOUtils.toByteArray(xmlClob.getAsciiStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (event != null) {
				event.setAppId(rs.getString(NOTIFICATION_COLUMN_APP_ID));
				event.setNotificationUid(rs.getString(NOTIFICATION_COLUMN_NOTIFICATION_UID));
				event.setEventUid(rs.getString(NOTIFICATION_COLUMN_EVENT_UID));
				event.setRetryCount(rs.getInt(NOTIFICATION_COLUMN_RETRY_COUNT));
				event.setDeliveryTiming(rs.getInt(NOTIFICATION_COLUMN_DELIVERY_TIMING));
				event.setState(rs.getString(NOTIFICATION_COLUMN_STATE));
				event.setStateTimestamp(rs.getTimestamp(NOTIFICATION_COLUMN_STATE_TIMESTAMP));
				event.setCreatedTimestamp(rs.getTimestamp(NOTIFICATION_COLUMN_CREATED_TIMESTAMP));
			}

			return event;
		}
	};

	@Override
	protected Logger getLog() {
		return this.log;
	}

	public String tableName() {
		return super.tableName(DaoConstants.NOTIFICATIONS_TABLE_NAME_WO_SCHEMA);
	}


	public List<NotificationVo> getNotificationsByEvent(String eventUid) {
		String sql = String.format("SELECT * FROM %s WHERE EVENT_UID=?",tableName());
		
		
		return getSimpleJdbcTemplate().query(
				sql,
				this.eventRowMapper, eventUid);
	}

	protected String transformWildcard(String in) {
		String out = in.replaceAll("\\*+","%");
		out = out.replaceAll("\\?+", "_");
		return out;
	}
	public NotificationSearchResults findNotificationsByDeliverySpec(
			String eventUid, 
			String deliverySpecPartial,
			String token, int max) {
		
		if (eventUid == null || deliverySpecPartial == null) {
			throw new IllegalArgumentException("eventUid and deliverySpecPartial cannot be null");
		}
		List<Object> args = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder("SELECT * FROM %s WHERE EVENT_UID = ? AND DELIVERY_SPEC LIKE ? ");
		args.add(eventUid);
		args.add(transformWildcard(deliverySpecPartial));
		
		if (token != null){ 
			sb.append(" AND NOTIFICATION_UID > ?");
			args.add(token);
		}
		sb.append(" ORDER BY NOTIFICATION_UID");

		String sql = String.format(sb.toString(),tableName());
		
		SimpleJdbcTemplate simpleJdbcTemplate = createBoundedJdbcTemplate(max);
		
		List<NotificationVo> res = simpleJdbcTemplate.query(sql, 
				this.eventRowMapper, args.toArray());
		String responseToken = null;
		
		if (res.size() == max) {
			responseToken = res.get(res.size()-1).getNotificationUid(); 
		}
		return new NotificationSearchResults(res,responseToken);

	}

	private SimpleJdbcTemplate createBoundedJdbcTemplate(int max) {
		JdbcTemplate jt = new JdbcTemplate(getJdbcTemplate().getDataSource());
		jt.setMaxRows(max);
		return new SimpleJdbcTemplate(jt);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getStatusCountsByEventId(String eventUid) {
		String sql = String.format("SELECT STATE,COUNT(NOTIFICATION_UID) AS CT FROM %s WHERE EVENT_UID = ? GROUP BY STATE", tableName());
		List<Map<String, Object>> results = getSimpleJdbcTemplate().getJdbcOperations().queryForList(sql, new Object[]{eventUid});
		Map<String, Integer> ret = new HashMap<String, Integer>();
		for (Map m : results) {
			String state = (String) m.get(NOTIFICATION_COLUMN_STATE);
			Number number = (Number) m.get("CT");
			ret.put(state, Integer.valueOf(number.intValue()));
		}
		return ret;
	}

	public int markNotificationsCancelledForEvent(String eventUid) {
		if (null == eventUid) { 
			throw new IllegalArgumentException("eventUid cannot be null");
		}
		String sql = "UPDATE %s SET STATE=?, STATE_TIMESTAMP=? WHERE EVENT_UID=? AND STATE=?";
		sql = String.format(sql, tableName());
		return getSimpleJdbcTemplate().update(sql, 
				NOTIFICATION_STATUS_CANCELLED,
				new Date(),
				eventUid,
				NOTIFICATION_STATUS_OPEN);
		
	}
}
