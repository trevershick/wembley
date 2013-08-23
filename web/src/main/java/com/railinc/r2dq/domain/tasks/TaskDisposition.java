package com.railinc.r2dq.domain.tasks;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

public enum TaskDisposition {

	/**
	 * Available to be assigned, this is the initial state
	 */
	Available, // (candidate user is set)
	Claimed,
	/**
	 * the task was delegated to someone else.
	 */
	Delegated,
	/**
	 * The work has been done. Nothing more is left to do on this task.
	 */
	Completed,
	/* some tasks can be completed by a user, but need to be 'approved' by someone else */
	AwaitingApproval,
	/* for escalation */
	TimedOut,
	/**
	 * For whatever reason (none yet) the task has been cancelled
	 */
	Cancelled;

	public boolean isDelegated() {
		return this == Delegated;
	}

	public boolean isFinalState() {
		return this != Available && this != Claimed;
	}

	public static Collection<TaskDisposition> nonComplete() {
		return newArrayList(Available,Claimed);
	}

}
