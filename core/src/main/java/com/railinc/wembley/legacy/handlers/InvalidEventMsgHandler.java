package com.railinc.wembley.legacy.handlers;

public interface InvalidEventMsgHandler {

	void handleInvalidEventMsg(String eventXml, Throwable e);
}
