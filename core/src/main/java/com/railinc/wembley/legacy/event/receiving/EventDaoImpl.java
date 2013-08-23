package com.railinc.wembley.legacy.event.receiving;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.EventVo;
import com.railinc.wembley.api.event.parser.EventParser;
import com.railinc.wembley.api.util.SchemaNameLocator;

public class EventDaoImpl implements EventDao {

	private static final Logger log = LoggerFactory.getLogger(EventDaoImpl.class);

	private static final String SCHEMA = SchemaNameLocator.getNotificationServiceSchemaName();
	private static final String SELECT_EVENTS_FOR_RETRY = String.format("SELECT EVENT_UID, APP_ID, STATE, STATE_TIMESTAMP, EVENT_XML, RETRY_COUNT " +
			"FROM %sAPPLICATION_EVENTS WHERE STATE=? AND RETRY_COUNT>? ORDER BY EVENT_UID", SCHEMA);

	private SimpleJdbcTemplate jdbcTemplate;
	private EventParser eventParser;

	private final ParameterizedRowMapper<Event> eventRowMapper = new ParameterizedRowMapper<Event>() {
		public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
			EventVo event = null;

			Clob xmlClob = rs.getClob("EVENT_XML");
			if(!rs.wasNull() && xmlClob != null && eventParser != null) {
				try {
					event = (EventVo)eventParser.parseEvent(xmlClob.getAsciiStream());
				} catch (NotificationServiceException e) {
					log.error("The event for retry could not be parsed and will be ignored", e);
				}
			}

			if(event != null) {
				event.setEventUid(rs.getString("EVENT_UID"));
				event.setRetryCount(rs.getInt("RETRY_COUNT"));
				event.setState(rs.getString("STATE"));
				event.setStateTimestamp(rs.getTimestamp("STATE_TIMESTAMP"));
			}

			return event;
		}
	};

	public EventDaoImpl(DataSource ds, EventParser eventParser) {
		this.jdbcTemplate = new SimpleJdbcTemplate(ds);
		this.eventParser = eventParser;
		log.info("Instantiated the EventDaoImpl");
	}

	public List<Event> getEventsForRetry() {
		List<Event> rawEvents = this.jdbcTemplate.query(SELECT_EVENTS_FOR_RETRY, this.eventRowMapper, "NEW", 0);
		List<Event> events = new ArrayList<Event>(rawEvents.size());
		for(Event event : rawEvents) {
			if(event != null) {
				events.add(event);
			}
		}
		return events;
	}
}
