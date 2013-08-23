package com.railinc.wembley.legacy.domain.dao;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.railinc.wembley.legacysvc.domain.EventVo;

public class EventDaoImpl extends BaseDaoImpl implements EventDao {

	


	private final Logger log = LoggerFactory.getLogger(EventDaoImpl.class);


	private final ParameterizedRowMapper<EventVo> eventRowMapper = new ParameterizedRowMapper<EventVo>() {
		public EventVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			EventVo event = new EventVo();

			Clob xmlClob = rs.getClob("EVENT_XML");

			try {
				event.setContents(IOUtils.toByteArray(xmlClob.getAsciiStream()));
			} catch (IOException e) {
				// wewant to return even if we can't get the content
				e.printStackTrace();
			}

			event.setEventUid(rs.getString("EVENT_UID"));
			event.setRetryCount(rs.getInt("RETRY_COUNT"));
			event.setState(rs.getString("STATE"));
			event.setStateTimestamp(rs.getTimestamp("STATE_TIMESTAMP"));
			event.setAppId(rs.getString("APP_ID"));
			event.setCorrelationId(rs.getString("CORRELATION_ID"));
			event.setSendAfter(rs.getTimestamp("SEND_AFTER"));
			return event;
		}
	};

	@Override
	protected Logger getLog() {
		return this.log;
	}

	public String tableName() {
		return super.tableName(DaoConstants.EVENTS_TABLE_NAME_WO_SCHEMA);
	}
	
	public EventVo getEvent(String uid) {
		String sql = String.format("SELECT * FROM %s WHERE EVENT_UID=?",tableName());
		try {
			return getSimpleJdbcTemplate().queryForObject(
					sql, 
					this.eventRowMapper, uid);
		} catch (EmptyResultDataAccessException dae) {
			return null;
		}
	}

	public EventVo getEventByCorrelationId(
			String appId,
			String correlationId) {
		
		String sql = String.format("SELECT * FROM %s WHERE APP_ID=? AND CORRELATION_ID=?",tableName());
		
		try {
			JdbcTemplate temp = new JdbcTemplate(getDataSource());
			temp.setMaxRows(1);
			SimpleJdbcTemplate stemp = new SimpleJdbcTemplate(temp);
			
			EventVo queryForObject = stemp.queryForObject(
				sql, 
				this.eventRowMapper, 
				appId, 
				correlationId);
			return queryForObject;
		} catch (EmptyResultDataAccessException erdae) {
			return null;
		} catch (IncorrectResultSizeDataAccessException i) {
			return null;
		}
	}

	
}
