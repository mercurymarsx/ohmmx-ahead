package com.ohmmx.common.core.task;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ohmmx.common.core.jmx.ConfigurableBean;
import com.ohmmx.common.entity.ConfigParameter;
import com.ohmmx.common.entity.TaskQueue;
import com.ohmmx.common.mapper.TaskQueueRepository;
import com.ohmmx.common.redis.RedisLockerTools;

public abstract class AbstractTaskQueueSelector implements TaskQueueSelector, ConfigurableBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected TaskQueueRepository taskQueueRepository;

	@Autowired
	protected RedisLockerTools redisLockerTools;

	protected String taskPrefix;

	public void setTaskPrefix(String taskPrefix) {
		this.taskPrefix = taskPrefix;
	}

	protected String configFetchSize = "task.scheduler.fetchSize";

	public void setConfigFetchSize(String configFetchSize) {
		this.configFetchSize = configFetchSize;
	}

	protected int fetchSize = 10;

	@Override
	public void refreshConfig(Map<String, ConfigParameter> parameters) {
		fetchSize = parameters.get(configFetchSize).getIntValue();
	}

	@Override
	public List<TaskQueue> select() {
		Pageable pageable = PageRequest.of(0, fetchSize);
		List<TaskQueue> tasks = taskQueueRepository.findByType(taskPrefix + '%', redisLockerTools.getLockOwner(), pageable);
		return tasks;
	}
}
