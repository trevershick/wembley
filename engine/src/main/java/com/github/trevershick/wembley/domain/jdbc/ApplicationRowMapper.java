package com.github.trevershick.wembley.domain.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.*;

import com.github.trevershick.wembley.domain.Application;

public class ApplicationRowMapper implements RowMapper<Application> {
	@Override
	public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Application(s(rs,"app_id"),
				yn(rs, "test_mode"),
				yn(rs, "on_hold"),
				rrn(rs, "success_rrn"),
				rrn(rs, "failure_rrn"));
	}


}
