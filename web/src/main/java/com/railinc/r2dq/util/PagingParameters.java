
package com.railinc.r2dq.util;

import java.io.Serializable;


public class PagingParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 848207809847830894L;
	
	public static final int MIN_PAGE_SIZE = 1;
	public static final int DEFAULT_PAGE = 0;
	public static final int DEFAULT_PAGE_SIZE = 25;
	public static final int DEFAULT_MAX_PAGE_SIZE = 100;
	/**
	 * Zero based
	 */
	private int page = DEFAULT_PAGE;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private int maxPageSize = DEFAULT_MAX_PAGE_SIZE;
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int value) {
		if (value <= 0) {
			value = DEFAULT_PAGE;
		}
		this.page = value;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int v) {
		this.pageSize = Math.max(Math.min(maxPageSize, v), MIN_PAGE_SIZE);
	}
	
	public int getMaxPageSize() {
		return maxPageSize;
	}
	public void setMaxPageSize(int v) {
		maxPageSize = Math.max(v, MIN_PAGE_SIZE);
	}

	
}
