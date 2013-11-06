package com.github.trevershick.wembley.domain.jdbc;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.trevershick.wembley.api.ApplicationRepository;
import com.github.trevershick.wembley.api.Intent;
import com.github.trevershick.wembley.api.Notification;
import com.github.trevershick.wembley.api.address.Address;
import com.github.trevershick.wembley.api.address.EmailAddress;
import com.github.trevershick.wembley.domain.jdbc.ApplicationRepositoryImpl;
import com.github.trevershick.wembley.domain.jdbc.NotificationRepositoryImpl;
import com.google.common.collect.Multimap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:/spring-test-wembley-db.xml"})
public class NotificationRepositoryImplTest {

	@Autowired
	protected DataSource dataSource;
	
	@Autowired
	protected PlatformTransactionManager tm;
	
	
	protected NotificationRepositoryImpl impl;
	protected ApplicationRepository appRepo;

	@Before
	public void setup() {
		impl = new NotificationRepositoryImpl();
		appRepo = mock(ApplicationRepository.class);
		when(appRepo.byIdOrDefault(null)).thenReturn(ApplicationRepositoryImpl.DEFAULT_APP);
		impl.setApplicationRepository(appRepo);
		impl.setDataSource(dataSource);
	}
	
	@Test
	public void testIntents_inferred_from_addresses() {
		Notification n = new Notification();
		n.addAddress(new EmailAddress("trever.shick@railinc.com"));
		Set<Intent> intents = impl.intents(n);
		assertEquals(1, intents.size());
		assertEquals(Intent.Email, intents.iterator().next());
	}
	
	

	@Test
	public void testIntents_explicit() {
		Notification n = new Notification();
		n.addIntent(Intent.Email);
		Set<Intent> intents = impl.intents(n);
		assertEquals(1, intents.size());
		assertEquals(Intent.Email, intents.iterator().next());
	}


	
	@Test
	public void testIntents_inferred_and_explicit() {
		Notification n = new Notification();
		n.addIntent(Intent.Phone);
		n.addAddress(new EmailAddress("trever.shick@railinc.com"));
		
		Set<Intent> intents = impl.intents(n);
		assertEquals(2, intents.size());
		
	}

	
	@Test
	public void testEmailsByIntent_one_email_address() {

		Notification n = new Notification();
		n.addAddress(new EmailAddress("trever.shick@railinc.com"));
		
		Set<Intent> intents = newHashSet();
		intents.addAll(Arrays.asList(Intent.values()));
		
		Multimap<Intent, Address> emailsByIntent = impl.addressesByIntent(n, intents);
		assertEquals(1, emailsByIntent.size());
		
		Collection<Address> addrs = emailsByIntent.get(Intent.Email);
		assertEquals(1, addrs.size());
		assertEquals(new EmailAddress("trever.shick@railinc.com"), getFirst(addrs, null));
		
	}
	
	
	@Test
	public void testEmailsByIntent_multi_email_address() {

		Notification n = new Notification();
		n.addAddress(new EmailAddress("trever.shick@railinc.com"));
		n.addAddress(new EmailAddress("trever2.shick@railinc.com"));
		
		Set<Intent> intents = newHashSet();
		intents.addAll(Arrays.asList(Intent.values()));
		
		Multimap<Intent, Address> emailsByIntent = impl.addressesByIntent(n, intents);
		assertEquals(1, emailsByIntent.keySet().size());
		assertEquals(2, emailsByIntent.size());
		
		Collection<Address> addrs = emailsByIntent.get(Intent.Email);
		assertEquals(2, addrs.size());
		
		
	}
	
	
	@Test
	public void store_the_notification_basic() {
		Long execute = new TransactionTemplate(tm).execute(new TransactionCallback<Long>() {
			@Override
			public Long doInTransaction(TransactionStatus status) {
				Notification n = new Notification();
				n.addAddress(new EmailAddress("trever.shick@railinc.com"));
				n.addAddress(new EmailAddress("trever2.shick@railinc.com"));
				return impl.store(n);
			}
		});
		
		
	}
	
	@Test
	public void store_the_notification_basic_with_content() {
		Long execute = new TransactionTemplate(tm).execute(new TransactionCallback<Long>() {
			@Override
			public Long doInTransaction(TransactionStatus status) {
				Notification n = new Notification();
				n.addAddress(new EmailAddress("trever.shick@railinc.com"));
				n.addAddress(new EmailAddress("trever2.shick@railinc.com"));
				n.setSubject("The Subject");
				n.setText("The Text");
				n.setHtml("The HTML Body");
				return impl.store(n);
			}
		});
	}

}
