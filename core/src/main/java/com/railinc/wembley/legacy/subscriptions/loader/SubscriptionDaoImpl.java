package com.railinc.wembley.legacy.subscriptions.loader;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.railinc.wembley.api.core.NotificationServiceException;
import com.railinc.wembley.api.event.parser.DeliverySpecParser;
import com.railinc.wembley.api.util.SchemaNameLocator;
import com.railinc.wembley.legacy.domain.dao.ApplicationDao;
import com.railinc.wembley.legacy.subscriptions.Subscription;
import com.railinc.wembley.legacy.subscriptions.XPathDbSubscriptionImpl;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class SubscriptionDaoImpl implements SubscriptionDao {

	private static final Logger log = LoggerFactory.getLogger(SubscriptionDaoImpl.class);

	private static final String NOTIF_SERV_NAMESPACE_PREFIX = "ns";
	private static final String NOTIF_SERV_NAMESPACE = "http://events.notifserv.railinc.com";

	private SimpleJdbcTemplate jdbcTemplate;
	private DeliverySpecParser deliverySpecParser;
	private XPathFactory xpathFactory;
	private ApplicationDao applicationDao;

	private static final String SCHEMA = SchemaNameLocator.getNotificationServiceSchemaName();

	private static final String ACTIVE_SUBS_FOR_APP_SQL = String.format("SELECT SUBSCRIPTION_UID, APP_ID, ACTIVE, CRITERIA_XPATH, DELIVERY_SPEC, DELIVERY_TIMING, " +
			"CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP FROM %sSUBSCRIPTIONS WHERE ACTIVE=? AND APP_ID=?", SCHEMA);

	private final ParameterizedRowMapper<Subscription> subscriptionRowMapper = new ParameterizedRowMapper<Subscription>() {
		public Subscription mapRow(ResultSet rs, int rowNum) throws SQLException {
			XPathDbSubscriptionImpl sub = new XPathDbSubscriptionImpl(rs.getString("APP_ID"));
			sub.setSubscriptionUid(rs.getString("SUBSCRIPTION_UID"));
			sub.setActive(rs.getInt("ACTIVE") == 1);

			String xpathStr = rs.getString("CRITERIA_XPATH");
			sub.setXpathString(xpathStr);
			if(xpathStr != null && xpathStr.length() > 0) {
				Map<String, String> namespaces = null;
				if(applicationDao != null) {
					ApplicationVo app = applicationDao.getApplication(sub.getAppId());
					if(app != null) {
						namespaces = app.getSupportedNamespaces();
					}
				}
				if(namespaces == null) {
					namespaces = new HashMap<String, String>();
				}
				namespaces.put(NOTIF_SERV_NAMESPACE_PREFIX, NOTIF_SERV_NAMESPACE);

				NamespaceContext nsCtx = new MapNamespaceContext(namespaces);
				XPath xpath = xpathFactory.newXPath();
				xpath.setNamespaceContext(nsCtx);

				try {
					xpath.compile(xpathStr);
					sub.setXpath(xpath);
				} catch (XPathExpressionException e) {
					sub.setXpath(null);
					log.error("Unable to compile xpath for subscription " + sub.getSubscriptionUid(), e);
				}
			}

			if(deliverySpecParser != null) {
				Clob xpath = rs.getClob("DELIVERY_SPEC");
				if(xpath != null) {
					try {
						sub.setDeliverySpec(deliverySpecParser.parseDeliverySpec(xpath.getAsciiStream()));
					} catch (NotificationServiceException e) {
						log.error("Unable to parse DELIVERY_SPEC for subscription " + sub.getSubscriptionUid(), e);
					}
				}
			}

			sub.setDeliveryTiming(rs.getString("DELIVERY_TIMING"));
			sub.setCreatedUser(rs.getString("CREATED_USER"));
			sub.setCreatedTimstamp(rs.getTimestamp("CREATED_TIMESTAMP"));
			String modUser = rs.getString("MODIFIED_USER");
			sub.setModifiedUser(rs.wasNull() ? null : modUser);
			Timestamp modTs = rs.getTimestamp("MODIFIED_TIMESTAMP");
			sub.setModifiedTimestamp(rs.wasNull() ? null : modTs);
			return sub;
		}
	};

	public SubscriptionDaoImpl(DataSource ds) {
		this.jdbcTemplate = new SimpleJdbcTemplate(ds);
		this.xpathFactory = XPathFactory.newInstance();
		log.info("Instantiating SubscriptionDaoImpl");
	}

	public List<Subscription> getActiveSubscriptionsForApp(String appId) {
		if(log.isDebugEnabled()) {
			log.debug(String.format("Getting all active subscriptions for App Id %s", appId));
		}
		List<Subscription> subs = this.jdbcTemplate.query(ACTIVE_SUBS_FOR_APP_SQL, subscriptionRowMapper, 1, appId);
		if(log.isDebugEnabled()) {
			log.debug(String.format("Got %d subscriptions for App Id %s", subs.size(), appId));
		}
		return subs;
	}

	public void setDeliverySpecParser(DeliverySpecParser deliverySpecParser) {
		this.deliverySpecParser = deliverySpecParser;
		log.debug("Setting the DeliverySpecParser");
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
		log.debug("Setting the ApplicationDao");
	}

	private static class MapNamespaceContext implements NamespaceContext {
		private Map<String, String> namespaces;

		private MapNamespaceContext(Map<String, String> namespaces) {
			this.namespaces = namespaces;
		}

		public String getNamespaceURI(String prefix) {
			return this.namespaces.get(prefix);
		}

		public String getPrefix(String namespaceURI) {
			String prefix = null;
			for(Map.Entry<String, String> entry : this.namespaces.entrySet()) {
				if(entry.getValue().equals(namespaceURI)) {
					prefix = entry.getKey();
					break;
				}
			}

			return prefix;
		}

		public Iterator<String> getPrefixes(String namespaceURI) {
			List<String> prefixes = new ArrayList<String>();
			for(Map.Entry<String, String> entry : this.namespaces.entrySet()) {
				if(entry.getValue().equals(namespaceURI)) {
					prefixes.add(entry.getKey());
				}
			}
			return prefixes.iterator();
		}
	}
}
