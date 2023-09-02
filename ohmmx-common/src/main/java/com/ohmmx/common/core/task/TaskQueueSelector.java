package com.ohmmx.common.core.task;

import java.util.List;

import com.ohmmx.common.entity.TaskQueue;

public interface TaskQueueSelector {

	List<TaskQueue> select();
}
