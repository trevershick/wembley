package com.railinc.r2dq.correspondence;

public class Pair<L,R> {
	private final L left;
	private final R right;
	
	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}
	
	public R right() { return right; }
	public L left() { return left; }
}
