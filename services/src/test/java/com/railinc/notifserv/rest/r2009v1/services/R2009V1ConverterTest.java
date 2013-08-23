package com.railinc.notifserv.rest.r2009v1.services;

import java.util.Date;

import junit.framework.TestCase;

import com.railinc.notifserv.restsvcs.r2009v1.Event;
import com.railinc.wembley.legacysvc.domain.EventVo;

public class R2009V1ConverterTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}


	public void testConvertEvent_US4682_sendAfter_only() {
		Date dt = new Date();
		
		EventVo v = new EventVo();
		v.setSendAfter(dt);
		Event jaxb = new R2009V1Converter().convert(v,false);
		assertEquals(dt, jaxb.getSendAfter().getTime());
	}

}
