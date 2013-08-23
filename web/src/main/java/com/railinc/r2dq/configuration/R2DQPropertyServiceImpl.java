package com.railinc.r2dq.configuration;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.common.configuration.hibernate.BasicHibernatePropertyServiceImpl;
import com.railinc.r2dq.domain.ConfigurationProperty;
import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.PagingParameters;
import com.railinc.r2dq.util.QueryHelper;

@Transactional
public class R2DQPropertyServiceImpl extends BasicHibernatePropertyServiceImpl<ConfigurationProperty> {
	
	public R2DQPropertyServiceImpl() {
		super(ConfigurationProperty.class);
	}
	
	@Transactional
	public Collection<ConfigurationProperty> all(String filter) {
		
		Criteria c = getSessionFactory().getCurrentSession().createCriteria(ConfigurationProperty.class);
		QueryHelper.freeTextSearchFor(c, CriteriaValue.orUnspecified(filter),
				ConfigurationProperty.PROPERTY_VALUE,
				ConfigurationProperty.PROPERTY_DESC,
				ConfigurationProperty.PROPERTY_CODE,
				ConfigurationProperty.PROPERTY_NAME);
		
		PagingParameters pp = new PagingParameters();
		pp.setMaxPageSize(Integer.MAX_VALUE);
		pp.setPageSize(Integer.MAX_VALUE);
		return QueryHelper.query(pp, c, Order.asc(ConfigurationProperty.PROPERTY_CODE));
			
	}
}
