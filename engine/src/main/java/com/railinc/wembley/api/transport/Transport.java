package com.railinc.wembley.api.transport;

import com.railinc.wembley.api.destination.Destination;
import com.railinc.wembley.api.retry.Retryable;

public interface Transport extends Retryable {
	boolean supports(Destination d);
}
