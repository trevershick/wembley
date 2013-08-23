package com.railinc.wembley.legacy.event.receiving;

import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.util.SchemaNameLocator;

public class DefaultEventFinalizerImpl implements EventFinalizer {

	private static final Logger log = LoggerFactory.getLogger(DefaultEventFinalizerImpl.class);

	private String appId;
	private SimpleJdbcTemplate jdbcTemplate;

	private static final String SCHEMA = SchemaNameLocator.getNotificationServiceSchemaName();
	private static final String UPDATE_FINAL_EVENT_STATE_SQL = String.format("UPDATE %sAPPLICATION_EVENTS SET STATE=?, STATE_TIMESTAMP=? WHERE EVENT_UID=?", SCHEMA);
	private static final String UPDATE_RETRY_EVENT_STATE_SQL = String.format("UPDATE %sAPPLICATION_EVENTS SET STATE_TIMESTAMP=?, RETRY_COUNT=? WHERE EVENT_UID=?", SCHEMA);

	public DefaultEventFinalizerImpl(String appId, DataSource ds) {
		this.appId = appId;
		this.jdbcTemplate = new SimpleJdbcTemplate(ds);
		log.info(String.format("Instantiating DefaultEventFinalizerImpl with AppId %s", appId));
	}

	public boolean finalizeEvent(Event event, String finalState) {
		boolean updated = false;

		if(log.isDebugEnabled()) {
			log.debug(String.format("Finalizing the Event %s with the state %s", event == null ? null : event.getEventUid(), finalState));
		}
		if(event != null) {
			updated = jdbcTemplate.update(UPDATE_FINAL_EVENT_STATE_SQL, finalState, new Date(), event.getEventUid()) > 0;
		}
		return updated;
	}

	public boolean retryEvent(Event event) {
		boolean updated = false;

		if(log.isDebugEnabled()) {
			log.debug(String.format("Marking the Event %s for retry (%d)", event == null ? null : event.getEventUid(),
					event == null ? 0 : event.getRetryCount()));
		}

		if(event != null) {
			updated = jdbcTemplate.update(UPDATE_RETRY_EVENT_STATE_SQL, new Date(), event.getRetryCount() + 1, event.getEventUid()) > 0;
		}

		return updated;
	}

	public String getAppId() {
		return appId;
	}
}
