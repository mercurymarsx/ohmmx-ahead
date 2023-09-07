package com.ohmmx.batch.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ohmmx.common.core.AbstractTaskConsumer;
import com.ohmmx.common.core.service.TaskService;

public abstract class AbstractTask extends AbstractTaskConsumer {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected TaskService taskService;
}
