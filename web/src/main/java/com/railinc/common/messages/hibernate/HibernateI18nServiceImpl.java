package com.railinc.common.messages.hibernate;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.lowerCase;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.railinc.common.messages.I18nService;
import com.railinc.r2dq.i18n.I18nUpdatedEvent;

@Transactional
public class HibernateI18nServiceImpl<T extends HibernateI18nMessage> implements I18nService<T> , SmartLifecycle, ApplicationEventPublisherAware {
	private SessionFactory sessionFactory;
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final Class<? extends HibernateI18nMessage> clazz;

	private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
			.setDaemon(true).setNameFormat("i18nReloader-%d").build());
	private long duration = 30;
	private TimeUnit reloadUnit = TimeUnit.SECONDS;

	private ScheduledFuture<?> reloader;
	private ApplicationEventPublisher publisher;

	public HibernateI18nServiceImpl() {
		clazz = HibernateI18nMessage.class;
	}

	protected HibernateI18nServiceImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Collection<T> all() {
		return all(null);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Collection<T> all(final String filter) {

		Criteria c = sessionFactory.getCurrentSession().createCriteria(HibernateI18nMessage.class);

		// this is so bad, but for my current purposes, i don't care.
		List<T> list = c.list();
		if (filter == null) {
			return list;
		}
		return newArrayList(Collections2.filter(list, new Predicate<HibernateI18nMessage>() {
			@Override
			public boolean apply(HibernateI18nMessage input) {
				String joined = input.toString();
				joined = lowerCase(joined);
				return joined.contains(filter);
			}
		}));
	}

	@Override
	@Transactional
	public void save(T s) {
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	@Override
	@Transactional
	public void delete(T s) {
		sessionFactory.getCurrentSession().delete(s);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public T get(Long id) {
		return (T) sessionFactory.getCurrentSession().get(clazz, id);
	}

	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Locale locale, String key) {
		if (key == null) {
			return null;
		}
		Criteria c = getSessionFactory().getCurrentSession().createCriteria(clazz);
		c.add(Restrictions.eq(HibernateI18nMessage.PROPERTY_CODE, key));
		if (locale == null) {
			c.add(Restrictions.isNull(HibernateI18nMessage.PROPERTY_LOCALE));
		} else { 
			c.add(Restrictions.eq(HibernateI18nMessage.PROPERTY_LOCALE, locale.toString()));
		}
		return (T) c.uniqueResult();
	}

	public void reload() {
		log.info("Reloading...");
		this.publisher.publishEvent(new I18nUpdatedEvent(this));
	}

	public void maybeReload() {
		if (shouldReload()) {
			reload();
		}
	}

	protected boolean shouldReload() {
		return true;
	}

	
	

	@Override
	public int getPhase() {
		// start up last
		return Integer.MAX_VALUE;
	}


	@Override
	public boolean isAutoStartup() {
		return true;
	}


	@Override
	public void stop(Runnable callback) {
		this.stop();
		callback.run();
		
	}

	@Override
	public void start() {
		log.info("Starting i18n Reloader");
		reload();
		
		
		this.reloader = this.ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					maybeReload();
				} catch (Exception e) {
					log.error("error running reloader", e);
				}
			}
		}, this.duration, this.duration, this.reloadUnit);
	}


	@Override
	public void stop() {
		if (this.reloader != null) {
			this.reloader.cancel(true);
			this.reloader = null;
		}
	}


	@Override
	public boolean isRunning() {
		return this.reloader != null;
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}