package com.github.trevershick.wembley.domain.jdbc;

import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.intent;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.l;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.rrn;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.state;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.github.trevershick.wembley.domain.Destination;

public class DestinationRowMapper implements RowMapper<Destination> {
	@Override
	public Destination mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Destination(l(rs,"id"),
				l(rs,"msg_id"),
				rrn(rs, "parent_rrn"),
				rrn(rs, "rrn"),
				intent(rs, "intent"),
				state(rs,"state"));
	}


}
