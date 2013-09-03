package com.railinc.wembley.domain.jdbc;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.railinc.wembley.api.ApplicationRepository;
import com.railinc.wembley.api.Intent;
import com.railinc.wembley.api.Notification;
import com.railinc.wembley.api.NotificationRepository;
import com.railinc.wembley.api.PipelinePhase;
import com.railinc.wembley.api.ProcessingState;
import com.railinc.wembley.api.TemplateRepository;
import com.railinc.wembley.api.address.Address;
import com.railinc.wembley.domain.Application;
import com.railinc.wembley.domain.MessageRepository;
import com.railinc.wembley.domain.TemplatePack;
import com.railinc.wembley.rrn.Rrn;

public class NotificationRepositoryImpl implements NotificationRepository {
	
	protected TemplateRepository repository;
	protected ApplicationRepository applicationRepository;
	protected MessageRepository messageRepository;
	
	protected DataSource dataSource;
	protected String schema = "wembley";
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	@Transactional
	public Long store(Notification notif) {
		// break down the notification and store as Mesage objects,etc...
		Application application = applicationRepository.byIdOrDefault(notif.getApp());
		
		Set<Intent> intents = intents(notif);
		
		Multimap<Intent, Address> emailsByIntent = addressesByIntent(notif, intents);
		
		
		KeyHolder kh = new GeneratedKeyHolder();
		NamedParameterJdbcTemplate jt = new NamedParameterJdbcTemplate(dataSource);
		jt.update(insertMessageSql(),
				insertMessageParams(notif, application),
				kh);
		Long messageId = (Long) getFirst(kh.getKeys().values(),null);
		
		for (final Intent i : emailsByIntent.keySet()) {
			jt.update(insertIntentSql(), insertIntentParams(messageId, i));
		}
		
		for (Entry<Intent, Address> s : emailsByIntent.entries()) {
			jt.update(insertDestinationSql(), insertDestinationParams(messageId, s.getKey(), s.getValue().toRrn()));
		}
		
		// store the message content
		if (notif.isSimple()){
			// insert the subject
			if (notif.hasSubject()) {
				jt.update(insertMessageContentSql(), insertSubjectParams(messageId, notif));
			}
			// insert the email body
			if (notif.hasTextBody()) {
				jt.update(insertMessageContentSql(), insertTextBodyParams(messageId, notif));
			}
			// insert the html body
			if (notif.hasHtmlBody()) {
				jt.update(insertMessageContentSql(), insertHtmlBodyParams(messageId, notif));
			}
		}
		
		return messageId;
		
	}


	protected SqlParameterSource insertHtmlBodyParams(Long messageId,
			Notification notif) {
		return insertMessageContentParams(messageId, Intent.Email, notif.getHtml(), "text/html");
	}

	protected SqlParameterSource insertTextBodyParams(Long messageId,
			Notification notif) {
		return insertMessageContentParams(messageId, Intent.Email, notif.getText(), "text/plain");
	}

	protected SqlParameterSource insertSubjectParams(Long messageId,
			Notification notif) {
		return insertMessageContentParams(messageId, Intent.Email, notif.getSubject(), "text/plain+subject");

	}

	protected SqlParameterSource insertMessageContentParams(Long messageId, Intent intent, String content, String contentType) {
		return new MapSqlParameterSource(ImmutableMap.of("messageId", messageId, "intent", intent.toString(), "content", content, "contentType", contentType));
	}


	protected String insertMessageContentSql() {
		return format("INSERT INTO %s.MSGCONTENT (MSG_ID, INTENT, CONTENT, CONTENTTYPE) VALUES (:messageId, :intent, :content, :contentType)", schema);
	}



	protected Set<Intent> intents(Notification notif) {
		Set<Intent> intents = newHashSet();
		// resolve the template
		intents.addAll(notif.getIntents());
		intents.addAll(resolveTemplate(notif).intents());
		intents.addAll(toIntents(notif.getAddresses())); // too intense ;)
		log.debug("intents : {}", intents);
		return intents;
	}












	protected Multimap<Intent, Address> addressesByIntent(Notification notif, Set<Intent> intents) {
		Multimap<Intent, Address> emailsByIntent = ArrayListMultimap.create();
		for (final Intent i : intents) {
			Collection<Address> addressesForIntent = Collections2.filter(notif.getAddresses(), new Predicate<Address>(){
				@Override
				public boolean apply(final Address a) {
					return a.supports(i);
				}});
			emailsByIntent.putAll(i, addressesForIntent);
		}
		log.debug("Email by intent {}", emailsByIntent);
		return emailsByIntent;
	}
	
	










	protected String insertDestinationSql() {
		final String sql = "INSERT INTO %s.DESTINATION (MSG_ID, RRN, INTENT, STATE) VALUES ( :messageId, :rrn, :intent, :state)";
		return format(sql, schema);
	}


	protected SqlParameterSource insertDestinationParams(Long messageId, Intent i, Rrn rrn) {
		Map<String,Object> params = newHashMap();
		params.put("messageId",messageId);
		params.put("rrn", rrn.rrn());
		params.put("intent", i.toString());
		params.put("state", ProcessingState.Unprocessed.toString());
		return new MapSqlParameterSource(params);
	}

	protected SqlParameterSource insertIntentParams(Long messageId, Intent i) {
		Map<String,Object> params = newHashMap();
		params.put("messageId",messageId);
		params.put("intent", i.toString());
		return new MapSqlParameterSource(params);
	}



	protected String insertIntentSql() {
		final String sql = "INSERT INTO %s.MSGINTENT (MSG_ID, INTENT) VALUES (:messageId, :intent)";
		return format(sql, schema);
	}



	protected SqlParameterSource insertMessageParams(Notification n, Application application) {
		Map<String,Object> params = newHashMap();
		params.put("appId", application.getApplicationId());
		params.put("testMode", JdbcUtils.yn(application.isTestMode()));
		params.put("templateName", n.getTemplate());
		params.put("templateVersion", n.getTemplateVersion());
		// TODO - remove hard coding, should come from app?
		params.put("state", ProcessingState.Unprocessed.toString());
		params.put("retryCount", 0);
		params.put("maxRetries", 3);
		params.put("phase", PipelinePhase.Ingest.toString());

		return new MapSqlParameterSource(params);
	}



	protected String insertMessageSql() {
		final String sql = "INSERT INTO %s.MSG (APP_ID, TEST_MODE, STATE, TEMPLATE_NAME, TEMPLATE_VER, RETRY_COUNT, MAX_RETRIES, PHASE) VALUES (:appId, :testMode, :state, :templateName, :templateVersion, :retryCount, :maxRetries, :phase)";
		return format(sql, schema);
	}



	protected Collection<? extends Intent> toIntents(
			Collection<Address> addresses) {
		final Collection<Intent> intents  = newArrayList();
		for (Address a : addresses) {
			intents.addAll(a.probableIntents());
		}
		return intents;
	}






	protected TemplatePack resolveTemplate(Notification notif) {
		return TemplatePack.NULL;
	}












	public void setRepository(TemplateRepository repository) {
		this.repository = repository;
	}












	public void setApplicationRepository(ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}












	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}












	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}








	public void setSchema(String schema) {
		this.schema = schema;
	}
	

}
