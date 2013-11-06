package com.github.trevershick.wembley.api;

public enum ProcessingState {
	Unprocessed,
	Processing,
	Complete,
	Retry,
	Failed,
	Aborted;
	
	// Unprocessed -> Aborted;
	// Unprocessed -> Processing;
	// Processing -> Retry
	// Processing - Complete
	// Processing -> Failed
	// Failed -> Unprocessed
	// Failed -> Aborted
	// Retry -> Processing
}
