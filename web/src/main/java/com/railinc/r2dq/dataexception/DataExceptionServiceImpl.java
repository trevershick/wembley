package com.railinc.r2dq.dataexception;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.railinc.r2dq.dataexception.event.DataExceptionEvent;
import com.railinc.r2dq.dataexception.event.DataExceptionIgnoredEvent;
import com.railinc.r2dq.dataexception.event.DataExceptionImplementedEvent;
import com.railinc.r2dq.dataexception.event.DataExceptionRejectedEvent;
import com.railinc.r2dq.dataexception.implementation.ImplementDataExceptionService;
import com.railinc.r2dq.domain.ApprovalDisposition;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.DataExceptionBundle;
import com.railinc.r2dq.domain.ImplementationDisposition;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.log.SystemAuditLogTraceEvent;
import com.railinc.r2dq.messages.MessageService;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.QueryHelper;

public class DataExceptionServiceImpl implements DataExceptionService, ApplicationContextAware {
	
	private SessionFactory sessionFactory;
	private ApplicationContext applicationContext;
	private MessageService messageService;
	private ImplementDataExceptionService implementDataExceptionService;

	@Transactional
	public PagedCollection<DataException> all(DataExceptionCriteria criteria) {
		criteria = Optional.fromNullable(criteria).or(new DataExceptionCriteria());
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(DataException.class);

		
		QueryHelper.freeTextSearchFor(c, criteria.getFreeText(), 
				DataException.PROPERTY_RESPONSIBLE_PERSON_ID,
				DataException.PROPERTY_SOURCE_SYSTEM_KEY_COL,
				DataException.PROPERTY_SOURCE_SYSTEM_KEY,
				DataException.PROPERTY_SOURCE_SYSTEM_OBJ_DATA,
				DataException.PROPERTY_SOURCE_SYSTEM_VALUE,
				DataException.PROPERTY_MDM_OBJECT_ATTRIBUTE,
				DataException.PROPERTY_MDM_OBJECT_TYPE,
				DataException.PROPERTY_MDM_VALUE);

		// TODO - make this happen via annotations
		QueryHelper.eqOrLikeOrIsNull(c, criteria.getPerson(), DataException.PROPERTY_RESPONSIBLE_PERSON_ID);
		QueryHelper.eqOrIsNull(c, criteria.getPersonType(), DataException.PROPERTY_RESPONSIBLE_PERSON_TYPE);
		QueryHelper.eqOrIsNull(c, criteria.getRuleNumber(), DataException.PROPERTY_RULE_NUMBER);
		QueryHelper.eqOrIsNull(c, criteria.getSourceSystem(), DataException.PROPERTY_SOURCE_SYSTEM);
		
		QueryHelper.eqOrLikeOrIsNull(c, criteria.getMdmAttributevalue(), DataException.PROPERTY_MDM_VALUE);
		QueryHelper.eqOrLikeOrIsNull(c, criteria.getMdmObjectAttribute(), DataException.PROPERTY_MDM_OBJECT_ATTRIBUTE);
		QueryHelper.eqOrLikeOrIsNull(c, criteria.getMdmObjectType(), DataException.PROPERTY_MDM_OBJECT_TYPE);

		QueryHelper.eqOrLikeOrIsNull(c, criteria.getSourceSystemKeyColumn(), DataException.PROPERTY_SOURCE_SYSTEM_KEY_COL);
		QueryHelper.eqOrLikeOrIsNull(c, criteria.getSourceSystemKey(), DataException.PROPERTY_SOURCE_SYSTEM_KEY);
		QueryHelper.eqOrLikeOrIsNull(c, criteria.getSourceSystemObjectData(), DataException.PROPERTY_SOURCE_SYSTEM_OBJ_DATA);
		QueryHelper.eqOrLikeOrIsNull(c, criteria.getSourceSystemValue(), DataException.PROPERTY_SOURCE_SYSTEM_VALUE);

		
		return QueryHelper.query(criteria.getPagingParameters(), c);

	}


	@Override
	@Transactional
	public void save(DataException s) {
		boolean create = s.getId() == null;
		sessionFactory.getCurrentSession().saveOrUpdate(s);
		if(create){
			publishSystemAuditLogEvent("CREATE_DATA_EXCEPTION", s);
		}
	}

	@Override
	@Transactional
	public void delete(DataException s) {
		s.delete();
		sessionFactory.getCurrentSession().update(s);
		publishSystemAuditLogEvent("DELETE_DATA_EXCEPTION", s);
	}

	@Override
	@Transactional
	public DataException get(Long id) {
		return (DataException) sessionFactory.getCurrentSession().get(DataException.class, id);
	}
	
	@Override
	public void undelete(DataException ss) {
		ss.undelete();
		sessionFactory.getCurrentSession().update(ss);
		publishSystemAuditLogEvent("UNDELETE_DATA_EXCEPTION", ss);
	}
	
	@Override
	@Transactional
	public void markSourceAsProcessed(DataException dataException) {
		messageService.markAsProcessed(dataException.getSource());
	}
	
	@Override
	public void suggest(DataException de, String suggestion, String comment) {
		de.setApprovalDisposition(ApprovalDisposition.NewValueSuggested);
		de.setUserSuggestedValue(suggestion);
		sessionFactory.getCurrentSession().save(de);
	
		// this should only be called for an 'automatic' impl type data exception
		// because the DE is reviewed, the user finds that the value should be something
		// else, so the source system is the requested to update the value to the suggested 
		// value.
		if (!de.getImplementationType().isAutomatic()) {
			throw new RuntimeException("WTF, manual remediation should not be suggesting values");
		}
		
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public void disapprove(final DataException de, String comment) {
		de.setApprovalDisposition(ApprovalDisposition.Disapproved);
		de.setUserSuggestedValue(null);
		
		implement(de, comment, new Supplier<DataExceptionEvent>(){
			@Override
			public DataExceptionEvent get() {
				return new DataExceptionRejectedEvent(this, de.getMdmExceptionId());
			}});

		publishSystemAuditLogEvent("REJECT_DATA_EXCEPTION", de);
	}

	@Override
	public void ignore(final DataException de, String comment) {
		de.setApprovalDisposition(ApprovalDisposition.Ignored);
		de.setUserSuggestedValue(null);

		implement(de, comment, new Supplier<DataExceptionEvent>(){
			@Override
			public DataExceptionEvent get() {
				return new DataExceptionIgnoredEvent(this, de.getMdmExceptionId());
			}});
		
		publishSystemAuditLogEvent("IGNORE_DATA_EXCEPTION", de);
	}

	@Override
	public void approve(final DataException de, String comment) {
		de.setApprovalDisposition(ApprovalDisposition.Approved);
		de.setUserSuggestedValue(null);

		implement(de, comment, new Supplier<DataExceptionEvent>(){
			@Override
			public DataExceptionEvent get() {
				return new DataExceptionImplementedEvent(this, de.getMdmExceptionId());
			}});
	}
	
	/**
	 * Currently DIES for non-manual implementation data exceptions
	 * 
	 * 1) marks the data exception manually implemented (if manual)
	 * 2) requests an event from the deFactory to be fired after marking manually implemented
	 * 3) saves the DataException
	 * 4) fires any events provided in step #2
	 *  
	 * @param de
	 * @param deFactory
	 */
	private void implement(DataException de, String comment, Supplier<DataExceptionEvent> deFactory) {
		String auditLogMessage = "IMPLMNT_DATA_EXCPTN";
		
		de.setUserComment(comment);
		// if the data exception implementation type is manual, then we can simply 
		// fire the data exception accepted event so that MDM will be notified
		
		if (de.isManual()) {
			de.setImplementationDisposition(ImplementationDisposition.ManuallyImplemented);
			auditLogMessage = "MANUAL_IMPLMNT_DATA_EXCPTN";
		} 
		
		if(de.isAvailableForSourceSystemToImplement()) {
			implementDataExceptionService.implement(de);
			de.setImplementationDisposition(ImplementationDisposition.Sent);
			auditLogMessage = "NOTFY_SS_TO_IMPLMNT_DATA_EXCPTN";
		}
		
		sessionFactory.getCurrentSession().save(de);
		
		if(de.isApprovalDispositionNeedToNotify() && deFactory!=null && deFactory.get()!=null){
			applicationContext.publishEvent(deFactory.get());
		}
		publishSystemAuditLogEvent(auditLogMessage, de);
	}
	
	@Override
	public void determineImplementationType(DataException de) {
		// they're all manual now, but based on registered rule numbers for a given source system
		// we will mark this as automatic and send it to the source system.
		
		// note - if we mark this as automatic and it goes to the source system then returns 'not implemented'
		// we need to add a rule to not send the specified rule number to that 
		// we also then need to switch the impl type and reprocess
		de.setImplementationType(ImplementationType.Manual);
	}


	@Override
	public void processAutomaticApprovals(DataException de) {
		// is the DE to be passed through ApprovalDisposition.PassThrough
		// is the DE to be ignored ApprovalDisposition.AutomaticIgnored
		
		// is the DE ApprovalDisposition.AutomaticApproval
		// is the DE ApprovalDisposition.AutomaticDisapproval
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public List<DataException> get(DataExceptionBundle dataExceptionBundle) {
		Criteria c = sessionFactory.getCurrentSession().createCriteria(DataException.class);
		c.add(Restrictions.eq(DataException.PROPERTY_SOURCE_SYSTEM, dataExceptionBundle.getSourceSystem()));
		c.add(Restrictions.eq(DataException.PROPERTY_SOURCE_SYSTEM_KEY_COL, dataExceptionBundle.getSourceSystemColumnName()));
		c.add(Restrictions.eq(DataException.PROPERTY_SOURCE_SYSTEM_KEY, dataExceptionBundle.getSourceSystemIdentifier()));
		c.add(Restrictions.eq(DataException.PROPERTY_RESPONSIBLE_PERSON_ID, dataExceptionBundle.getResponsiblePerson().getId()));
		c.add(Restrictions.eq(DataException.PROPERTY_RESPONSIBLE_PERSON_TYPE, dataExceptionBundle.getResponsiblePerson().getType()));
		c.add(Restrictions.eq(DataException.PROPERTY_IMPL_TYPE, dataExceptionBundle.getImplementationType()));
		c.add(Restrictions.isNull(DataException.PROPERTY_TASK));
		return (List<DataException>)c.list();
	}
	
	
	private void publishSystemAuditLogEvent(String action, DataException de){
		applicationContext.publishEvent(new SystemAuditLogTraceEvent(this, action, DataException.class.getSimpleName(), de.getId().toString(), de.getSource().getClass().getSimpleName(), de.getSource().getIdentifier().toString(), de.toJsonString()));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Required
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	public void setImplementDataExceptionService(ImplementDataExceptionService implementDataExceptionService) {
		this.implementDataExceptionService = implementDataExceptionService;
	}
	
	

}
