package com.github.trevershick.wembley.api;

import java.util.Collection;

import com.github.trevershick.wembley.domain.Destination;

public interface DestinationRepository {

	Collection<Destination> byMessageId(long messageId);

	void markProcessed(Collection<Destination> ds);

	void store(Collection<Destination> newdests);

}
