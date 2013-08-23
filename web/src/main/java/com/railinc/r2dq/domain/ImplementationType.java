package com.railinc.r2dq.domain;

public enum ImplementationType {
	/**
	 * Implementation of the data exception change is manual. That is, no source
	 * system is involved in the implementation
	 */
	Manual,
	/**
	 * Approvals are optionally sent to the source system of the data exception
	 * for implementation.
	 * 
	 */
	Automated,
	/**
	 * Any and all approval to occur will be done by the source system
	 */
	PassThrough,
	
	/**
	 * 
	 * If there is no implementation defined for a given source system and ruleNumber, by default we mark it as StromDrain. 
	 */	
	StormDrain,
	
	ForceStormDrain;

	public boolean isAutomatic() {
		return this == Automated;
	}

	public boolean isManual() {
		return this == Manual;
	}

	public boolean isPassThrough() {
		return this == PassThrough;
	}
	
	public boolean isStormDrain(){
		return this == StormDrain;
	}
	
	public boolean isForceStormDrain(){
		return this== ForceStormDrain;
	}

	public static final int MAX_LENGTH = 32;

}
