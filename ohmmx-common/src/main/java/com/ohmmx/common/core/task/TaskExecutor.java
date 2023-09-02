package com.ohmmx.common.core.task;

import com.ohmmx.common.entity.TaskQueue;

public interface TaskExecutor {

	void addFirst(TaskQueue e);

	void addLast(TaskQueue e);

	void clear();

	int size();

	int depth();

	int poolSize();

	int activeCount();
}
