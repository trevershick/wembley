package com.railinc.r2dq.sso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.beans.ExceptionListener;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.railinc.serviceability.monitoring.Status;

public class SingleSignOnV2ImplTest {

	
	private GetMethod getMethod;
	
	private SingleSignOnV2Impl sso;

	private HttpClient httpClient;

	private ExceptionListener sel;


	@Before
	public void setUp() throws Exception {
		sel = new ExceptionListener() {
			@Override
			public void exceptionThrown(Exception e) {
				throw new RuntimeException(e);
			}};
		sso = new SingleSignOnV2Impl();
		httpClient = mock(HttpClient.class);
		sso.setHttpClient(httpClient);
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGroupsForUserString() throws HttpException, IOException {
		final String output = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><user xmlns='http://resources.sso.railinc.com/v2' id='sdtxs01' email='trever.shick@railinc.com' type='Web' status='Active'><permissions><permission id='PUPSRULEMGR'/><permission id='SSOADM'/><permission id='SSOUSRADM'/><permission id='PUPSAPPSUPPORT'/><permission id='EHMSINTERRISAPPADM'/><permission id='SCRSCOMPADM'><entities><entity id='RAIL' type='mark'/></entities></permission><permission id='PUPSAPPADM'/><permission id='LOAAPPADM'/><permission id='IRFISTATNUSER'/><permission id='FINDUSRAILCNTCADMM'><entities><entity id='BNSF' type='mark'/></entities></permission><permission id='SSOSUPPORT'/><permission id='SCRSAPPUSR'><entities><entity id='RAIL' type='mark'/></entities></permission><permission id='IRFICIFUSER'><entities><entity id='RAIL' type='mark'/></entities></permission><permission id='MRIRFAPPADM'/><permission id='IRFICMDTYUSER'/><permission id='SSORUNAS'/><permission id='RMCAPPADM'/><permission id='IRFIAPPADM'/><permission id='SCRSAPPADM'/><permission id='MRIRFITSUPPORT'/><permission id='FINDUSRAILCOMPADM'><entities><entity id='BNSF' type='mark'/></entities></permission><permission id='RMCMSGVWR'/><permission id='FINDUSRAILITSAPPADM'/><permission id='CSSPAPPADM'/><permission id='FINDUSRAILAPPADM'/><permission id='PUPSADAPTAPPADM'/><permission id='PUPSUSER'/><permission id='LOASUPPORT'><entities><entity id='RAIL' type='mark'/></entities></permission><permission id='IRFIMARKUSER'/><permission id='CSSPAGENT'/><permission id='PUPSADAPTSUPPORT'/><permission id='IRFIROUTEUSER'/></permissions></user>";
		InputStream stream = new ByteArrayInputStream(output.getBytes("UTF-8"));
		
		when(httpClient.executeMethod(any(GetMethod.class))).thenAnswer(new ExecuteMethodAnswer(stream));
		
		Collection<String> gs = sso.groupsForUser("test", sel);
		assertEquals(32,gs.size());
		assertEquals("PUPSRULEMGR", gs.iterator().next());
		assertEquals("application/xml",getMethod.getRequestHeader("Accept").getValue());

	}




	@Test
	public void testIsNotDown_After_IOException() throws IOException {
		InputStream stream = mock(InputStream.class);
		when(stream.read()).thenThrow(IOException.class);
		
		when(httpClient.executeMethod(any(GetMethod.class))).thenAnswer(new ExecuteMethodAnswer(stream));
		
		sso.groupsForUser("test", null);
		assertFalse("Should NOT be marked as down just becaue of IO exception", this.sso.isDown());

	}

	@Test
	public void testIsDown_after_UnknownHostException() throws IOException, InterruptedException {
		sso.setRecheckAfterDuration(5);
		sso.setRecheckAfterUnit(TimeUnit.SECONDS);
		
		forceDownCondition();
		assertTrue("Should be marked as down", this.sso.isDown());
		
		
		Thread.sleep(10000);
		assertFalse("Should be marked as back up", this.sso.isDown());

	}

	private void forceDownCondition() throws IOException, HttpException {
		InputStream stream = mock(InputStream.class);
		when(stream.read()).thenThrow(UnknownHostException.class);
		
		when(httpClient.executeMethod(any(GetMethod.class))).thenAnswer(new ExecuteMethodAnswer(stream));
		
		
		sso.groupsForUser("test", null);
	}

	@Test
	public void testCheck() throws HttpException, IOException {
		forceDownCondition();
		assertTrue("Should be marked as down", this.sso.isDown());
		reset(httpClient);
		
		Status status = this.sso.check().getStatus();
		assertTrue(status == Status.ERROR);
		verify(httpClient, times(0)).executeMethod((HttpMethod) anyObject());
	}



	   
	private void setResponseStream(HttpMethodBase httpMethod, InputStream inputStream) throws NoSuchFieldException,
			IllegalAccessException {
		Field privateResponseStream = HttpMethodBase.class.getDeclaredField("responseStream");
		privateResponseStream.setAccessible(true);
		privateResponseStream.set(httpMethod, inputStream);
	}

	private class ExecuteMethodAnswer implements Answer {
		private InputStream source;
		public ExecuteMethodAnswer(InputStream is) {
			this.source = is;
		}
		public Object answer(InvocationOnMock invocation) throws FileNotFoundException, NoSuchFieldException,
				IllegalAccessException {
			getMethod = (GetMethod) invocation.getArguments()[0];
			setResponseStream(getMethod, source);
			return HttpStatus.SC_OK;
		}
	}

}
