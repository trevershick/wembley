package com.railinc.r2dq.correspondence;

import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.gson.Gson;

public class TestCorrespondenceTest extends BasicTemplateTest {

	@Before
	public void fillInParameters() {
		when(this.configServiceMock.getRailincUrl()).thenReturn("http://www.railinc.com");
	}
	
	@Test
	public void basic_mechanism_is_working() {
		TestCorrespondence tc = new TestCorrespondence();
		tc.addData("x", "y");
		Map<String,Object> xdata = newHashMap();
		xdata.put("x2","y2");
		tc.addData(xdata);
		Correspondence c = this.process(tc);
		assertIsComplete(c);
		assertHasText(c, "This is x = y");
		assertHasText(c, "This is x2 = y2");
		
	}
	
	@Test
	public void reply_to() {
		TestCorrespondence tc = new TestCorrespondence();
		tc.addRecipients(newArrayList("x@y.com"));
		tc.setReplyTo("trever.shick@railinc.com", "Trever Shick");
		tc.setFrom("from@from.com","Frommer");
		tc.addData("x", "y");
		Correspondence c = this.process(tc);
		assertEquals("trever.shick@railinc.com", c.getReplyTo().getEmailAddress());
		assertEquals("Trever Shick", c.getReplyTo().getFullName());
		
		assertEquals("from@from.com", c.getFrom().getEmailAddress());
		assertEquals("Frommer", c.getFrom().getFullName());
		
		Collection<Contact> r = c.getRecipients();
		
		//we should have an x@y.com receipienit (find throws exception if not found)
		find(r, new Predicate<Contact>(){
			@Override
			public boolean apply(Contact input) {
				return "x@y.com".equals(input.getEmailAddress());
			}});
		
	}
	
	@Test
	public void testToSTringIsJson() {
		TestCorrespondence tc = new TestCorrespondence();
		tc.addData("x", "y");
		Map<String,Object> xdata = newHashMap();
		xdata.put("x2","y2");
		tc.addData(xdata);
		
		Correspondence c = this.process(tc);
		String json = c.toString();
		
		Correspondence fromJson = new Gson().fromJson(json, Correspondence.class);
		assertIsComplete(fromJson);
		assertHasText(fromJson, "This is x = y");
		assertHasText(fromJson, "This is x2 = y2");
		

	}

}
