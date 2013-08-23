package com.railinc.r2dq.util;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Lists.newArrayList;

import java.beans.ExceptionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

public class SimpleExceptionListener implements ExceptionListener {
	private final Logger log;
	private final Supplier<List<Exception>> LAZY_SUPPLIER = new Supplier<List<Exception>>(){
		@Override
		public List<Exception> get() {
			return newArrayList();
		}};
		
		
	List<Exception> exceptions = null;
	
	public SimpleExceptionListener(){ 
		 this(null);
	}
	
	public SimpleExceptionListener(Logger l) {
		log = (l != null) ? l : LoggerFactory.getLogger(SimpleExceptionListener.class);
	}

	private final List<Exception> exceptions() {
		return exceptions = Optional.fromNullable(exceptions).or(LAZY_SUPPLIER);
	}
	
	public Collection<Exception> getExceptions() {
		if (exceptions == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(exceptions);
	}
	
	public Exception getException() {
		if (! hasExceptions()) {
			throw new IllegalStateException("there are no exceptions");
		}
		return getFirst(exceptions, null);
	}
	public boolean hasExceptions() { 
		return exceptions != null && exceptions.size() > 0;
	}
	
	@Override
	public void exceptionThrown(Exception e) {
		Preconditions.checkArgument(e != null,"You cannot pass null to this exception listener");
		log.warn("Exceptions have occurred", e);	
		exceptions().add(e);
	}




}
