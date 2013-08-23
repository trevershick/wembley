package com.railinc.r2dq.implementation;

import static com.google.common.base.Predicates.and;
import static com.railinc.r2dq.domain.Implementation.isRuleNumberMatch;
import static com.railinc.r2dq.domain.Implementation.isSourceSystemMatch;

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
import com.railinc.r2dq.domain.Implementation;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.domain.YesNo;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.QueryHelper;

public class ImplementationServiceImpl implements ImplementationService {
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public void save(Implementation implementation){
		sessionFactory.getCurrentSession().saveOrUpdate(implementation);
	}
	
	@Override
	@Transactional(readOnly=true)
	public PagedCollection<Implementation> all(ImplementationCriteria criteria) {
		criteria = Optional.fromNullable(criteria).or(new ImplementationCriteria());
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Implementation.class);

		QueryHelper.eqOrIsNull(c, criteria.getImplementationType(), Implementation.PROPERTY_IMPLEMENTATION_TYPE);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumberType(), Implementation.PROPERTY_RULENUMBER_TYPE);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumberFrom(), Implementation.PROPERTY_RULENUMBER_FROM);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumber(), Implementation.PROPERTY_RULENUMBER_FROM);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumberThru(), Implementation.PROPERTY_RULENUMBER_THRU);
		QueryHelper.eqOrIsNull(c, criteria.getSourceSystem(), Implementation.PROPERTY_SOURCESYSTEM);

		return QueryHelper.query(criteria.getPagingParameters(), c,Order.asc(Implementation.PROPERTY_PRECEDENCE));
	}

	@Override
	@Transactional
	public void delete(Implementation implementation) {
		implementation.delete();
		sessionFactory.getCurrentSession().update(implementation);
		
	}

	@Override
	@Transactional(readOnly=true)
	public Implementation get(Long id) {
		 return (Implementation)sessionFactory.getCurrentSession().get(Implementation.class, id);
	}

	@Override
	@Transactional
	public void undelete(Implementation implementation) {
		implementation.undelete();
		sessionFactory.getCurrentSession().update(implementation);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ImplementationType getImplementationType(DataException dataException) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Implementation.class);
		criteria.add(Restrictions.eq(Implementation.PROPERTY_DELETED, YesNo.N));
		criteria.addOrder(Order.asc(Implementation.PROPERTY_PRECEDENCE));
		
		@SuppressWarnings("unchecked")
		List<Implementation> implementationList = (List<Implementation>)criteria.list();
		
		Collections.sort(implementationList);
		
		for(Implementation implementation: implementationList){
			if(and(isSourceSystemMatch(dataException.getSourceSystem()), isRuleNumberMatch(dataException.getRuleNumber())).apply(implementation)){
				return implementation.getDerivedType();
			}
		}
		return ImplementationType.StormDrain;
	}
	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}



	

}
