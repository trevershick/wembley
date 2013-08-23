package com.railinc.r2dq.task.admin;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.IdentityType;
import com.railinc.r2dq.domain.tasks.TaskDisposition;
import com.railinc.r2dq.domain.views.TaskView;
import com.railinc.r2dq.util.PagedSearchForm;

public class AdminTaskSearchForm extends PagedSearchForm<AdminTaskCriteria, TaskView> {

	public static interface ReassignmentChecks {} 
	/**
	 * 
	 */
	private static final long serialVersionUID = -7112438745586765432L;

	public static final String DEFAULT_TASK_NAME = "admintasksearch";
	
	private String query;

	private boolean includeCompleted = false;

	
	private IdentityType personType;
	private String person;

	@NotNull(groups={ReassignmentChecks.class})
	private IdentityType newPersonType;
	
	@NotNull(groups={ReassignmentChecks.class})
	@Size(groups={ReassignmentChecks.class},min=1)
	private String newPerson;
	
	@NotNull(groups={ReassignmentChecks.class})
	private List<Long> selectedTaskIds = newArrayList();
	

	


	public IdentityType getPersonType() {
		return personType;
	}

	public void setPersonType(IdentityType personType) {
		this.personType = personType;
	}
	
	@Override
	public AdminTaskCriteria getCriteriaInternal() {
		AdminTaskCriteria criteria = new AdminTaskCriteria();
		criteria.setFreeText(query);
		if (!includeCompleted) {
			criteria.addDispositions(TaskDisposition.nonComplete());
		}
		criteria.setPerson(person);
		criteria.setPersonType(personType);
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

	public Long[] getSelectedTaskIds() {
		return selectedTaskIds.toArray(new Long[selectedTaskIds.size()]);
	}

	
	public int getSelectedTaskIdsCount() {
		return selectedTaskIds.size();
	}
	
	public void setSelectedTaskIds(Long[] selectedTaskIds) {
		if (selectedTaskIds != null) {
			this.selectedTaskIds.addAll(Arrays.asList(selectedTaskIds));
		}
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public IdentityType getNewPersonType() {
		return this.newPersonType;
	}
	public String getNewPerson() {
		return this.newPerson;
	}

	public void setNewPersonType(IdentityType newPersonType) {
		this.newPersonType = newPersonType;
	}

	public void setNewPerson(String newPerson) {
		this.newPerson = newPerson;
	}

	public boolean isShowReassignButton() {
		return this.selectedTaskIds.size() > 0;
	}

}
