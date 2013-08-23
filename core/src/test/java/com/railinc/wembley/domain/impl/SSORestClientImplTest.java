package com.railinc.wembley.domain.impl;

import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.junit.Test;

import com.railinc.wembley.legacy.domain.impl.sso.SSORestClientImpl;
import com.railinc.wembley.legacy.domain.impl.sso.SSOUserInfo;

public class SSORestClientImplTest {
	
	private static final String BASE_URL = "http://datasvcs.dev.railinc.com/SSOServices/sso/v1";

	@Test
	public void testGetAllActiveUsers() throws IOException{
		SSORestClientImpl sso = new SSORestClientImpl();
		
		HttpClient httpClient = new HttpClient();
		String response = IOUtils.toString(getClass().getResourceAsStream("allusers.xml"));
		MockHttpConnection connection = new MockHttpConnection("datasvcs.dev.railinc.com",80,response);
		MockHttpConnectionManager mockHttpConnectionManager = new MockHttpConnectionManager(connection);
		httpClient.setHttpConnectionManager(mockHttpConnectionManager);
		sso.setHttpClient(httpClient);
		sso.setBaseUrl(BASE_URL);
		List<SSOUserInfo> emails = sso.getAllActiveUsers();
		assertTrue(emails.size() > 0);
	}
	
	@Test
	public void testByRole() throws IOException {
		SSORestClientImpl sso = new SSORestClientImpl();

		HttpClient httpClient = new HttpClient();
		String response = IOUtils.toString(getClass().getResourceAsStream("usersbyrole.xml"));
		MockHttpConnection connection = new MockHttpConnection("datasvcs.dev.railinc.com",80,response);
		MockHttpConnectionManager mockHttpConnectionManager = new MockHttpConnectionManager(connection);
		httpClient.setHttpConnectionManager(mockHttpConnectionManager);
		sso.setHttpClient(httpClient);
		
		List<SSOUserInfo> users = sso.getUsersInRole("EHMSAPPUSR");
		assertTrue(users.size() >0);
		for (SSOUserInfo u : users) {
			if ("BCoupe".equals(u.getUsername())) {
				TestCase.fail("BCoupe should hhave been filtered, he's INACTIVE");
			}
		}
	}
	
}
