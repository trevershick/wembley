package com.railinc.ddcts.notifserv.notifications.loader.impl;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.railinc.wembley.core.exception.NotificationServiceException;
import com.railinc.wembley.event.parser.DeliverySpecParser;
import com.railinc.wembley.event.parser.EventParser;
import com.railinc.wembley.notifications.loader.NotificationIterator;
import com.railinc.wembley.notifications.loader.NotificationLoader;
import com.railinc.wembley.notifications.loader.impl.DefaultNotificationIterator;
import com.railinc.wembley.notifications.model.Notification;
import com.railinc.wembley.notifications.model.impl.NotificationVo;
import com.railinc.wembley.util.db.SchemaNameLocator;

public class DdctsNotificationLoaderImpl implements NotificationLoader {

	private static final Logger log = LoggerFactory.getLogger(DdctsNotificationLoaderImpl.class);

	private SimpleJdbcTemplate jdbcTemplate;
	private EventParser eventParser;
	private DeliverySpecParser deliverySpecParser;

	private static final String NEW_STATE = "OPEN";
	private static final String SCHEMA = SchemaNameLocator.getNotificationServiceSchemaName();
	private static final String LOAD_NEW_NOTIFICATIONS_SQL = String.format("SELECT NOTIFICATION_UID, N.APP_ID, N.STATE, N.EVENT_UID, DELIVERY_SPEC, " +
			"DELIVERY_TIMING, N.CREATED_TIMESTAMP, N.STATE_TIMESTAMP, N.RETRY_COUNT, E.EVENT_XML FROM %1$sNOTIFICATIONS N, %1$sAPPLICATION_EVENTS E " +
			"WHERE N.APP_ID=? AND N.DELIVERY_TIMING=? AND N.STATE=? AND N.EVENT_UID = E.EVENT_UID AND (E.SEND_AFTER IS NULL OR E.SEND_AFTER < sysdate) ORDER BY E.CORRELATION_ID, N.CREATED_TIMESTAMP", SCHEMA);

	private final ParameterizedRowMapper<Notification> notificationRowMapper = new ParameterizedRowMapper<Notification>() {
		public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
			NotificationVo not = new NotificationVo();
			not.setNotificationUid(rs.getString("NOTIFICATION_UID"));
			not.setAppId(rs.getString("APP_ID"));
			not.setState(rs.getString("STATE"));
			not.setRetryCount(rs.getInt("RETRY_COUNT"));
			not.setEventUid(rs.getString("EVENT_UID"));
			not.setDeliveryTiming(rs.getString("DELIVERY_TIMING"));
			not.setDeliverySpecString(rs.getString("DELIVERY_SPEC"));
			if(rs.wasNull()) {
				not.setDeliverySpecString(null);
			} else {
				if(deliverySpecParser != null) {
					try {
						not.setDeliverySpec(deliverySpecParser.parseDeliverySpec(not.getDeliverySpecString()));
					} catch (NotificationServiceException e) {
						log.error("Error parsing Delivery Spec", e);
					}
				}
			}
			Clob c = rs.getClob("EVENT_XML");
			if(!rs.wasNull()) {
				not.setEventXml(c.getSubString(1, (int)c.length()));
			}
			not.setCreatedTimestamp(rs.getTimestamp("CREATED_TIMESTAMP"));
			not.setStateTimestamp(rs.getTimestamp("STATE_TIMESTAMP"));
			return not;
		}
	};

	public DdctsNotificationLoaderImpl(DataSource ds) {
		this.jdbcTemplate = new SimpleJdbcTemplate(ds);
		log.info("Instantiating DdctsNotificationLoaderImpl");
	}
	
	@Override
	public NotificationIterator loadNotifications(String appId, String deliveryTiming) {
		if(log.isDebugEnabled()) {
			log.debug(String.format("Loading new notifications using DdctsNotificationLoaderImpl for App ID %s and Delivery Timing %s", appId, deliveryTiming));
		}
		List<Notification> nots = this.jdbcTemplate.query(LOAD_NEW_NOTIFICATIONS_SQL, notificationRowMapper, appId, deliveryTiming, NEW_STATE);

		if ( log.isDebugEnabled() ) {
			log.debug(String.format( "%d notification(s) loaded for App ID %s and Delivery Timing %s using DdctsNotificationLoaderImpl", nots.size(), appId, deliveryTiming ) );
		}

		DefaultNotificationIterator it = new DefaultNotificationIterator(this.eventParser);
		it.setNotifications(nots.iterator());
		return it;
	}

	@Override
	public String getAppId() {
		return "DDCTS";
	}
	
	public void setDeliverySpecParser(DeliverySpecParser deliverySpecParser) {
		this.deliverySpecParser = deliverySpecParser;
		log.debug("Setting the DeliverySpecParser for the DefaultNotificationLoaderImpl");
	}

	public void setEventParser(EventParser eventParser) {
		this.eventParser = eventParser;
	}
}
