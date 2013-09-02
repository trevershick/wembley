package com.railinc.wembley.domain.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.railinc.wembley.api.Intent;
import com.railinc.wembley.api.PipelinePhase;
import com.railinc.wembley.api.ProcessingState;

public class JdbcUtils {
	
	public static ProcessingState state(ResultSet rs, String string) throws SQLException {
		String s = s(rs,string);
		return ProcessingState.valueOf(s);
	}

	public static Intent intent(ResultSet rs, String string) throws SQLException {
		String s = s(rs,string);
		return Intent.valueOf(s);
	}
	
	public static PipelinePhase phase(ResultSet rs, String string) throws SQLException {
		String s = s(rs,string);
		return PipelinePhase.valueOf(s);
	}

	public static int i(ResultSet rs, String string) throws SQLException {
		return rs.getInt(string);
	}
	
	public static long l(ResultSet rs, String string) throws SQLException {
		return rs.getLong(string);
	}
	
	public static String rrn(ResultSet rs, String string) throws SQLException {
		return s(rs, string);
	}

	public static boolean yn(ResultSet rs, String string) throws SQLException {
		return "Y".equals(rs.getString(string));
	}

	public static String s(ResultSet rs, String string) throws SQLException {
		return rs.getString(string);
	}

	public static String yn(boolean b) {
		return b ? "Y" : "N";
	}

}
