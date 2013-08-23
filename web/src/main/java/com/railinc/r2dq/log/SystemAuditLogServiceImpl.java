package com.railinc.r2dq.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.base.Optional;
import com.railinc.r2dq.domain.SystemAuditLog;
import com.railinc.r2dq.domain.views.SystemAuditLogToView;
import com.railinc.r2dq.domain.views.SystemAuditLogView;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.PagingParameters;
import com.railinc.r2dq.util.PagingResults;
import com.railinc.r2dq.util.QueryHelper;
import static com.google.common.collect.Collections2.transform;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Order.desc;

public class SystemAuditLogServiceImpl implements SystemAuditLogService {
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void save(SystemAuditLog systemError) {
		sessionFactory.getCurrentSession().save(systemError);
	}
	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional
	public PagedCollection<SystemAuditLogView> linkLogHistory(SystemAuditLogSearchCriteria criteria) {
		
		PagedCollection<SystemAuditLogView> currentLogSet = new PagedCollection<SystemAuditLogView>();
		
		//find log entries that match criteria for entitity name and id
		criteria.setDirection(Direction.Up);
		PagedCollection<SystemAuditLogView> startingLogSet = findLogsByNameAndId(criteria);
		
		if (startingLogSet.isEmpty()) {
			//no results found in base fields for id & name so search source fields
			criteria.setDirection(Direction.Down);
			startingLogSet = findLogsByNameAndId(criteria);
			
		}
		
		currentLogSet.addAll(startingLogSet);
		
		if (null != startingLogSet && !startingLogSet.isEmpty()) {
			
			//work down first
			currentLogSet.addAll(findAllLinksToLogSet(startingLogSet,true));
			
			//work up next
			currentLogSet.addAll(findAllLinksToLogSet(startingLogSet,false));
			
		} else {
			//no results found so search criteria are invalid
			return QueryHelper.emptyPagedCollection(new PagingParameters());
		}
		
		//convert to a set to eliminate duplicate records
		final Collection<SystemAuditLogView> noDups = new HashSet<SystemAuditLogView>(currentLogSet);
		
		//sort the set by date
		List<SystemAuditLogView> temp = new ArrayList<SystemAuditLogView>(noDups);
		final Comparator<SystemAuditLogView> SORT_NEWEST_LOGS_FIRST = new Comparator<SystemAuditLogView>() {
			@Override
			public int compare(SystemAuditLogView o1, SystemAuditLogView o2) {
				return new CompareToBuilder()
					.append(o2.getCreatedDate(), o1.getCreatedDate())
					.toComparison();
			}
		};
		
		Collections.sort(temp, SORT_NEWEST_LOGS_FIRST);
		
		//PagedCollection<SystemAuditLogView> finalLogSet = new PagedCollection(temp, new PagingResults(criteria, temp.size()));
		
		return new PagedCollection(temp, new PagingResults(criteria, temp.size()));
	}
	
	public PagedCollection<SystemAuditLogView> findNextLinksToLog(SystemAuditLogView logView, boolean linkToSuccessor) {
		
		PagedCollection<SystemAuditLogView> linkedLogs = new PagedCollection<SystemAuditLogView>();
		
		if (logView.isLogSource() && !linkToSuccessor) {
			//nothing to do because source has no predecessor
			return linkedLogs;
		}
		SystemAuditLogSearchCriteria c = new SystemAuditLogSearchCriteria();
		PagedCollection<SystemAuditLogView> currentLinks = new PagedCollection<SystemAuditLogView>();
		
		if (linkToSuccessor) {
			c.setEntityId(logView.getEntityId());
			c.setEntityName(logView.getEntityName());
			c.setDirection(Direction.Down);
			currentLinks = findLogsByNameAndId(c);
		} else {
			c.setEntityId(logView.getSourceEntityId());
			c.setEntityName(logView.getSourceEntityName());
			c.setDirection(Direction.Up);
			currentLinks = findLogsByNameAndId(c);
		}
		
		//Make sure that record is not linking to itself
		if (currentLinks.contains(logView)) {
			currentLinks.remove(logView);
		}
		linkedLogs.addAll(currentLinks);
		return linkedLogs;
	}
	
	@SuppressWarnings("unused")
	private boolean setContainsRoot(PagedCollection<SystemAuditLogView> logSet) {
		for (SystemAuditLogView logView : logSet) {
			if (null != logView.getSourceEntityName() && null != logView.getSourceEntityId()) {
				return true;
			} 
	    }
		return false;
	}
	
	private PagedCollection<SystemAuditLogView> findAllLinksToLogSet(PagedCollection<SystemAuditLogView> logSet, boolean workDown) {
		
		CopyOnWriteArrayList<SystemAuditLogView> scanned = new CopyOnWriteArrayList<SystemAuditLogView>(); 
		CopyOnWriteArrayList<SystemAuditLogView> unscanned = new CopyOnWriteArrayList<SystemAuditLogView>();
		PagedCollection<SystemAuditLogView> finalLogSet = new PagedCollection<SystemAuditLogView>();
		unscanned.addAll(logSet);
		
		while (unscanned.size() > 0) {
			for (SystemAuditLogView logView : unscanned) {
				PagedCollection<SystemAuditLogView> currentLinks = new PagedCollection<SystemAuditLogView>();
				if (workDown) {
					currentLinks = findNextLinksToLog(logView, true);
				} else {
					currentLinks = findNextLinksToLog(logView, false);
				}
				unscanned.remove(logView);
				
				for (SystemAuditLogView v : currentLinks) {
					if (!unscanned.contains(v)) {
						unscanned.add(v);
					}
				}
				scanned.add(logView);
//				System.out.println(unscanned.size());
//				System.out.println(unscanned.toString());
			}
		}
		finalLogSet.addAll(scanned);
		return finalLogSet;
		
	}
	
	
	@Override
	@Transactional
	public PagedCollection<SystemAuditLogView> findLogsByNameAndId(SystemAuditLogSearchCriteria criteria) {
		
		//search for log entries that match criteria for entitity name and id 
		criteria = Optional.fromNullable(criteria).or(new SystemAuditLogSearchCriteria());
		Criteria c = sessionFactory.getCurrentSession().createCriteria(SystemAuditLog.class);
		
		if (criteria.isUp()) {
			QueryHelper.eqOrIsNull(c, criteria.getEntityName(), SystemAuditLog.PROPERTY_ENTITYNAME);
			QueryHelper.eqOrIsNull(c, criteria.getEntityId(), SystemAuditLog.PROPERTY_ENTITYID);
		} else {
			QueryHelper.eqOrIsNull(c, criteria.getEntityName(), SystemAuditLog.PROPERTY_SRCENTITYNAME);
			QueryHelper.eqOrIsNull(c, criteria.getEntityId(), SystemAuditLog.PROPERTY_SRCENTITYID);
		}
		
		//sort logs newest first
		return QueryHelper.query(criteria.getPagingParameters(), c, desc(SystemAuditLog.PROPERTY_CREATEDDATE), new SystemAuditLogToView());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public SystemAuditLogView findLogByMasterId(Long masterId) {
		
		//search for log entry that matches the unique masterId 
		DetachedCriteria c = DetachedCriteria.forClass(SystemAuditLog.class);
		c.add(eq(SystemAuditLog.PROPERTY_ID, masterId));
		List<SystemAuditLog> temp = findByCriteria(c);
		SystemAuditLog log = new SystemAuditLog();
		if (temp.size() > 0) {
			log = temp.get(0);
		} else {
			return null;
		}
		SystemAuditLogToView transformer = new SystemAuditLogToView();
		if (null != log) {
			return transformer.apply(log);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public PagedCollection<SystemAuditLogView> findAllLogExceptions() {
		
		//search for every log entry that has type = "Error" 
		SystemAuditLogSearchCriteria criteria = new SystemAuditLogSearchCriteria();
		criteria.setFindAllExceptions(true);
		DetachedCriteria c = DetachedCriteria.forClass(SystemAuditLog.class);
		c.add(eq(SystemAuditLog.PROPERTY_TYPE, SystemAuditLog.AuditLogType.ERROR));
		List<SystemAuditLog> rawResults = findByCriteria(c);
		SystemAuditLogToView transformer = new SystemAuditLogToView();
		Collection<SystemAuditLogView> finalResults = transform(rawResults, transformer);
		return new PagedCollection<SystemAuditLogView>
			(finalResults, new PagingResults(new PagingParameters(), finalResults.size()));
	}
	
	
	
	@SuppressWarnings("rawtypes")
	private List findByCriteria(DetachedCriteria dc) {
	        return dc.getExecutableCriteria(sessionFactory.getCurrentSession()).list();
	    }

	
}
