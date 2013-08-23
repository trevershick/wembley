package com.railinc.r2dq.mdm;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public class MDMExceptionServiceImpl implements MDMExceptionService {
	
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void saveMDMExceptionStatus(MDMExceptionStatus mdmExceptionStatus) {
		sessionFactory.getCurrentSession().save(mdmExceptionStatus);
	}
	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
