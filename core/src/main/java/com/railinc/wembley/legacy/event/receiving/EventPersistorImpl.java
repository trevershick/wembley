package com.railinc.wembley.legacy.event.receiving;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.Event;
import com.railinc.wembley.api.event.EventHeader;
import com.railinc.wembley.api.event.EventParameter;
import com.railinc.wembley.api.util.SchemaNameLocator;
import com.railinc.wembley.api.util.UUIDGenerator;

public class EventPersistorImpl implements EventPersistor {

	private static final Logger log = LoggerFactory.getLogger(EventPersistorImpl.class);
	
	private static final String FAILED_TO_PERSIST_EVENT_ERROR_S_S = "[%s] Failed to persist event with AppId '%s' (see stack trace)";
	private static final String SCHEMA = SchemaNameLocator.getNotificationServiceSchemaName();
	private static final String INSERT_EVENT = String.format("INSERT INTO %sAPPLICATION_EVENTS (EVENT_UID, APP_ID, STATE, CREATED_TIMESTAMP, " +
										 "STATE_TIMESTAMP, EVENT_XML, CORRELATION_ID, SEND_AFTER) VALUES( ?, ?, ?, ?, ?, ?, ?, ?)", SCHEMA);

	private static final Logger LOG = LoggerFactory.getLogger( EventPersistorImpl.class );


	private JdbcTemplate jdbcTemplate;

	/**
	 * This ID is used to register and access the OSGi bundle for this service
	 */
	private String appId;

	
	
	
	public EventPersistorImpl(String appId, DataSource dataSource) {
		this.appId        = appId;
		this.jdbcTemplate = new JdbcTemplate( dataSource );
		
		LOG.info( String.format( "Initialized EventPersistor with AppId '%s' and datasource '%s'", appId, dataSource ) );
	}

	/**
	 * Persists an Application Event and the raw message XML into the Notification Service's
	 * own data store. The event should contain an Application ID which is a unique identifier
	 * for an application.
	 *
	 *	US3252 - added the correlation id column and insert logic.
	 *
	 *  @param event
	 *  @param eventXml
	 *  @return boolean
	 */
	public boolean persistEvent( Event event, String eventXml) {
		
		if(event == null || event.getEventHeader() == null) {
			throw new NotificationServiceException("Null Event or Missing Event, Cannot persist the event");
		}
		if ( LOG.isDebugEnabled() )	{
			LOG.debug( String.format( "[%s] Persisting an event for AppId '%s'", appId, event.getEventHeader().getAppId() ) );
		}

		// This ID is used to store the event XML and identify it with a
		// specific application
		String applicationEventId = event.getEventHeader().getAppId();

		try
		{
			UUIDGenerator uuidGen = UUIDGenerator.getInstance();
			Date createdDate = new Date();

			event.setEventUid( uuidGen.createUUID() );

			Object[] params = { event.getEventUid(), 
					applicationEventId, 
					EventConstants.EVENT_STATUS_NEW, 
					createdDate, 
					createdDate, 
					eventXml , 
					event.getEventHeader().getCorrelationId(),
					sendAfter(event)};
			
			jdbcTemplate.update( INSERT_EVENT, params );
			return true;
		} catch ( DataAccessException e ) {
			LOG.error( String.format( FAILED_TO_PERSIST_EVENT_ERROR_S_S, 
					appId, event.getEventHeader().getAppId()), e );
			throw new NotificationServiceException(e);
		}
	}

	public String getAppId() {
		return appId;
	}

	
	public boolean persistUnparseableEvent(String appId, String correlationId,
			String eventXml) {
		if (appId == null || correlationId == null || eventXml == null) {
			return false;
		}
		try
		{
			UUIDGenerator uuidGen = UUIDGenerator.getInstance();
			Date createdDate = new Date();

			Object[] params = { uuidGen.createUUID(), 
					appId, 
					EventConstants.EVENT_STATUS_POISON, 
					createdDate, 
					createdDate, 
					eventXml , 
					correlationId,
					sendAfter(null)};
			
			jdbcTemplate.update( INSERT_EVENT, params );
			return true;
		} catch ( DataAccessException e ) {
			LOG.error( String.format( FAILED_TO_PERSIST_EVENT_ERROR_S_S, appId, appId), e );
			throw new NotificationServiceException(e);
		}
	}
	
	/**
	 * this is very forgiving with parsing dates. it only parses what's there if it can
	 * enforcement of the Header being a proper date valeu is handled in the eventparserimpl
	 * @param event
	 * @return
	 */
	protected Date sendAfter(Event event) {
		if (null == event) {
			return null;
		}
		EventHeader header = event.getEventHeader();
		if (null == header) {
			return null;
		}
		List<? extends EventParameter> params = header.getEventParams();
		if (params == null || params.size() == 0) {
			return null;
		}
		for (EventParameter p : params) {
			if (Event.SEND_AFTER.equals(p.getParamName())) {
				return attemptToParseDate(p.getParamValue());
			}
		}
		return null;
	}

	protected Date attemptToParseDate(String paramValue) {
		try {
			XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(paramValue);
			return calendar.toGregorianCalendar().getTime();
		} catch (Exception e) {
			log.error("unable to parse date from header values (" + paramValue + ")", e);
			return null;
		}
	}
}
