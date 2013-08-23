package com.railinc.r2dq.task.notify;

import org.springframework.batch.item.database.HibernateCursorItemReader;

import com.railinc.r2dq.domain.tasks.Task;

public class UnnotifiedTaskReader extends HibernateCursorItemReader<Task> {

	@Override
	public void afterPropertiesSet() throws Exception {
		setUseStatelessSession(false);
		setQueryString("from Task where notificationSent is null");
		super.afterPropertiesSet();
	}
}
