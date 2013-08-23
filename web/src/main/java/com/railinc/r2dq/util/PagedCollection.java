package com.railinc.r2dq.util;

import java.util.ArrayList;
import java.util.Collection;

public class PagedCollection<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5341450194060025414L;

	private final PagingResults paging = new PagingResults();

	public PagedCollection() {
	}

	public PagedCollection(Collection<T> all) {
		addAll(all);
	}

	public PagedCollection(Collection<T> all, PagingResults paging) {
		this(all);
		this.paging.set(paging);
	}

	public PagingResults getPaging() {
		return paging;
	}

	public void set(PagedCollection<T> newResults) {
		this.clear();
		this.addAll(newResults);
		this.paging.set(newResults.getPaging());
	}

}
