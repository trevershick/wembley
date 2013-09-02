package com.railinc.wembley.api.worker;

public interface Lessor {
	
	void leaseItemTo(Leasable item, Lessee to);
	/**
	 * wipes out old invalid claims. most likely will 
	 * use the claimant campfire to determine who's still around
	 * and then will remove claims for those not around anymore
	 */
	void expireLeases();
}
