package com.railinc.wembley.api;

import com.railinc.wembley.domain.TemplatePack;

public interface TemplateRepository {
	TemplatePack findTemplatePack(String applicaitionId, String name, int version);
}
