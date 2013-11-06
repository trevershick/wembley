package com.github.trevershick.wembley.domain.jdbc;

import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.intent;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.l;
import static com.github.trevershick.wembley.domain.jdbc.JdbcUtils.s;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.github.trevershick.wembley.domain.MessageContent;

public class MessageContentRowMapper implements RowMapper<MessageContent> {
	@Override
	public MessageContent mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		return new MessageContent(l(rs,"id"),
				l(rs,"msg_id"),
				intent(rs, "intent"),
				s(rs,"content"),
				s(rs, "contenttype"),
				l(rs, "dest_id"));
	}


}
