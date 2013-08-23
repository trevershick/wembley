package com.railinc.common.configuration.hibernate;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang.StringUtils.lowerCase;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.railinc.common.configuration.PropertyService;

@Transactional
public class BasicHibernatePropertyServiceImpl<T extends HibernateConfigurationProperty> implements PropertyService<T> {
	private SessionFactory sessionFactory;

	private final Class<? extends HibernateConfigurationProperty> clazz;

	public BasicHibernatePropertyServiceImpl() {
		clazz = HibernateConfigurationProperty.class;
	}

	protected BasicHibernatePropertyServiceImpl(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Collection<T> all() {
		return all(null);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Collection<T> all(final String filter) {

		Criteria c = sessionFactory.getCurrentSession().createCriteria(HibernateConfigurationProperty.class);

		// this is so bad, but for my current purposes, i don't care.
		return newArrayList(Collections2.filter(c.list(), new Predicate<HibernateConfigurationProperty>() {
			@Override
			public boolean apply(HibernateConfigurationProperty input) {
				String joined = Joiner.on(" ").join(input.getCode(), input.getName(), input.getValue(),
						input.getDescription());
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

}
