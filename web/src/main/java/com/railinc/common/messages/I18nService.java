package com.railinc.common.messages;

import java.util.Collection;
import java.util.Locale;


public interface I18nService<T extends I18nMessage> {
	Collection<T> all();
	Collection<T> all(String filter);
	void save(T s);
	void delete(T s);
	T get(Long id);
	T get(Locale locale, String key);
}
