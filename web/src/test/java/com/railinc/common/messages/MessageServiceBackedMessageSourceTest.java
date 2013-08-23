package com.railinc.common.messages;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class MessageServiceBackedMessageSourceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_defaults_without_special_config() {
		I18nService<I18nMessage> svc = mock(I18nService.class);
		I18nServiceBackedMessageSource ms = new I18nServiceBackedMessageSource();
		ms.setService(svc);
		
		com.railinc.r2dq.domain.I18nMessage m = new com.railinc.r2dq.domain.I18nMessage();
		m.setLocale(Locale.getDefault().toString());
		m.setCode("test.default");
		m.setText("testdefaultvalue");
		
		Collection<I18nMessage> value = newArrayList();
		value.add(m);
		when(svc.all()).thenReturn(value);
		
		
		
		
		String message = ms.getMessage("test.1", new Object[]{"testarg1"}, "This is a default message", Locale.getDefault());

		assertEquals("testdefaultvalue", message);
	}
	@Test
	public void test_simple() {
		I18nService<I18nMessage> svc = mock(I18nService.class);
	
		I18nServiceBackedMessageSource ms = new I18nServiceBackedMessageSource();
		ms.setService(svc);
		
		com.railinc.r2dq.domain.I18nMessage m = new com.railinc.r2dq.domain.I18nMessage();
		m.setLocale(Locale.getDefault().toString());
		m.setCode("test.3");
		m.setText("test 3 value");
		
		Collection<I18nMessage> value = newArrayList();
		value.add(m);
		when(svc.all()).thenReturn(value);
		
		
		String message = ms.getMessage("test.3", new Object[]{"testarg1"}, "This is a default message", Locale.getDefault());
		assertEquals("test 3 value", message);
	}

	
	
	
	@Test
	public void test_def_value() {
		I18nService<I18nMessage> svc = mock(I18nService.class);
		I18nServiceBackedMessageSource ms = new I18nServiceBackedMessageSource();
		ms.setService(svc);
		
		String message = ms.getMessage("test.1", new Object[]{"testarg1"}, "This is a default message", Locale.getDefault());
		assertEquals("This is a default message", message);
	}


}
