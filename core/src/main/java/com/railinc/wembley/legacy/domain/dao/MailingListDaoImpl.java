package com.railinc.wembley.legacy.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;

public class MailingListDaoImpl extends BaseDaoImpl implements MailingListDao, DaoConstants {
	private static final String PROPERTY_SHORT_NAME = "shortName";
	private final Logger log= LoggerFactory.getLogger(MailingListDaoImpl.class);
	
		
		
	public MailingListsVo getMailingLists() {
		String sql = String.format( "SELECT * FROM %s", tableName() );

		List<MailingListVo> query = getSimpleJdbcTemplate().query(sql,ML_ROWMAPPER);
		MailingListsVo mailingListsVo = new MailingListsVo();
		mailingListsVo.addAll(query);
		return mailingListsVo;
	}
	
	
	
	private static final ParameterizedRowMapper<MailingListVo> ML_ROWMAPPER = 
		new ParameterizedRowMapper<MailingListVo>() {
			public MailingListVo mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				MailingListVo vo = new MailingListVo();
				vo.setKey(arg0.getString("KEY"));
				vo.setType(arg0.getString("TYPE"));
				vo.setApplication(arg0.getString("APPLICATION"));
				vo.setShortName(arg0.getString("SHORT_NAME"));
				vo.setTitle(arg0.getString("TITLE"));
				vo.setDescription(arg0.getString("DESCRIPTION"));
				vo.setActive(Y.equals(arg0.getString("ACTIVE_FLAG")));
				vo.setFromAddress(arg0.getString("FROM_ADDRESS"));
				return vo;
			}
		};



	@Override
	protected Logger getLog() {
		return this.log;
	}



	@SuppressWarnings("unchecked")
	public List<String> getMailingListIds() {
		String sql = String.format( "SELECT KEY FROM %s", tableName() );
		List<String> query = getJdbcTemplate().queryForList(sql, String.class);
		return query;
	}

	public String tableName() {
		return super.tableName(MAILINGLIST_TABLE_NAME_WO_SCHEMA);
	}
	
	public MailingListsVo getMailingList( String application, String type ) {
		
		MailingListsVo mailingLists = new MailingListsVo();

		log.debug( String.format( "Getting the mailing list for the application %s and type %s", application, type ) );
		String sql = String.format( "SELECT * FROM %s WHERE APPLICATION=? AND TYPE=? AND ACTIVE_FLAG='Y'", tableName() );
		mailingLists.addAll( getSimpleJdbcTemplate().query( sql, ML_ROWMAPPER, application, type ) );

		return mailingLists;
	}



	public void createMailingList(MailingListVo vo) {
		if (vo.getKey() == null) {
			vo.setKey(UUID.randomUUID().toString());
		}
		
		checkRequired(PROPERTY_SHORT_NAME, vo.getShortName());
		
		String sql = String.format("INSERT INTO %s (KEY,TYPE,APPLICATION,SHORT_NAME,TITLE,DESCRIPTION,ACTIVE_FLAG,FROM_ADDRESS) VALUES (?,?,?,?,?,?,?,?)",tableName());
		getJdbcTemplate().update(sql, new Object[]{
				vo.getKey(),
				vo.getType(),
				vo.getApplication(),
				vo.getShortName(),
				vo.getTitle(),
				vo.getDescription(),
				vo.isActive()?Y:N,
				vo.getFromAddress()});
		
	}



	public void deleteMailingList(String mailingListKey) {
		String sql = String.format("DELETE FROM %s WHERE KEY=?",tableName());
		getJdbcTemplate().update(sql, new Object[]{mailingListKey});
	}



	public MailingListVo getMailingList(String key) {
		try {
			String sql = String.format( "SELECT * FROM %s WHERE KEY=?", tableName() );
			return getSimpleJdbcTemplate().queryForObject( sql, ML_ROWMAPPER, key);
		} catch ( EmptyResultDataAccessException e ) {
			log.warn( String.format( "Could not find mailing list for the key %s", key) );
			return null;
		}
	}
	
	public MailingListVo getMailingListByShortName(String shortName) {
		try {
			String sql = String.format( "SELECT * FROM %s WHERE SHORT_NAME=?", tableName() );
			return getSimpleJdbcTemplate().queryForObject( sql, ML_ROWMAPPER, shortName);
		} catch ( EmptyResultDataAccessException e ) {
			log.warn( String.format( "Could not find mailing list for the key %s", shortName) );
			return null;
		}
	}



	public void updateMailingList(String key, MailingListVo vo) {
		String sql = String.format("UPDATE %s SET KEY=?,TYPE=?,APPLICATION=?,SHORT_NAME=?,TITLE=?,DESCRIPTION=?,ACTIVE_FLAG=?,FROM_ADDRESS=? WHERE KEY=?",tableName());
		getJdbcTemplate().update(sql, new Object[]{
				vo.getKey(),
				vo.getType(),
				vo.getApplication(),
				vo.getShortName(),
				vo.getTitle(),
				vo.getDescription(),
				vo.isActive()?Y:N,
				vo.getFromAddress(), key});
	}

}
