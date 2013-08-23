package com.railinc.r2dq.util;

import java.io.Serializable;

public class CriteriaWithPaging implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4535549333098533087L;
	private final PagingParameters pagingParams = new PagingParameters();

	public PagingParameters getPagingParameters() {
		return this.pagingParams;
	}
	
	public int getPage() {
		return pagingParams.getPage();
	}

	public void setPage(int value) {
		pagingParams.setPage(value);
	}

	public int getPageSize() {
		return pagingParams.getPageSize();
	}

	public void setPageSize(int v) {
		pagingParams.setPageSize(v);
	}
}
