package com.github.trevershick.wembley.api.retry;

public interface RetryExecutor {
	void triedAndFailed(Retryable item, RetryPolicy policy);
}
