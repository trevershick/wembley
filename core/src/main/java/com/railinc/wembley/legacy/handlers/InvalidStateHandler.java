package com.railinc.wembley.legacy.handlers;

public interface InvalidStateHandler {

	void handleInvalidState(String msg, Throwable exception);
}
