package com.github.trevershick.wembley.api.templating;

import java.io.OutputStream;

public interface TemplateProcessor {
	void process(Template t, OutputStream out, TemplateProcessorCallback callback);
}
