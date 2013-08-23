package com.railinc.wembley.api.templating;

import com.railinc.wembley.api.Intent;

public interface Template {
	String contentType();
	String applicationId();
	String logicalName();
	String content();
	Intent intent();
}
