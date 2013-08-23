package com.railinc.wembley.legacy.domain.dao;



import org.slf4j.Logger;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class BaseDaoImpl extends JdbcDaoSupport {
	
	private static final String PERIOD = ".";
	private String schema;
	
	protected abstract Logger getLog();
	protected void debug(String message, Object ... args) {
		Logger log = getLog();
		if (log.isDebugEnabled()) {
			log.debug(String.format(message,args));
		}
	}
	

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		if (this.simple == null) {
			this.simple = new SimpleJdbcTemplate(getJdbcTemplate());
		}
		return this.simple;
	}
	private SimpleJdbcTemplate simple;
	
	/**
	 * Sets the schema name to the supplied value. If the supplied value has a period on the
	 * end of it, then the period is removed.
	 * @param schema
	 */
	public void setSchema( String schema ) {
		String tmp = schema;
		if (null != tmp && tmp.endsWith(PERIOD)) {
			tmp = tmp.substring(0, tmp.length() - 1);
		}
		this.schema = tmp;
	}
	
	/**
	 * If the schema is set on the dao, then this method will preped the schema name with
	 * the qualifier onto the table name.
	 * @param tableName
	 * @return
	 */
	protected String tableName(String tableName) {
		String tmp = schema;
		if (null == tmp || "".equals(tmp)) {
			return tableName;
		}
		return new StringBuilder(tmp).append(PERIOD).append(tableName).toString();
	}
	/**
	 * 
	 * @return
	 */
	public String getSchema() {
		if ( schema == null ) {
			schema = "";
		}
		return schema;
	}
	protected void checkRequired(String title, String value) {
		if (value == null) {
			throw new IllegalArgumentException(title + " is required");
		}
	}
	protected void checkColumnWidth(String title, String value, int maxColumnWidth) {
		if (null == value) {
			return;
		}
		if (value.length() > maxColumnWidth) {
			throw new IllegalArgumentException(title + " can only be " + maxColumnWidth + " characters");
		}
	}
}
