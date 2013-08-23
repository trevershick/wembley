package com.railinc.r2dq.responsibility;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Responsibility;
import com.railinc.r2dq.util.PagedCollection;

@Service
@Transactional
public interface ResponsibilityService {
	PagedCollection<Responsibility> all(ResponsibilityCriteria criteria);
	void save(Responsibility s);
	void delete(Responsibility s);
	Responsibility get(Long id);
	void undelete(Responsibility ss);
	Responsibility getResponsibility(DataException data);
}
