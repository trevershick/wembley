package com.railinc.wembley.api;

public interface LesseeCampfire {
	/**
	 * @return a list of current active claimants
	 */
	Iterable<Lessee> claimants();
}
