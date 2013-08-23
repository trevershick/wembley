package com.railinc.r2dq.domain;

public enum YesNo {
	Y(true),N(false);
	public static final int MAX_LENGTH = 1;
	private boolean e;

	YesNo(boolean equiv) {
		this.e = equiv;
	}
	boolean toBoolean() {
		return this.e;
	}
	public static YesNo fromBoolean(Boolean b) {
		if (b == null) {
			return null;
		}
		return b.booleanValue() ? Y : N;
	}
}
