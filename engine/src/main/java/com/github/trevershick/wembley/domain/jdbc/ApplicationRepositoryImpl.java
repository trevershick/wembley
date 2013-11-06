package com.github.trevershick.wembley.domain.jdbc;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.github.trevershick.wembley.api.ApplicationRepository;
import com.github.trevershick.wembley.domain.Application;
import com.google.common.base.Preconditions;

public class ApplicationRepositoryImpl implements ApplicationRepository {
	public static final Application DEFAULT_APP = new Application("DEF", false, false, null, null);

	private DataSource dataSource;
	
	// TODO - pull out to common repo baseimpl
	private String schema = "wembley";
	
	
	@Override
	public Application byIdOrDefault(String id) {
		Preconditions.checkArgument(isNotBlank(id), "id is required");
		
		NamedParameterJdbcTemplate t = new NamedParameterJdbcTemplate(dataSource);
		Application app = t.queryForObject(byIdSql(), byIdParameterSource(id), new ApplicationRowMapper());
		if (app == null) {
			return DEFAULT_APP;
		}
		
		return app;
	}


	private String byIdSql() {
		return String.format("SELECT * FROM %s.app WHERE app_id = :appId", schema);
	}

	private SqlParameterSource byIdParameterSource(String applicationId) {
		return new MapSqlParameterSource("appId", applicationId);
	}

}
