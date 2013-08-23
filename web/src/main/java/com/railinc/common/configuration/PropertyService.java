package com.railinc.common.configuration;

import java.util.Collection;


public interface PropertyService<T extends ConfigurationProperty> {
	Collection<T> all();
	Collection<T> all(String filter);
	void save(T s);
	void delete(T s);
	T get(Long id);
}
