package com.railinc.r2dq.domain.tasks;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * if an exception remediation task is submitted with suggestions
 * and the user is not of sufficient privileges then an approval task
 * will be created for admins.
 * 
 * @author trevershick
 *
 */
@DiscriminatorValue("ER_APPROVAL")
@Entity
public class ApproveExceptionSuggestionTask extends Task {
	@Override
	public String getTaskName() {
		return "Approve a Suggested Value";
	}

	@Override
	public String getTaskDescription() {
		return "Approve proposed data changes from " + this.getLinkedFrom().getTaskName();
	}


	

}
