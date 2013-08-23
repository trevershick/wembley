package com.railinc.r2dq.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Predicate;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.DataExceptionBundle;
import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.tasks.Task;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.identity.contact.ContactInfoNotFoundException;
import com.railinc.r2dq.task.exceptionremediation.ExceptionRemediationTaskCompletion;
import com.railinc.r2dq.task.manualremediation.ManualRemediationTaskCompletion;
import com.railinc.r2dq.util.PagedCollection;
@Service
@Transactional
public interface TaskService {
	void save(Task task);

	void notifyUserOfTasks(Identity id) throws ContactInfoNotFoundException;
	
	PagedCollection<TaskView> getMyTasks(TaskCriteria criteria);
	PagedCollection<TaskView> getAvailableTasks(TaskCriteria criteria);
	
	PagedCollection<TaskView> all(TaskCriteria criteria);

	TaskView getTask(Long taskIdToPerform);

	void completeTask(ExceptionRemediationTaskCompletion r);

	/**
	 * Will reset the notification date to null so they'll be notified of the tasks
	 * 
	 * @param selectedTaskIds - the task ids to reassign
	 * @param identity - the identity to reassign to
	 * @param notReassignedOrNull - called with ids that canot be reassigned, reutrning null from the predicate will stop the reassignment
	 * @param reassignedOrNull - called with ids that will be reassigned, returning nul from the predicate will stop the reassignment
	 * 
	 * @return true if any were reassigned, false if reassignment was aborted
	 */
	boolean reassignTasksTo(Long[] selectedTaskIds, Identity identity, Predicate<Long> notReassignedOrNull, Predicate<Long> reassignedOrNull);

	void completeTask(ManualRemediationTaskCompletion r);

	Task createTaskFor(DataException dataException);

	void createTask(DataExceptionBundle dataExceptionBundle);
	
}
