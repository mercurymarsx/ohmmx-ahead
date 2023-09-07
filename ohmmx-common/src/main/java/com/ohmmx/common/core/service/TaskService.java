package com.ohmmx.common.core.service;

import java.util.Date;
import java.util.function.Consumer;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ohmmx.common.entity.TaskQueue;

public interface TaskService {

	Consumer<TaskQueue> DEFAULT_ACCEPTOR = new Consumer<TaskQueue>() {

		@Override
		public void accept(TaskQueue task) {
			task.setState("NEW");
		}
	};

	@Transactional(propagation = Propagation.REQUIRED)
	TaskQueue createTask(String type, String reference);

	@Transactional(propagation = Propagation.REQUIRED)
	TaskQueue createTask(String type, String reference, Consumer<TaskQueue> setter);

	@Transactional(propagation = Propagation.REQUIRED)
	TaskQueue createTask(String type, String reference, Date notBefore);

	@Transactional(propagation = Propagation.REQUIRED)
	TaskQueue createTask(String type, String reference, String state);

	@Transactional(propagation = Propagation.REQUIRED)
	TaskQueue createTask(String type, String reference, String state, Date notBefore);
}
