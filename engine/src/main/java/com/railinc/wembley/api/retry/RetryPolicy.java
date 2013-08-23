package com.railinc.wembley.api.retry;

import java.util.concurrent.TimeUnit;

public interface RetryPolicy {
	int maximumRetryAttempts();
	TimeUnit retryAfterUnit();
	long retryAfter(int retryAttempt);
}
