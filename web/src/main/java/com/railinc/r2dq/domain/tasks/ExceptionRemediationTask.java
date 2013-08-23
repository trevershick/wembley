package com.railinc.r2dq.domain.tasks;

import static com.google.common.collect.Collections2.transform;

import java.util.Collection;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.railinc.r2dq.dataexception.TaskCapitalizer;
import com.railinc.r2dq.domain.DataException;
import com.railinc.r2dq.domain.SourceSystem;

/**
 * This is the standard remediation task that goes to users for 'approval'.
 * This should be created for source systems that are automated.
 * 
 * upon 'completing' the task, the data exceptions will be sent (possibly)
 * to the source system for implementation.
 * 
 * if the user has made a suggestion, then the another task may be created
 * to approve the suggestion {@link ApproveExceptionSuggestionTask}
 * 
 * @author trevershick
 *
 */
@DiscriminatorValue("EXCEPTION_REMEDIATION")
@Entity
public class ExceptionRemediationTask extends Task {
	@Override
	public String getTaskName() {
		return "Approve Changes";
	}

	@Override
	public String getTaskDescription() {
		Collection<SourceSystem> sss = getSourceSystems();
		Collection<String> names = transform(sss, SourceSystem.GET_NAME_FUNCTION);
		Set<DataException> des = getDataExceptions();
		Collection<String> attrs = transform(getDataExceptions(), new Function<DataException,String>(){
			@Override
			public String apply(DataException de) {
				return de.getMdmObjectAttribute();
			}});
		attrs = transform(attrs, new TaskCapitalizer());
		String attributes = Joiner.on(", ").skipNulls().join(attrs);
		String sourceSystems = Joiner.on(", ").skipNulls().join(names);
		// Fix 'Company Name' and in 
		return "Approve changes to " + attributes + " in " + sourceSystems;
	}



}
