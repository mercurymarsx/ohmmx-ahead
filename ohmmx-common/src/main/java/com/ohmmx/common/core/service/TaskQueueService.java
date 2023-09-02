package com.ohmmx.common.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ohmmx.common.core.TaskConsumeResult;
import com.ohmmx.common.entity.TaskQueue;

public interface TaskQueueService {

	int[] DEFAULT_FREQ_RETRIES = new int[] { 0, 1, 1, 1, 5, 10, 30, 60, 120 };

	List<TaskQueue> list(int limit);

	@Transactional(propagation = Propagation.REQUIRED)
	void retry(TaskQueue task);

	@Transactional(propagation = Propagation.REQUIRED)
	void retry(TaskQueue task, int interval);

	@Transactional(propagation = Propagation.REQUIRED)
	void done(TaskQueue task, TaskConsumeResult result, String prevState);

	@Transactional(propagation = Propagation.REQUIRED)
	void next(TaskQueue task, TaskConsumeResult result, String prevState);
}
