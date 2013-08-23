package com.railinc.wembley.api.event;

public enum FailedEventReason {
	EXCEPTION_RECEIVING_EVENT, NO_MATCHING_SUBSCRIPTIONS, EXCEPTION_DELIVERING_NOTIFICATION, EXCEPTION_LOADIING_SUBSCRIPTIONS, EXCEPTION_POSTING_NOTIFICATION,
	INVALID_DELIVERY_SPEC;
}
