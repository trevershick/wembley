package com.railinc.r2dq.implementation;

import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.Implementation;
import com.railinc.r2dq.domain.ImplementationType;
import com.railinc.r2dq.util.PagedCollection;

public interface ImplementationService {
	ImplementationType getImplementationType(DataException dataException);
	void save(Implementation implementation);
	PagedCollection<Implementation> all(ImplementationCriteria criteria);
	void delete(Implementation implementation);
	Implementation get(Long id);
	void undelete(Implementation implementation);

}
