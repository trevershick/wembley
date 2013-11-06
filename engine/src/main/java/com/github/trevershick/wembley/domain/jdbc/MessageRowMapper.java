package com.github.trevershick.wembley.domain.jdbc;

import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.i;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.l;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.phase;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.s;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.state;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.yn;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.github.trevershick.wembley.domain.Message;

public class MessageRowMapper implements RowMapper<Message> {
	@Override
	public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Message(l(rs,"id"),
				s(rs, "app_id"),
				yn(rs, "test_mode"),
				state(rs,"state"),
				s(rs, "template_name"),
				i(rs, "template_ver"),
				i(rs, "retry_count"),
				i(rs, "max_retries"),
				s(rs, "worker"), 
				phase(rs, "phase"));
		
	}


}
