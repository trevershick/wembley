package com.railinc.r2dq.domain;


public enum ApprovalDisposition {
	/**
	 * Initial state, nothing has happened yet
	 */
	Initial,
	/**
	 * The user accepted the data exception
	 */
	Approved,
	/**
	 * The user disapproved the data exception
	 */
	Disapproved,
	/**
	 * The user has chosen to ignore the exception
	 */
	Ignored,
	/**
	 * the user suggested a new value
	 */
	NewValueSuggested,
	/**
	 * Per automated rules, this was automatically accepted
	 */
	AutomaticApproval,
	/**
	 * Per automated rules, this was automatically disapproved
	 */
	//AutomaticDisapproval,
	/**
	 * Per rules this was ignored, not by the user but by the system.
	 */
	AutomaticIgnored;
	
	public static final int MAX_LENGTH = 32;

	
	public boolean isInitial() {
		return this == Initial;
	}
	
	public boolean isApproved(){
		return this == Approved;
	}
	
	public boolean isAutomaticApproval(){
		return this == AutomaticApproval;
	}
}
