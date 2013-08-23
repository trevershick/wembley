package com.railinc.wembley.legacy.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.railinc.wembley.api.util.SchemaNameLocator;
import com.railinc.wembley.legacysvc.domain.Application;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;

public class ApplicationDaoImpl extends BaseDaoImpl implements ApplicationDao, DaoConstants {
	private static final String SCHEMA = SchemaNameLocator.getNotificationServiceSchemaName();
	private static final String SELECT_ALL_APPS_SQL = String.format("SELECT APP_ID, SUPPORTED_NAMESPACES, DEFAULT_DELIVERY_TIMING, ADMIN_EMAIL, " +
			"CREATED_TIMESTAMP FROM %sAPPLICATIONS ORDER BY APP_ID", SCHEMA);
	
	private static final String PROPERTY_DEFAULT_DELIVERY_TIMING = "defaultDeliveryTiming";
	private static final String PROPERTY_ADMIN_EMAIL = "adminEmail";
	private static final String PROPERTY_APP_ID = "appId";

	private static final String NAMESPACE_DELIMITER = "\\|";
	private static final String PREFIX_URI_DELIMITER = "=";

	public static final int COLUMN_WIDTH_APP_ID = 15;
	public static final int COLUMN_WIDTH_DEFAULT_DELIVERY_TIMING = 15;
	public static final int COLUMN_WIDTH_ADMIN_EMAIL = 50;
	
	private final Logger log = LoggerFactory.getLogger(ApplicationDaoImpl.class);
	

	private final ParameterizedRowMapper<ApplicationVo> appRowMapper = new ParameterizedRowMapper<ApplicationVo>() {
		public ApplicationVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			ApplicationVo app = new ApplicationVo();
			app.setAppId(rs.getString(APPLICATION_COLUMN_APP_ID));
			app.setDefaultDeliveryTiming(rs.getString(APPLICATION_COLUMN_DEFAULT_DELIVERY_TIMING));
			app.setCreatedTimestamp(rs.getTimestamp(APPLICATION_COLUMN_CREATED_TIMESTAMP));
			app.setSupportedNamespaces(new HashMap<String, String>());
			String email = rs.getString(APPLICATION_COLUMN_ADMIN_EMAIL);
			app.setAdminEmail(rs.wasNull() ? null : email);

			String ns = rs.getString(APPLICATION_COLUMN_SUPPORTED_NAMESPACES);
			if(!rs.wasNull() && ns != null && ns.length() > 0) {
				String[] nsList = ns.split(NAMESPACE_DELIMITER);
				if(nsList != null) {
					for(String s : nsList) {
						String[] prefixUri = s.split(PREFIX_URI_DELIMITER);
						if(prefixUri != null && prefixUri.length == 2) {
							app.getSupportedNamespaces().put(prefixUri[0], prefixUri[1]);
						}
					}
				}
			}

			return app;
		}

	};

	public List<ApplicationVo> getAllApplications() {
		String sql = String.format("SELECT APP_ID, SUPPORTED_NAMESPACES, DEFAULT_DELIVERY_TIMING, ADMIN_EMAIL, CREATED_TIMESTAMP FROM %s ORDER BY APP_ID", 
				tableName());
		
		List<ApplicationVo> apps = getSimpleJdbcTemplate().query(sql, appRowMapper);
		return apps;
	}

	public ApplicationVo getApplication(String appId) {
		if (null == appId) {
			return null;
		}
		try {
			debug("Getting the application for the ID %s", appId);
			
			String sql = String.format("SELECT APP_ID, SUPPORTED_NAMESPACES, DEFAULT_DELIVERY_TIMING, ADMIN_EMAIL, CREATED_TIMESTAMP FROM %s WHERE APP_ID=?", 
					tableName());
			
			return getSimpleJdbcTemplate().queryForObject(sql, appRowMapper, appId);
		} catch (EmptyResultDataAccessException e) {
			log.warn(String.format("Could not find the application for the ID %s", appId));
			return null;
		}
	}

	@Override
	protected Logger getLog() {
		return this.log;
	}
	
	public String tableName() {
		return super.tableName(APPLICATION_TABLE_NAME_WO_SCHEMA);
	}

	public void deleteApplication(String appId) {
		checkRequired(PROPERTY_APP_ID, appId);
		checkColumnWidth(PROPERTY_APP_ID, appId, COLUMN_WIDTH_APP_ID);
		
		String sql = String.format("DELETE FROM %s WHERE APP_ID=?", tableName());
		int update = getJdbcTemplate().update(sql, new Object[]{appId});
		if (0 == update) {
			throw new IncorrectResultSizeDataAccessException(1,0);
		}
	}
	public void insertApplication(ApplicationVo vo) {
		checkColumnWidths(vo);
		checkRequired(vo);
		String sql = String.format("INSERT INTO %s (APP_ID,DEFAULT_DELIVERY_TIMING,ADMIN_EMAIL,CREATED_TIMESTAMP) VALUES (?,?,?,sysdate)", tableName());
		getJdbcTemplate().update(sql, new Object[]{vo.getAppId(), vo.getDefaultDeliveryTiming(), vo.getAdminEmail()}); 
	}

	public ApplicationVo updateApplication(String appId, ApplicationVo vo) {
		checkColumnWidths(vo);
		checkRequired(vo);
		String sql = String.format("UPDATE %s SET APP_ID=?, DEFAULT_DELIVERY_TIMING=?,ADMIN_EMAIL=? WHERE APP_ID=?", tableName());
		int update = getJdbcTemplate().update(sql, new Object[]{vo.getAppId(), vo.getDefaultDeliveryTiming(), vo.getAdminEmail(),appId});
		if (0 == update) {
			throw new IncorrectResultSizeDataAccessException(1,0);
		}
		return vo;
	}

	private void checkColumnWidths(ApplicationVo vo) {
		checkColumnWidth(PROPERTY_ADMIN_EMAIL, vo.getAdminEmail(), COLUMN_WIDTH_ADMIN_EMAIL);
		checkColumnWidth(PROPERTY_APP_ID, vo.getAppId(), COLUMN_WIDTH_APP_ID);
		checkColumnWidth(PROPERTY_DEFAULT_DELIVERY_TIMING, vo.getDefaultDeliveryTiming(), COLUMN_WIDTH_DEFAULT_DELIVERY_TIMING);
	}
	
	private void checkRequired(ApplicationVo vo) {
		checkRequired(PROPERTY_ADMIN_EMAIL, vo.getAdminEmail());
		checkRequired(PROPERTY_APP_ID, vo.getAppId());
		checkRequired(PROPERTY_DEFAULT_DELIVERY_TIMING, vo.getDefaultDeliveryTiming());
	}

	@Override
	public Map<String, ApplicationVo> getAllApplicationsByKey() {
		log.debug("Getting all active applications");
		List<ApplicationVo> apps = getSimpleJdbcTemplate().query(SELECT_ALL_APPS_SQL, appRowMapper);
		Map<String, ApplicationVo> appMap = new HashMap<String, ApplicationVo>();
		if(apps != null) {
			for(ApplicationVo app : apps) {
				if(log.isDebugEnabled()) {
					log.debug(String.format("Got the application %s: %s", app.getAppId(), app));
				}
				appMap.put(app.getAppId(), app);
			}
		}
		if(log.isDebugEnabled()) {
			log.debug(String.format("Loaded %d applications", appMap.size()));
		}

		return appMap;
	}
}
