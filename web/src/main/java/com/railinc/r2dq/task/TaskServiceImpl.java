package com.railinc.r2dq.task;

import static com.google.common.collect.Iterables.partition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import com.railinc.r2dq.configuration.R2DQConfigurationService;
import com.railinc.r2dq.correspondence.Contact;
import com.railinc.r2dq.correspondence.CorrespondenceService;
import com.railinc.r2dq.dataexception.DataExceptionService;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.DataExceptionBundle;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.tasks.ApproveExceptionSuggestionTask;
import com.railinc.r2dq.domain.tasks.ExceptionRemediationTask;
import com.railinc.r2dq.domain.tasks.ManualRemediationTask;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.domain.tasks.TaskDisposition;
import com.railinc.r2dq.domain.views.TaskToView;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.identity.IdentityService;
import com.railinc.r2dq.identity.contact.ContactInfoNotFoundException;
import com.railinc.r2dq.log.SystemAuditLogTraceEvent;
import com.railinc.r2dq.sourcesystem.SourceSystemService;
import com.railinc.r2dq.task.exceptionremediation.ExceptionRemediationTaskCompletion;
import com.railinc.r2dq.task.manualremediation.ManualRemediationTaskCompletion;
import com.railinc.r2dq.task.notify.YouHaveTasks;
import com.railinc.r2dq.util.PagedCollection;
import com.railinc.r2dq.util.QueryHelper;

public class TaskServiceImpl implements TaskService, ApplicationContextAware {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private SessionFactory sessionFactory;
	private IdentityService identityService;
	private DataExceptionService dataExceptionService;
	private CorrespondenceService correspondenceService;
	private TaskToView taskToTaskViewTransformer;
	private ApplicationContext eventPublisher;
	private R2DQConfigurationService configurationService;
	private SourceSystemService sourceSystemService;
	
	
	@Override
	@Transactional
	public void save(Task task) {
		boolean createTask = task.getId()==null;
		
		sessionFactory.getCurrentSession().saveOrUpdate(task);
		
		if(createTask){
			for(DataException dataException:task.getDataExceptions()){
				publishSystemAuditLogEvent("CREATE_TASK", task, dataException);
			}
		}
		
	}

	@Override
	public PagedCollection<TaskView> all(TaskCriteria criteria) {
		criteria = Optional.fromNullable(criteria).or(new TaskCriteria());
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Task.class);
		Criteria excriteria = c.createCriteria("exceptions",Criteria.LEFT_JOIN);
		
//		Criteria exceptionCriteria = c.createCriteria("exceptions",Criteria.LEFT_JOIN);

		QueryHelper.inOrIsNull(c, criteria.getCandidates(), Task.PROPERTY_CANDIDATE);
		QueryHelper.inOrIsNull(c, criteria.getAssignees(), Task.PROPERTY_CLAIMANT);
		QueryHelper.inOrIsNull(c, criteria.getDispositions(), Task.PROPERTY_DISPOSITION);
		
		
		QueryHelper.freeTextSearchFor(excriteria, criteria.getFreeText(), 
				DataException.PROPERTY_RESPONSIBLE_PERSON_ID,
				DataException.PROPERTY_SOURCE_SYSTEM_KEY_COL,
				DataException.PROPERTY_SOURCE_SYSTEM_KEY,
				DataException.PROPERTY_SOURCE_SYSTEM_OBJ_DATA,
				DataException.PROPERTY_SOURCE_SYSTEM_VALUE,
				DataException.PROPERTY_MDM_OBJECT_ATTRIBUTE,
				DataException.PROPERTY_MDM_OBJECT_TYPE,
				DataException.PROPERTY_MDM_VALUE);

		
		
//
//		CriteriaHelper.freeTextSearchFor(c, criteria.getFreeText(), Responsibility.PROPERTY_RESPONSIBLE_PERSON_ID);
//
//		CriteriaHelper.eqOrIsNull(c, criteria.getPerson(), Responsibility.PROPERTY_RESPONSIBLE_PERSON_ID);
//		CriteriaHelper.eqOrIsNull(c, criteria.getPersonType(), Responsibility.PROPERTY_RESPONSIBLE_PERSON_TYPE);
//		CriteriaHelper.eqOrIsNull(c, criteria.getRuleNumberType(), Responsibility.PROPERTY_RULENUMBER_TYPE);
//		CriteriaHelper.eqOrIsNull(c, criteria.getRuleNumberFrom(), Responsibility.PROPERTY_RULENUMBER_FROM);
//		CriteriaHelper.eqOrIsNull(c, criteria.getRuleNumber(), Responsibility.PROPERTY_RULENUMBER_FROM);
//		CriteriaHelper.eqOrIsNull(c, criteria.getRuleNumberThru(), Responsibility.PROPERTY_RULENUMBER_THRU);
//		CriteriaHelper.eqOrIsNull(c, criteria.getSourceSystem(), Responsibility.PROPERTY_SOURCESYSTEM);


		PagedCollection<TaskView> paged = QueryHelper.query(criteria.getPagingParameters(), c, taskToTaskViewTransformer);
		return paged;
	}

	
	
	/**
	 * at this point 'criteria is ignored' except for paging information 
	 */
	@Override
	public PagedCollection<TaskView> getMyTasks(TaskCriteria criteria) {
		criteria = Optional.fromNullable(criteria).or(new TaskCriteria());
		
		

		Collection<Identity> ids = identityService.getCurrentIdentities();
		if (ids.isEmpty()) {
			return QueryHelper.emptyPagedCollection(criteria.getPagingParameters());
		}
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Task.class);
		QueryHelper.inOrIsNull(c, criteria.getDispositions(), Task.PROPERTY_DISPOSITION);
//		QueryHelper.freeTextSearchFor(c, criteria.getFreeText(), Task.PROPER);
		
		Criterion candidateMatches = Restrictions.and(Restrictions.in(Task.PROPERTY_CANDIDATE, ids), Restrictions.isNull(Task.PROPERTY_CLAIMANT));
		Criterion assigneeMatches = Restrictions.in(Task.PROPERTY_CLAIMANT, ids);
		c.add(Restrictions.or(assigneeMatches, candidateMatches));
		Order o = Order.desc(Task.PROPERTY_DUE_DATE);
		return QueryHelper.query(criteria.getPagingParameters(), c, o, taskToTaskViewTransformer);
	}

	@Override
	public PagedCollection<TaskView> getAvailableTasks(TaskCriteria criteria) {
		criteria = Optional.fromNullable(criteria).or(new TaskCriteria());
		
		Collection<Identity> ids = identityService.getCurrentIdentities();
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Task.class);
		c.add(Restrictions.in(Task.PROPERTY_CANDIDATE, ids));
		c.add(Restrictions.eq(Task.PROPERTY_DISPOSITION, TaskDisposition.Available));
		
		return QueryHelper.query(criteria.getPagingParameters(), c, taskToTaskViewTransformer);

	}

	@Override
	public TaskView getTask(Long taskId) {
		Task task = (Task) sessionFactory.getCurrentSession().get(Task.class, taskId);
		if (task == null) {
			return null;
		}
		
		TaskView taskview = taskToTaskViewTransformer.copy().convertExceptions().apply(task);
		return taskview;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void notifyUserOfTasks(Identity id) throws ContactInfoNotFoundException {
		
		// get tasks for the identity that are open and not notified (unless sendEventIfNotified = true)
		// send the correpondence.

		Contact contactInfo = identityService.contactInfo(id);
		if (contactInfo == null) {
			throw new ContactInfoNotFoundException(id);
		}
		
		Collection<Identity> ids = identityService.identitiesForPerson(id);
		
		if (ids.isEmpty()) {
			return;
		}
		
		// pull up non-complete tasks that have not been completed
		TaskCriteria criteria = new TaskCriteria();
		criteria.notNotified();
		criteria.addDispositions(TaskDisposition.nonComplete());
		
		Criteria c = sessionFactory.getCurrentSession().createCriteria(Task.class);
		QueryHelper.isNullIfFalse(c, criteria.getNotified(), Task.PROPERTY_NOTIFICATION_SENT_WHEN);
		QueryHelper.inOrIsNull(c, criteria.getDispositions(), Task.PROPERTY_DISPOSITION);

		
		c.add(Restrictions.or(Restrictions.in(Task.PROPERTY_CLAIMANT, ids), Restrictions.in(Task.PROPERTY_CANDIDATE, ids)));
		Order o = Order.desc(Task.PROPERTY_DUE_DATE);
		c.addOrder(o);

		int max = this.configurationService.getMaximumTasksPerEmail();
		Collection<Task> allTasks = (Collection<Task>) c.list();
		Iterable<List<Task>> tasksInChunks = partition(allTasks, max);
		
		for (List<Task> chunk : tasksInChunks) {
			
			YouHaveTasks youHaveTasks = new YouHaveTasks();
			youHaveTasks.addRecipient(contactInfo);
			youHaveTasks.setTasks(chunk);
			correspondenceService.convertAndSend(youHaveTasks);
			
			for (Task t : chunk) {
				t.setNotificationSent(new Date());
			}
			
			// fire off an audit log event
			publishUserNotifiedOfTasks(youHaveTasks, chunk);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean reassignTasksTo(Long[] selectedTaskIds, Identity identity, Predicate<Long> notReassigned, Predicate<Long> reassigned) {
		if (selectedTaskIds == null || selectedTaskIds.length == 0) {
			return false;
		}
		Session session = this.sessionFactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		List<Task> tasksToReassign = session.createCriteria(Task.class).add(Restrictions.in(Task.PROPERTY_ID, selectedTaskIds)).list();
		

		
		
		reassigned = alwaysTrueOr(reassigned);
		notReassigned = alwaysTrueOr(notReassigned);
		
		// split the list into can/ cannot be reassigned
		// call the callbacks to see if we should continue or abort the reassignment
		// if we should continue, iterate over the tasks and reassign them.
		Predicate<Task> canBeReassigned = new Predicate<Task>(){
			@Override
			public boolean apply(Task input) {
				return input.canBeReassigned();
		}};
		
		ImmutableListMultimap<Boolean, Task> sortedByReassignmentPossibility = Multimaps.index(tasksToReassign, Functions.forPredicate(canBeReassigned));
		
		Collection<Task> canreassign = notNull(sortedByReassignmentPossibility.get(Boolean.TRUE));
		Collection<Task> cannotreassign = notNull(sortedByReassignmentPossibility.get(Boolean.FALSE));
		
		boolean continueReassignment = false;
		
		for (Task t : cannotreassign) {
			continueReassignment |= notReassigned.apply(t.getId());
		}
		for (Task t : canreassign) {
			continueReassignment |= reassigned.apply(t.getId());
		}
		if (! continueReassignment) {
			return false;
		}
		for (Task t : canreassign) {
			t.reassign(identity);
			session.update(t);
		}
		return true;
	}

	private Predicate<Long> alwaysTrueOr(Predicate<Long> p) {
		if (p == null) {
			return Predicates.alwaysTrue();
		}
		return p;
	}

	private Collection<Task> notNull(ImmutableList<Task> l) {
		if (l == null) { return new ArrayList<Task>(); }
		return l;
	}

	protected <T extends Task> T incompleteTaskById(Long id, Class<T> clazz) {
		Task t = (Task) sessionFactory.getCurrentSession().get(clazz, id);
		if (t.isDone()) {
			throw new AlreadyDoneException();
		}
		return (T) t;
	}
	
	
	@Override
	public void completeTask(ManualRemediationTaskCompletion r) {
		Preconditions.checkArgument(r.getTaskId() != null, "taskId is not set");
		// validate
		ManualRemediationTask t = incompleteTaskById(r.getTaskId(), ManualRemediationTask.class);
		
		// TODO refactor to common task completion code
		// make sure the task is assigned
		if (!t.isClaimed()) {
			Collection<Identity> currentIdentities = this.identityService.getCurrentIdentities();
			Identity id = identityService.getMostSpecificSingularIdentity(currentIdentities);
			t.claim(id);
		}
		
		// update each exception through the exception service
		
		for (DataException de : t.getExceptions()) {
			if (r.wasApproved(de.getId())) {
				dataExceptionService.approve(de, r.getComment(de.getId()));
			} else if (r.wasIgnored(de.getId())) {
				dataExceptionService.ignore(de, r.getComment(de.getId()));
			} else if (r.wasDisapproved(de.getId())) {
				dataExceptionService.disapprove(de, r.getComment(de.getId()));
			}
			publishSystemAuditLogEvent("MANUAL_COMPLETE_TASK", t, de);
		}
		// mark the task complete
		t.markComplete();
		
		sessionFactory.getCurrentSession().save(t);
		
	}


	@Override
	public void completeTask(ExceptionRemediationTaskCompletion r) {
		
		// validate
		ExceptionRemediationTask t = incompleteTaskById(r.getTaskId(), ExceptionRemediationTask.class);

		// make sure the task is assigned
		if (!t.isClaimed()) {
			Collection<Identity> currentIdentities = this.identityService.getCurrentIdentities();
			Identity id = identityService.getMostSpecificSingularIdentity(currentIdentities);
			t.claim(id);
		}
		
		
		// if there are any suggestions, then we need to POSSIBLY 
		// create another task that references this one :(
		if (requiresApproval(r)) {
			t.markAwaitingApproval();
			new ApproveExceptionSuggestionTask();
			// assign someone
			// save it
			throw new RuntimeException("Not yet implemented");
		}
		
		
		// update each exception through the exception service
		
		for (DataException de : t.getExceptions()) {
			if (r.wasApproved(de.getId())) {
				dataExceptionService.approve(de, r.getComment(de.getId()));
			} else if (r.wasIgnored(de.getId())) {
				dataExceptionService.ignore(de, r.getComment(de.getId()));
			} else if (r.wasDisapproved(de.getId())) {
				dataExceptionService.disapprove(de, r.getComment(de.getId()));
			} else if (r.hasSuggestion(de.getId())) {
				dataExceptionService.suggest(de, r.getSuggestion(de.getId()), r.getComment(de.getId()));
			}
			publishSystemAuditLogEvent("COMPLETE_TASK", t, de);
		}
		// mark the task complete
		t.markComplete();
		
		sessionFactory.getCurrentSession().save(t);
		
	}

	private boolean requiresApproval(ExceptionRemediationTaskCompletion r) {
		// this needs to look at the user identity too
		return r.hasSuggestions();
	}

	@Override
	public Task createTaskFor(DataException dataException) {
		Preconditions.checkArgument(dataException != null, "Data Exception cannot be null");
		Preconditions.checkState(dataException.getTask() == null, "Data Exception already has a task assigned to it");
		Preconditions.checkState(dataException.getApprovalDisposition().isInitial(), "This data exception has already been through the approval process");
		
		Task task = null;
		if (dataException.getImplementationType().isManual()) {
			task = new ManualRemediationTask();
		}
		if (dataException.getImplementationType().isAutomatic()) {
			task = new ExceptionRemediationTask();	
		}
		if (task == null) {
			Preconditions.checkNotNull(task, "Unable to create a task based on looking at the implementation type"); 
		}
		
		task.setCandidate(dataException.getResponsiblePerson());
		task.addDataException(dataException);
		return task;
	}
	
	@Override
	public void createTask(DataExceptionBundle dataExceptionBundle){
		Preconditions.checkState(dataExceptionBundle.getSourceSystem()!=null, "DataExpectionBundle should have SourceSystem value");
		dataExceptionBundle.setSourceSystem(sourceSystemService.get(dataExceptionBundle.getSourceSystem().getIdentifier()));
		List<DataException> dataExceptions = dataExceptionService.get(dataExceptionBundle);
		
		if(dataExceptions == null || dataExceptions.isEmpty()){
			return;
		}
		
		Task task = createTaskFor(dataExceptions.get(0));
		if(dataExceptions.size()>1){
			for(DataException dataException:dataExceptions.subList(1, dataExceptions.size())){
				task.addDataException(dataException);
			}
		}
		save(task);
	}
	
	
	
	@Required
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Required
	public void setDataExceptionService(DataExceptionService dataExceptionService) {
		this.dataExceptionService = dataExceptionService;
	}

	@Required
	public void setTaskToTaskViewTransformer(TaskToView taskToTaskViewTransformer) {
		this.taskToTaskViewTransformer = taskToTaskViewTransformer;
	}
	
	@Required
	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.eventPublisher = applicationContext;
	}
	
	public CorrespondenceService getCorrespondenceService() {
		return correspondenceService;
	}

	@Required
	public void setCorrespondenceService(CorrespondenceService correspondenceService) {
		this.correspondenceService = correspondenceService;
	}
	
	@Required
	public void setConfigurationService(R2DQConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	protected void publishSystemAuditLogEvent(String action, Task task, DataException dataException) {
		eventPublisher.publishEvent(new SystemAuditLogTraceEvent(this, action, task.getClass().getSimpleName(), task.getId().toString(), dataException.getClass().getSimpleName(), dataException.getId().toString(), task.toJsonString()));
	}
	
	protected void publishUserNotifiedOfTasks(YouHaveTasks c, Collection<Task> tasks) {
		

		
		for (Task t : tasks) {
			try {
				eventPublisher.publishEvent(new SystemAuditLogTraceEvent(this, "NOTIFY_USER_OF_TASKS", 
					"Notification",
					"n/a",
					t.getClass().getSimpleName(),
					t.getId().toString(),
					c.toJsonString()));
			} catch (Exception e) {
				log.warn("Error auditing correspondence", e);
			}
		}
	}
	
	@Required
	public void setSourceSystemService(SourceSystemService sourceSystemService) {
		this.sourceSystemService = sourceSystemService;
	}
}
