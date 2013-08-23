package com.railinc.wembley.api.retry;

public interface RetryExecutor {
	void triedAndFailed(Retryable item, RetryPolicy policy);
}
