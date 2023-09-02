package com.ohmmx.common.core;

import com.ohmmx.common.entity.TaskQueue;

public interface TaskConsumer {

	TaskConsumeResult consume(TaskQueue task) throws Exception;

	TaskConsumeResult onException(TaskQueue task, Throwable exception);

	void onStateEnter(TaskQueue task);

	void onStateLeave(TaskQueue task, String nextState);

	void onTaskStart(TaskQueue task);

	void onTaskEnd(TaskQueue task, boolean succeed);

	void onRetry(TaskQueue queue, int interval);

}
