package com.railinc.wembley.legacy.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.railinc.wembley.api.util.SchemaNameLocator;
import com.railinc.wembley.legacysvc.domain.SystemConfig;
import com.railinc.wembley.legacysvc.domain.SystemConfigVo;

public class SystemConfigDaoImpl implements SystemConfigDao {

	private static final Logger log = LoggerFactory.getLogger(SystemConfigDaoImpl.class);
	private static final String SCHEMA = SchemaNameLocator.getNotificationServiceSchemaName();
	private static final String GET_ALL_SYSTEM_CONFIG = String.format("SELECT CONFIG_KEY, CONFIG_VALUE, DESCRIPTION " +
			"FROM %sSYSTEM_CONFIG ORDER BY CONFIG_KEY", SCHEMA);
	private static final String GET_SYSTEM_CONFIG = String.format("SELECT CONFIG_KEY, CONFIG_VALUE, DESCRIPTION " +
			"FROM %sSYSTEM_CONFIG WHERE CONFIG_KEY=?", SCHEMA);

	private SimpleJdbcTemplate jdbcTemplate;

	private final ParameterizedRowMapper<SystemConfig> configRowMapper = new ParameterizedRowMapper<SystemConfig>() {
		public SystemConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
			SystemConfigVo config = new SystemConfigVo();
			config.setKey(rs.getString("CONFIG_KEY"));
			String value = rs.getString("CONFIG_VALUE");
			config.setValue(rs.wasNull() ? null : value);
			String desc = rs.getString("DESCRIPTION");
			config.setDescription(rs.wasNull() ? null : desc);
			return config;
		}

	};

	public SystemConfigDaoImpl(DataSource ds) {
		this.jdbcTemplate = new SimpleJdbcTemplate(ds);
		log.info("Instantiated the SystemConfigDaoImpl");
	}

	public List<SystemConfig> getAllSystemConfig() {
		return this.jdbcTemplate.query(GET_ALL_SYSTEM_CONFIG, this.configRowMapper);
	}

	public SystemConfig getSystemConfig(String key) {
		SystemConfig config = null;
		try {
			log.debug(String.format("Getting the config for the key %s", key));
			config = jdbcTemplate.queryForObject(GET_SYSTEM_CONFIG, configRowMapper, key);
		} catch (EmptyResultDataAccessException e) {
			log.warn(String.format("Could not find the config for the key %s", key));
		}
		return config;
	}
}
