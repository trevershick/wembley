package com.railinc.wembley.rrn;

import java.util.Collection;

import com.railinc.wembley.api.Intent;

public interface RrnExpander {

	/**
	 * take an RRN in and return RRNs for a given intent.  It IS ok for this to return logical Rrns
	 * @param rrn
	 * @param forIntent
	 * @return
	 */
	Collection<Rrn> expand(Rrn rrn, Intent forIntent);
}
