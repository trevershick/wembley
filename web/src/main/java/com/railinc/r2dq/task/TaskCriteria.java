package com.railinc.r2dq.task;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.tasks.TaskDisposition;
import com.railinc.r2dq.util.CriteriaValue;
import com.railinc.r2dq.util.CriteriaWithPaging;

public class TaskCriteria extends CriteriaWithPaging {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5043736016260052092L;
	
	private CriteriaValue<ArrayList<Identity>> candidates = CriteriaValue.unspecified();
	private CriteriaValue<ArrayList<Identity>> assignees = CriteriaValue.unspecified();
	private CriteriaValue<String> freeText = CriteriaValue.unspecified();

	
	public TaskCriteria() {}
	
	private CriteriaValue<ArrayList<TaskDisposition>> dispositions = CriteriaValue.unspecified();

	private CriteriaValue<Boolean> notified = CriteriaValue.unspecified(); 


	
	public CriteriaValue<ArrayList<Identity>> getCandidates() {
		return candidates;
	}

	public void addCandidate(Identity s) {
		if (s != null) {
			if (candidates.isUnspecifiedOrNull()) {
				candidates = CriteriaValue.of(new ArrayList<Identity>());
			}
			candidates.value().add(s);
		}
	}



	public void addCandidates(Collection<Identity> is) {
		if (is == null || is.isEmpty()) {
			return;
		}
		for (Identity i : is) {
			addCandidate(i);
		}
	}

	public CriteriaValue<ArrayList<Identity>> getAssignees() {
		return assignees;
	}

	public void addAssignee(Identity s) {
		if (s != null) {
			if (assignees.isUnspecifiedOrNull()) {
				assignees = CriteriaValue.of(new ArrayList<Identity>());
			}
			assignees.value().add(s);
		}
	}



	public void addAssignees(Collection<Identity> is) {
		if (is == null || is.isEmpty()) {
			return;
		}
		for (Identity i : is) {
			addAssignee(i);
		}
	}
	
	public CriteriaValue<ArrayList<TaskDisposition>> getDispositions() {
		return dispositions;
	}

	public void addDisposition(TaskDisposition s) {
		if (s != null) {
			if (dispositions.isUnspecifiedOrNull()) {
				dispositions = CriteriaValue.of(new ArrayList<TaskDisposition>());
			}
			dispositions.value().add(s);
		}
	}



	public void addDispositions(Collection<TaskDisposition> is) {
		if (is == null || is.isEmpty()) {
			return;
		}
		for (TaskDisposition i : is) {
			addDisposition(i);
		}
	}
	public void setFreeText(String value) {
		this.freeText = CriteriaValue.orUnspecified(StringUtils.trimToNull(value));
	}

	public CriteriaValue<String> getFreeText() {
		return freeText;
	}

	public void notNotified() {
		this.notified = CriteriaValue.of(false);
	}

	public CriteriaValue<Boolean> getNotified() {
		return notified;
	}
	

	

}
