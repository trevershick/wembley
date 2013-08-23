package com.railinc.r2dq.domain;

public enum ImplementationDisposition {
	/**
	 * This is the initial state and means nothing.
	 */
	Initial,
	/**
	 * The data exception was manually implemented by a data steward
	 */
	ManuallyImplemented,
	/**
	 * the data exception was sent to the source system for processing
	 */
	Sent,
	/**
	 * The source system completed the update
	 */
	Updated,
	/**
	 * The rule is not implemented in the source system
	 */
	NotImplemented,
	/**
	 * The source system acknowledges the data exception but has determined that
	 * the source system in fact matches already, thus nothing was necessary
	 */
	NoChange,
	/**
	 * The source system is awaiting user intervention to complete the update
	 */
	PendingUserAction,
	/**
	 * The source system rejected the disposition
	 */
	Rejected;
	
	public static final int MAX_LENGTH = 32;
	
	public boolean isInitial() {
		return this == Initial;
	}
}
