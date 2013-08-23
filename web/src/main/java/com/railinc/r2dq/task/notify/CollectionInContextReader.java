package com.railinc.r2dq.task.notify;

import java.util.Collections;
import java.util.Iterator;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Required;

public class CollectionInContextReader extends AbstractItemCountingItemStreamItemReader<Object> implements StepExecutionListener {

	private String contextVariableName;
	
	private Iterator<?> iterator = Collections.emptyList().iterator();
	
	public CollectionInContextReader() {
		setName("cicr");
	}
	
	@Override
	protected Object doRead() throws Exception {
		return iterator.hasNext() ? iterator.next() : null;
	}
	
	@Override
	protected void doOpen() throws Exception {}
	@Override
	protected void doClose() throws Exception {}

	@Required
	public String getContextVariableName() {
		return contextVariableName;
	}
	public void setContextVariableName(String contextVariableName) {
		this.contextVariableName = contextVariableName;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		if (stepExecution.getJobExecution().getExecutionContext().containsKey(contextVariableName)) {
			Iterable<?> iterable = (Iterable<?>) stepExecution.getJobExecution().getExecutionContext().get(contextVariableName);
			this.iterator = iterable.iterator();
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
