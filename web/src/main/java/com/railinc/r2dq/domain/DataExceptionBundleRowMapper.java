package com.railinc.r2dq.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class DataExceptionBundleRowMapper implements ParameterizedRowMapper<DataExceptionBundle> {

	@Override
	public DataExceptionBundle mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		DataExceptionBundle bundle = new DataExceptionBundle();
		bundle.setSourceSystem(new SourceSystem(rs.getString("source_system_code"), ""));
		bundle.setImplementationType(ImplementationType.valueOf(rs.getString("implementation_code")));
		bundle.setSourceSystemColumnName(rs.getString("source_system_col_name"));
		bundle.setSourceSystemIdentifier(rs.getString("source_system_row_key"));
		bundle.setResponsiblePerson(new Identity(IdentityType.valueOf(rs.getString("candidate_resp_type")), rs.getString("candidate_resp_reference")));
		return bundle;
	}

}
