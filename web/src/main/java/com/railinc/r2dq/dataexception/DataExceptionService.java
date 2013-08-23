package com.railinc.r2dq.dataexception;

import java.util.List;

import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.DataExceptionBundle;
import com.railinc.r2dq.util.PagedCollection;

public interface DataExceptionService {
	PagedCollection<DataException> all(DataExceptionCriteria criteria);
	void save(DataException s);
	void delete(DataException s);
	void undelete(DataException ss);
	DataException get(Long id);
	
	/**
	 * evaluates the data exception and determines the implementation type (manual/automatic)
	 * for the data exception
	 * @param de
	 */
	void determineImplementationType(DataException de);

	/**
	 * Processes the data exception to determine if the 
	 * DE should be automatically accepted/rejected/ignored
	 * @param de
	 */
	void processAutomaticApprovals(DataException de);
	
	void markSourceAsProcessed(DataException dataException);
	
	
	void suggest(DataException de, String suggestion, String comment);
	void disapprove(DataException de, String comment);
	void ignore(DataException de, String comment);
	void approve(DataException de, String comment);
	List<DataException> get(DataExceptionBundle dataExceptionBundle);
}
