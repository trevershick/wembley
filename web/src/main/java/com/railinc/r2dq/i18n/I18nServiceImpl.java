package com.railinc.r2dq.i18n;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.common.messages.hibernate.HibernateI18nServiceImpl;
import com.railinc.r2dq.domain.I18nMessage;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.QueryHelper;

@Transactional
public class I18nServiceImpl extends HibernateI18nServiceImpl<I18nMessage> implements I18nSearchableService , I18nServiceImplMBean {

	public I18nServiceImpl() {
		super(I18nMessage.class);
	}

	private final AtomicReference<Date> lastUpdated = new AtomicReference<Date>();

	@Override
	public PagedCollection<I18nMessage> all(I18nCriteria criteria) {
		Criteria c = getSessionFactory().getCurrentSession().createCriteria(I18nMessage.class);
		QueryHelper.freeTextSearchFor(c, criteria.getFreeText(), I18nMessage.PROPERTY_CODE, I18nMessage.PROPERTY_TEXT);
		return QueryHelper.query(criteria.getPagingParameters(), c, Order.asc(I18nMessage.PROPERTY_CODE));
	}

	private Date latestEntryUpdated() {
		Session session = getSessionFactory().openSession();
		try {
			Criteria c = session.createCriteria(I18nMessage.class);
			c.setProjection(Projections.max(I18nMessage.PROPERTY_UPDATED));
			Date d = (Date) c.uniqueResult();
			return d;
		} finally {
			session.close();
		}
	}

	@Override
	public void reload() {
		super.reload();
		this.lastUpdated.set(new Date());
	}

	@Override
	protected boolean shouldReload() {
		Date dt = latestEntryUpdated();
		if (dt == null) return false;  // there are none to get
		Date lu = lastUpdated.get();
		
		return lu == null || lu.before(dt);
	}

	@Override
	public Date getLastReloaded() {
		return this.lastUpdated.get();
	}
}
