package com.railinc.r2dq.task.notify;

import org.springframework.batch.item.ItemProcessor;

import com.railinc.r2dq.domain.Identity;
import com.railinc.r2dq.domain.tasks.Task;
/**
 * returns the assignee for the task.  if the assignee is not set then it returns the
 * candidate.
 * @author trevershick
 *
 */
public class TaskIdentityProcessor implements ItemProcessor<Task, Identity>{

	@Override
	public Identity process(Task item) throws Exception {
		return (item.isClaimed()) ? item.getClaimant() : item.getCandidate();
	}

}
