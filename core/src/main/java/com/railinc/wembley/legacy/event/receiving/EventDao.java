package com.railinc.wembley.legacy.event.receiving;

import java.util.List;

import com.railinc.wembley.api.event.Event;

public interface EventDao {

	List<Event> getEventsForRetry();
}
