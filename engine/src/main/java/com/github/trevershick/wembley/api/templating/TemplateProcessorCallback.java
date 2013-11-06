package com.github.trevershick.wembley.api.templating;

public interface TemplateProcessorCallback {
	void failed();
	void compilationError();
	void success();
}
