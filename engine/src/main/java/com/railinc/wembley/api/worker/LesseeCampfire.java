package com.railinc.wembley.api.worker;

public interface LesseeCampfire {
	/**
	 * @return a list of current active claimants
	 */
	Iterable<Lessee> claimants();
}
