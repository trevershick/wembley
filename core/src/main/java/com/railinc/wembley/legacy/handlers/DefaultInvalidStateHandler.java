package com.railinc.wembley.legacy.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultInvalidStateHandler implements InvalidStateHandler {

	private static final Logger log = LoggerFactory.getLogger(DefaultInvalidStateHandler.class);

	public void handleInvalidState(String msg, Throwable exception) {

		if(exception == null) {
			log.error(msg);
		} else {
			log.error(msg, exception);
		}
	}
}
