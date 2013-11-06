package com.github.trevershick.wembley.api;

import com.github.trevershick.wembley.domain.TemplatePack;

public interface TemplateRepository {
	TemplatePack findTemplatePack(String applicaitionId, String name, int version);
}
