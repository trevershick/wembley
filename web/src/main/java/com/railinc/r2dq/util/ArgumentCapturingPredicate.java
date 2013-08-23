package com.railinc.r2dq.util;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import com.google.common.base.Predicate;

public class ArgumentCapturingPredicate<T> implements Predicate<T> {

	Collection<T> arguments = newArrayList();
	private final boolean returns;
	
	public ArgumentCapturingPredicate(boolean returnValue) {
		this.returns = returnValue;
	}
	public ArgumentCapturingPredicate() {
		this.returns = true;
	}

	@Override
	public boolean apply(T input) {
		arguments.add(input);
		return returns;
	}
	
	public Collection<T> captured() {
		return arguments;
	}

}
