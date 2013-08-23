package com.railinc.r2dq.util;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.google.common.base.Predicate;

public class PagingResults {
	private final PagingParameters parameters = new PagingParameters();
	private int totalCount = 0;

	public PagingResults() {}
	
	public PagingResults(CriteriaWithPaging criteria, int count) {
		parameters.setPage(criteria.getPage());
		parameters.setPageSize(criteria.getPageSize());
		this.totalCount = count;
	}

	public PagingResults(PagingParameters paging, int count) {
		parameters.setPage(paging.getPage());
		parameters.setPageSize(paging.getPageSize());
		this.totalCount = count;
	}

	public Collection<Integer> getStaggeredPages() {
		// 10 pages on page 5 should show  1 3 4 5 6 7 10
		// for 'x' we need the 2 nearest pages on either side
		// then we need each page in each direction at a regular interval
		
		Set<Integer> c = newHashSet();
		int total = 10;
		
		int curr = this.getPage();

		c.add(0);
		c.add(getPageCount()-1);
		c.add(curr);
		c.add(curr - 1);
		c.add(curr + 1);
		int slotsLeft = total - c.size();
		int leftOfCenter = (getPage() - 2);
		int rightOfCenter = getPageCount() - (getPage() + 2);
		
		int inc = leftOfCenter / (slotsLeft / 2);
		inc = inc < 1 ? 1 : inc;
		for (int i=(curr - 2); i > 0;i-=inc) {
			c.add(i);
			slotsLeft--;
		}
		
		inc = rightOfCenter / slotsLeft;
		inc = inc < 1 ? 1 : inc;
		for (int i=(curr + 2); i < getPageCount();i+=inc) {
			c.add(i);
		}
		ArrayList<Integer> newArrayList2 = newArrayList(newHashSet(filter(c, new Predicate<Integer>() {
			@Override
			public boolean apply(Integer input) {
				return input.intValue() >= 0 && input.intValue() < getPageCount();
			}
		})));
		Collections.sort(newArrayList2);
		return newArrayList2;
	}
	public Collection<Integer> getPages() {
		Collection<Integer> c = newArrayList();
		for (int i=0;i<getPageCount();i++) {
			c.add(i);
		}
		return c;		
	}
	
	public int getPageCount() {
		if (parameters.getPageSize() == 1) {
			return (int) totalCount;
		}
		return (totalCount / getPageSize()) + ((totalCount % getPageSize()) > 0 ? 1 : 0);
	}

	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	public int getPage() {
		return parameters.getPage();
	}

	public void setPage(int value) {
		parameters.setPage(value);
	}

	public int getPageSize() {
		return parameters.getPageSize();
	}

	public void setPageSize(int v) {
		parameters.setPageSize(v);
	}

	public int getMaxPageSize() {
		return parameters.getMaxPageSize();
	}

	public void setMaxPageSize(int v) {
		parameters.setMaxPageSize(v);
	}

	public void set(PagingResults paging) {
		setPage(paging.getPage());
		setPageSize(paging.getPageSize());
		setTotalCount(paging.getTotalCount());
	}

}
