package com.railinc.r2dq.task.notify;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;

import com.railinc.r2dq.domain.Identity;

public class IdentityGatheringWriter implements ItemWriter<Identity>, ItemStream {
	
	private static final String IDS = "ids";
	private String outVariableName = IDS;
	private Collection<Identity> ids = new HashSet<Identity>();
	
	@Override
	public void write(List<? extends Identity> items) throws Exception {
		ids.addAll(items);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey(outVariableName)) {
			this.ids = (Collection<Identity>) executionContext.get(outVariableName);
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.put(outVariableName, newHashSet(this.ids));
	}

	@Override
	public void close() throws ItemStreamException {
	}

	public String getOutVariableName() {
		return outVariableName;
	}

	public void setOutVariableName(String outVariableName) {
		this.outVariableName = outVariableName;
	}

}
