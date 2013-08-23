package com.railinc.r2dq.i18n;

import com.railinc.common.messages.I18nService;
import com.railinc.r2dq.domain.I18nMessage;
import com.railinc.r2dq.util.PagedCollection;

public interface I18nSearchableService extends I18nService<I18nMessage> {

	public PagedCollection<I18nMessage> all(I18nCriteria criteria);
}
