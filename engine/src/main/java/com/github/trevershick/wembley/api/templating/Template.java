package com.github.trevershick.wembley.api.templating;

import com.github.trevershick.wembley.api.Intent;

public interface Template {
	String contentType();
	String applicationId();
	String logicalName();
	String content();
	Intent intent();
}
