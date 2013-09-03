package com.railinc.wembley.api;

import java.util.Collection;

import com.railinc.wembley.domain.Destination;

public interface DestinationRepository {

	Collection<Destination> byMessageId(long messageId);

	void markProcessed(Collection<Destination> ds);

	void store(Collection<Destination> newdests);

}
