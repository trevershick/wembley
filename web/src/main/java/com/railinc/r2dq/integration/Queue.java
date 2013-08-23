package com.railinc.r2dq.integration;

public interface Queue {
	/**
	 * enqueues the string message (usually async) and returns
	 * @param m
	 */
	void sendMessage(Object m);
}
