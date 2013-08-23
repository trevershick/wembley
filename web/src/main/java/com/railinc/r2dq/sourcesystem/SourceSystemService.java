package com.railinc.r2dq.sourcesystem;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.SourceSystem;

@Transactional
public interface SourceSystemService {
	Collection<SourceSystem> all(String filter);
	void save(SourceSystem s);
	void delete(SourceSystem s);
	SourceSystem get(String id);
	void undelete(SourceSystem ss);
	Collection<SourceSystem> active();
	SourceSystem getOrCreate(String id);
}
