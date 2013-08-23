package com.railinc.r2dq.util;

import java.io.Serializable;

public abstract class PagedSearchForm<C extends CriteriaWithPaging,R> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4180264693103486439L;
	
	
	private final PagedCollection<R> results = new PagedCollection<R>();

	public PagedSearchForm() {
		
	}
	
	public PagingResults getPaging() {
		return results.getPaging();
	}
	public C getCriteria() {
		C c = getCriteriaInternal();
		c.setPage(results.getPaging().getPage());
		return c;
	}
	public abstract C getCriteriaInternal();

	public void setResults(PagedCollection<R> r) {
		results.set(r);
	}
	
	public PagedCollection<R> getResults() {
		return results;
	}
	

	

}
