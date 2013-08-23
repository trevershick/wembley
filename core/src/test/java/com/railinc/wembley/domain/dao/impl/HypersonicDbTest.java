package com.railinc.wembley.domain.dao.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@Ignore
public class HypersonicDbTest {
	private static final Logger log = LoggerFactory.getLogger(HypersonicDbTest.class);
	private DataSource dataSource = null;
	
	
	@AfterClass
	public static void dropAllTables() {
		SingleConnectionDataSource ds = new SingleConnectionDataSource(databaseUrl(),false);
		new JdbcTemplate(ds).execute("DROP SCHEMA PUBLIC CASCADE");
	}
	
	@BeforeClass
	public static void setupDatabase() throws IOException, ClassNotFoundException {
		Class.forName("org.hsqldb.jdbcDriver");
		SingleConnectionDataSource ds = new SingleConnectionDataSource(databaseUrl(),false);
		executeDdl(ds);
	}
	
	



	protected static void executeDdl(DataSource ds) throws IOException {
		// load resource files.
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/com/railinc/notifserv/ddl/spring-ddl.xml");

		Resource[] xmlResources = context
				.getResources("classpath:/com/railinc/notifserv/ddl/*.sql");

		JdbcTemplate template = new JdbcTemplate(ds);
		for (Resource r : xmlResources) {
			InputStream inputStream = r.getInputStream();
			String sqlFile = IOUtils.toString(inputStream);
			String[] ddl = sqlFile.split(";");
			for (String statement : ddl) {
				statement = convertToHsqlDb(statement);
				log.debug(statement);
				template.execute(statement);
			}
		}
	}
	
	
	
	
	
	private static String convertToHsqlDb(String statement) {
		String tmp = statement.replaceAll("VARCHAR2","VARCHAR");
		tmp = tmp.replaceAll(" BYTE\\s*\\)",")");
		// per http://db.apache.org/ddlutils/databases/hsqldb.html
		tmp = tmp.replaceAll(" CLOB", " LONGVARCHAR");
		tmp = tmp.replaceAll(" NUMBER", " NUMERIC");
		tmp = tmp.replaceAll(" DATE", " DATETIME");
		System.out.println(tmp);
		return tmp;
	}

	protected JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}

	protected DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = constructDataSource();
		}
		return dataSource;
	}

	protected DataSource constructDataSource() {
		return new SingleConnectionDataSource(databaseUrl(),false);
	}

	protected static String databaseUrl() {
		return "jdbc:hsqldb:mem:testdb";
	}

}
