package com.railinc.wembley.api.templating;

public interface TemplateProcessorCallback {
	void failed();
	void compilationError();
	void success();
}
