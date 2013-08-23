package com.railinc.r2dq.domain;

import com.google.common.base.Predicate;

public interface SoftDelete {
	boolean isDeleted();
	void undelete();
	void delete();
	

	public static final Predicate<SoftDelete> NOT_DELETED =  new Predicate<SoftDelete>() {
		@Override
		public boolean apply(SoftDelete arg0) {
			return ! arg0.isDeleted();
		}
	};
}
