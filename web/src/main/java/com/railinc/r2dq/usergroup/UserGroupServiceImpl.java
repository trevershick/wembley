package com.railinc.r2dq.usergroup;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.UserGroup;
import com.railinc.r2dq.domain.UserGroupMember;
import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.QueryHelper;

@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Collection<UserGroup> all(String filter) {
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserGroup.class);
		QueryHelper.freeTextSearchFor(c, 
				CriteriaValue.orUnspecified(filter), 
				UserGroup.PROPERTY_ID,
				UserGroup.PROPERTY_NAME);
		
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		c.addOrder(Order.asc(UserGroup.PROPERTY_ID));
		
		return c.list();
			
	}

	@Override
	@Transactional
	public void save(UserGroup s) {
		sessionFactory.getCurrentSession().saveOrUpdate(s);
	}

	@Override
	@Transactional
	public void delete(UserGroup s) {
		s.delete();
		sessionFactory.getCurrentSession().update(s);
	}

	@Override
	@Transactional
	public UserGroup get(String id) {
		return (UserGroup) sessionFactory.getCurrentSession().get(UserGroup.class, id);
	}

	@Override
	public void undelete(UserGroup ss) {
		ss.undelete();
		sessionFactory.getCurrentSession().update(ss);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<UserGroup> groupsForUser(String userLogin) {
		if (isBlank(userLogin)) { return Collections.emptyList(); }
		Criteria c = sessionFactory.getCurrentSession().createCriteria(UserGroup.class);
		c.createCriteria(UserGroup.PROPERTY_MEMBERS).add(Restrictions.eq(UserGroupMember.PROPERTY_SSO_ID, userLogin));
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return c.list();
	}

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
	}
}
