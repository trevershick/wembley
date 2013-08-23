package com.railinc.r2dq.task;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.tasks.TaskDisposition;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.util.PagedSearchForm;

public class TaskSearchForm extends PagedSearchForm<TaskCriteria, TaskView> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7112438745586765432L;

	public static final String DEFAULT_TASK_NAME = "tasksearch";
	
	private String query;

	private boolean includeCompleted = false;

	
	@Override
	public TaskCriteria getCriteriaInternal() {
		TaskCriteria criteria = new TaskCriteria();
		criteria.setFreeText(query);
		if (!includeCompleted) {
			criteria.addDispositions(TaskDisposition.nonComplete());
		}
		return criteria;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String in) {
		this.query = StringUtils.trimToNull(in);
	}

	public boolean isIncludeCompleted() {
		return includeCompleted;
	}

	public void setIncludeCompleted(boolean includeCompleted) {
		this.includeCompleted = includeCompleted;
	}


	

}
