package com.github.trevershick.wembley.pipeline.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Collection;

import com.github.trevershick.wembley.api.DestinationRepository;
import com.github.trevershick.wembley.api.ProcessingState;
import com.github.trevershick.wembley.domain.Destination;
import com.github.trevershick.wembley.pipeline.AddressProcessing;
import com.github.trevershick.wembley.rrn.Rrn;
import com.github.trevershick.wembley.rrn.RrnExpander;

public class AddressProcessingImpl implements AddressProcessing {
	DestinationRepository destinationRepository;
	Collection<RrnExpander> expanders = newArrayList();

	@Override
	public void resolveAddresses(final long messageId) {
		// get all addresses that are unprocessed
		// resolve them recursively
		// store all resolved addresses 
		// mark them as resolved
		final Collection<Destination> ds = destinationRepository.byMessageId(messageId);

		
		
		// using the rrn resolvers, expand the destinations
		// this is done by intent and rrn
		while (!ds.isEmpty()) {
			Collection<Destination> newdests = expand(ds);
			destinationRepository.markProcessed(ds);
			destinationRepository.store(newdests);
			ds.clear();
			ds.addAll(newdests);
		}
	}

	protected Collection<Destination> expand(Collection<Destination> ds) {
		Collection<Destination> results = newHashSet();
		for (Destination d : ds) {
			results.addAll(expand(d));
		}
		return results;
	}

	protected Collection<Destination> expand(Destination d) {
		// for each expander, expand the rrn
		Collection<Destination> results = newHashSet();
		Rrn rrn = d.rrn();
		
		// get resolved children, create destinations
		// update the chidren from the parent rrn
		
		Collection<Rrn> result = newHashSet();
		for (RrnExpander expander : expanders) {
			result.addAll( expander.expand(rrn, d.getIntent()) );
		}
		
		for (Rrn r : result) {
			results.add( new Destination(0, d.getMessageId(), rrn.rrn(), r.rrn(), d.getIntent(), ProcessingState.Unprocessed) );
		}
		return results;
	}
	
	
	

}
