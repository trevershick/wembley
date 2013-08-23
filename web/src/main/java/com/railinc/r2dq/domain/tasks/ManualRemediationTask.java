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
 * manual remediation tasks are entered into the sysetm for exceptions
 * for source systems that are NOT automated.
 * 
 * @author trevershick
 *
 */
@DiscriminatorValue("MANUAL_REMEDIATION")
@Entity
public class ManualRemediationTask extends Task {
	@Override
	public String getTaskName() {
		return "Manual Change to Source System";
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
		return "Make changes to " + attributes + " in " + sourceSystems;
	}



}
