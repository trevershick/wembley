package com.railinc.r2dq.responsibility;

import static com.google.common.base.Predicates.and;
import static com.railinc.r2dq.domain.Responsibility.isRuleNumberMatch;
import static com.railinc.r2dq.domain.Responsibility.isSourceSystemMatch;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Responsibility;
import com.railinc.r2dq.domain.YesNo;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.QueryHelper;


@Transactional
public class ResponsibilityServiceImpl implements ResponsibilityService {
	
	private SessionFactory sessionFactory;
	
	@Transactional
	public PagedCollection<Responsibility> all(ResponsibilityCriteria criteria) {
		criteria = Optional.fromNullable(criteria).or(new ResponsibilityCriteria());
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Responsibility.class);


		QueryHelper.freeTextSearchFor(c, criteria.getFreeText(), Responsibility.PROPERTY_RESPONSIBLE_PERSON_ID);

		QueryHelper.eqOrIsNull(c, criteria.getPerson(), Responsibility.PROPERTY_RESPONSIBLE_PERSON_ID);
		QueryHelper.eqOrIsNull(c, criteria.getPersonType(), Responsibility.PROPERTY_RESPONSIBLE_PERSON_TYPE);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumberType(), Responsibility.PROPERTY_RULENUMBER_TYPE);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumberFrom(), Responsibility.PROPERTY_RULENUMBER_FROM);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumber(), Responsibility.PROPERTY_RULENUMBER_FROM);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumberThru(), Responsibility.PROPERTY_RULENUMBER_THRU);
		QueryHelper.eqOrIsNull(c, criteria.getSourceSystem(), Responsibility.PROPERTY_SOURCESYSTEM);

		return QueryHelper.query(criteria.getPagingParameters(), c,Order.asc(Responsibility.PROPERTY_PRECEDENCE));
	}

	@Override
	@Transactional
	public void save(Responsibility s) {
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	@Override
	@Transactional
	public void delete(Responsibility s) {
		s.delete();
		sessionFactory.getCurrentSession().update(s);
	}

	@Override
	@Transactional
	public Responsibility get(Long id) {
		return (Responsibility) sessionFactory.getCurrentSession().get(Responsibility.class, id);
	}
	
	@Override
	public Responsibility getResponsibility(DataException data){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Responsibility.class);
		criteria.add(Restrictions.eq(Responsibility.PROPERTY_DELETED, YesNo.N));
		
		@SuppressWarnings("unchecked")
		List<Responsibility> responsibilityList = (List<Responsibility>)criteria.list();
		
		Collections.sort(responsibilityList);
		
		for(Responsibility responsibility: responsibilityList){
			if(and(isSourceSystemMatch(data.getSourceSystem()), isRuleNumberMatch(data.getRuleNumber())).apply(responsibility)){
				return responsibility;
			}
		}
		return null;
	}
	
	@Override
	public void undelete(Responsibility ss) {
		ss.undelete();
		sessionFactory.getCurrentSession().update(ss);
	}
	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
