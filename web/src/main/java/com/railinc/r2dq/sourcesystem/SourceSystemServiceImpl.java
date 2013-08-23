package com.railinc.r2dq.sourcesystem;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.railinc.r2dq.domain.SourceSystem;
import com.railinc.r2dq.domain.YesNo;
import com.railinc.r2dq.monitoring.QueueMonitor;
import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.PagingParameters;
import com.railinc.r2dq.util.QueryHelper;

@Transactional
public class SourceSystemServiceImpl implements SourceSystemService {
	private SessionFactory sessionFactory;
	private QueueMonitor queueMonitor;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<SourceSystem> active() {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(SourceSystem.class);
		c.add(Restrictions.eq(SourceSystem.PROPERTY_DELETED, YesNo.N));
		c.addOrder(Order.asc(SourceSystem.DEFAULT_ORDER_BY_PROPERTY));
		return (Collection<SourceSystem>) c.list();
	}

	// TODO - create a source system criteria object
	@Transactional
	public Collection<SourceSystem> all(String filter) {
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(SourceSystem.class);
		QueryHelper.freeTextSearchFor(c, CriteriaValue.orUnspecified(filter), 
				SourceSystem.PROPERTY_OUTBOUND_QUEUE,
				SourceSystem.PROPERTY_ID,
				SourceSystem.PROPERTY_NAME);
		
		PagingParameters pp = new PagingParameters();
		pp.setMaxPageSize(Integer.MAX_VALUE);
		pp.setPageSize(Integer.MAX_VALUE);
		return QueryHelper.query(pp, c, Order.asc(SourceSystem.PROPERTY_ID));
			
	}

	@Override
	@Transactional
	public void save(SourceSystem s) {
		if(s.isAutomatic()){
			boolean status = queueMonitor.ping(s.getOutboundQueue());
			Preconditions.checkState(status, "Unable to ping Outbound queue. Either its not defined in MQConnection Factory or MQ is down."); 
		}
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	@Override
	@Transactional
	public void delete(SourceSystem s) {
		s.delete();
		sessionFactory.getCurrentSession().update(s);
	}

	@Override
	@Transactional
	public SourceSystem get(String id) {
		return (SourceSystem) sessionFactory.getCurrentSession().get(SourceSystem.class, id);
	}

	@Override
	public SourceSystem getOrCreate(String id) {
		SourceSystem ss = get(id);
		if (ss == null) {
			ss = new SourceSystem(id,id);
			save(ss);
		}
		return ss;
	}


	@Override
	public void undelete(SourceSystem ss) {
		ss.undelete();
		sessionFactory.getCurrentSession().update(ss);
	}
	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Required
	public void setQueueMonitor(QueueMonitor queueMonitor) {
		this.queueMonitor = queueMonitor;
	}

}
