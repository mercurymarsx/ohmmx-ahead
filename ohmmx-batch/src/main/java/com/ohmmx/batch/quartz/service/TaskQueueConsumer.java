package com.ohmmx.batch.quartz.service;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ohmmx.common.core.service.AbstractLockedTask;
import com.ohmmx.common.core.task.TaskExecutor;
import com.ohmmx.common.core.task.TaskQueueSelector;
import com.ohmmx.common.entity.TaskQueue;

public class TaskQueueConsumer extends AbstractLockedTask implements InitializingBean, DisposableBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String LOCK_KEY_PREFIX = "locker:queue:consumer:";
	public static final String LOCK_KEY_NAME = "main";

	private String lockerKeyPrefix = LOCK_KEY_PREFIX;

	private String lockerKeyName = LOCK_KEY_NAME;

	private String lockerKey = lockerKeyPrefix + lockerKeyName;

	private Map<String, TaskExecutor> executors;

	private TaskExecutor defaultExecutor;

	@Autowired
	private TaskQueueSelector selector;

	public void setExecutors(Map<String, TaskExecutor> executors) {
		this.executors = executors;
	}

	public void setDefaultExecutor(TaskExecutor defaultExecutor) {
		this.defaultExecutor = defaultExecutor;
	}

	public void setSelector(TaskQueueSelector selector) {
		this.selector = selector;
	}

	public void setLockerKeyPrefix(String prefix) {
		this.lockerKeyPrefix = prefix;
		this.lockerKey = this.lockerKeyPrefix + this.lockerKeyName;
	}

	public void setLockerKeyName(String lockerKeyName) {
		this.lockerKeyName = lockerKeyName;
		this.lockerKey = this.lockerKeyPrefix + this.lockerKeyName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		locker.clearPrefix(this.lockerKey);
	}

	@Override
	public String jobKey() {
		return lockerKey;
	}

	@Override
	public void executeInternal() throws Exception {
		executeInternal(null);
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		List<TaskQueue> tasks = selector.select();
		logger.info("TaskQueueConsumer schedule tasks count: [{}]", tasks.size());

		for (TaskQueue task : tasks) {
			consume(task);
		}
	}

	protected void consume(TaskQueue task) {
		String key = task.getId().getType();
		String state = task.getState();

		// select executor by type+state
		TaskExecutor executor = executors.get(key + ":" + state);
		if (executor == null) {
			// fallback: select by type
			executor = executors.get(key);
		}
		if (executor != null) {
			executor.addLast(task);
		} else {
			defaultExecutor.addLast(task);
		}
	}

	@Override
	public void destroy() throws Exception {
		locker.clearPrefix(this.lockerKey);
	}
}
