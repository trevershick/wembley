package com.railinc.wembley.domain.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.railinc.wembley.legacy.domain.dao.ApplicationDaoImpl;
import com.railinc.wembley.legacysvc.domain.ApplicationVo;



public class ApplicationDaoImplTest extends HypersonicDbTest {

	private ApplicationDaoImpl dao;

	@Before
	public void setupBefore() {
		dao = new ApplicationDaoImpl();
		dao.setDataSource(getDataSource());
		getJdbcTemplate().execute("DELETE FROM " + dao.APPLICATION_TABLE_NAME_WO_SCHEMA);
	}
	
	@After
	public void setupAfter() {
		getJdbcTemplate().execute("DELETE FROM " + dao.APPLICATION_TABLE_NAME_WO_SCHEMA);
		
	}
	
	
	@Test
	public void testGetAllApplications() {
		List<ApplicationVo> apps = dao.getAllApplications();
		assertNotNull(apps);
		assertEquals(0, apps.size());
		
		ApplicationVo a = new ApplicationVo();
		a.setAdminEmail("trevershick@yahoo.com");
		a.setAppId("UNIT1");
		
		dao.insertApplication(a);
		
		apps = dao.getAllApplications();
		assertEquals(1, apps.size());
		
		
		
	}

	@Test
	public void testGetApplication() {
		ApplicationVo a = new ApplicationVo();
		a.setAdminEmail("trevershick@yahoo.com");
		a.setAppId("UNIT1");
		
		dao.insertApplication(a);
		ApplicationVo a2 = dao.getApplication("UNIT1");
		assertNotNull(a2);
		assertEquals(a.getAppId(), a2.getAppId());
		assertEquals(a.getAdminEmail(), a2.getAdminEmail());
		
		assertNull(dao.getApplication(null));
	}

	@Test
	public void testDeleteApplication() {
		ApplicationVo a = new ApplicationVo();
		a.setAdminEmail("trevershick@yahoo.com");
		a.setAppId("UNIT1");
		dao.insertApplication(a);
		
		ApplicationVo a2 = dao.getApplication("UNIT1");
		assertNotNull(a2);
		
		dao.deleteApplication("UNIT1");
		a2 = dao.getApplication("UNIT1");
		assertNull(a2);
	}

	@Test
	public void testUpdateApplication() {
		ApplicationVo a = new ApplicationVo();
		a.setAdminEmail("trevershick@yahoo.com");
		a.setAppId("UNIT1");
		dao.insertApplication(a);
		
		a.setAppId("UNIT2");
		a.setAdminEmail("tshick@hotmail.com");
		
		dao.updateApplication("UNIT1", a);

		
		a = dao.getApplication("UNIT1");
		assertNull(a);
		
		a = dao.getApplication("UNIT2");
		assertNotNull(a);
		
	}

}
