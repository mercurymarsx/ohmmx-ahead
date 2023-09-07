package com.ohmmx.common.core.service.impl;

import java.util.Date;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ohmmx.common.core.service.TaskService;
import com.ohmmx.common.entity.TaskQueue;
import com.ohmmx.common.mapper.TaskQueueRepository;

@Service
public class TaskServiceImpl implements TaskService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected TaskQueueRepository taskQueueRepository;

	@Override
	public TaskQueue createTask(String type, String reference) {
		return createTask(type, reference, "NEW", new Date());
	}

	@Override
	public TaskQueue createTask(String type, String reference, Consumer<TaskQueue> setter) {
		TaskQueue.ID id = new TaskQueue.ID(type, reference);
		TaskQueue task = taskQueueRepository.findById(id).orElse(null);
		if (task == null) {
			task = new TaskQueue(id);
		}
		setter.accept(task);
		taskQueueRepository.save(task);
		return task;
	}

	@Override
	public TaskQueue createTask(String type, String reference, Date notBefore) {
		return createTask(type, reference, "NEW", notBefore);
	}

	@Override
	public TaskQueue createTask(String type, String reference, String state) {
		return createTask(type, reference, state, new Date());
	}

	@Override
	public TaskQueue createTask(String type, String reference, String state, Date notBefore) {
		return createTask(type, reference, (task) -> {
			task.setState(state);
			task.setNotBefore(notBefore);
		});
	}
}
