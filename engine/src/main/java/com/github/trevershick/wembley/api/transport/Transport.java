package com.github.trevershick.wembley.api.transport;

import com.github.trevershick.wembley.api.destination.Destination;
import com.github.trevershick.wembley.api.retry.Retryable;

public interface Transport extends Retryable {
	boolean supports(Destination d);
}
