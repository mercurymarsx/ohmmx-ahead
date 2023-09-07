package com.ohmmx.common.core.task;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import com.ohmmx.common.core.TaskConsumeResult;
import com.ohmmx.common.core.TaskConsumer;
import com.ohmmx.common.core.service.TaskQueueService;
import com.ohmmx.common.entity.TaskQueue;
import com.ohmmx.common.mapper.TaskQueueRepository;
import com.ohmmx.common.redis.RedisLockerTools;
import com.ohmmx.common.utils.DateUtils;

public class TaskRunner implements InitializingBean, ApplicationContextAware {

	public static final String LOCK_KEY_PREFIX = "locker:runner:";

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected TaskQueueRepository taskQueueRepository;

	private ApplicationContext applicationContext;

	@Autowired
	protected TaskQueueService taskQueueService;

	@Autowired
	protected RedisLockerTools lockerTools;

	protected String namespace;

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	protected String lockerPrefix;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(namespace, "property 'namespace' must be set.");
		lockerPrefix = LOCK_KEY_PREFIX + namespace + ":";
		lockerTools.clearPrefix(lockerPrefix);
	}

	public void consume(TaskQueue task, Map<TaskQueue.ID, TaskQueue> tasks) {
		TaskQueue.ID id = task.getId();
		try {
			String key = lockerPrefix + id.toString();
			lockerTools.inLock(key, () -> {
				TaskQueue task2 = taskQueueRepository.findById(id).orElse(null);
				if (task2 == null) {
					return;
				}
				TaskConsumer consumer;
				try {
					consumer = applicationContext.getBean(id.getType(), TaskConsumer.class);
				} catch (BeansException e) {
					task2.setNotBefore(DateUtils.nextHour(new Date()));
					taskQueueRepository.save(task2);
					logger.error("Unsupported Task.type: " + id.getType(), e);
					return;
				}
				runTask(consumer, task2);
			}, 3600);
		} finally {
			tasks.remove(id);
		}

	}

	private void runTask(TaskConsumer consumer, TaskQueue task) {
		String taskId = task.getId().toString();
		logger.info("Task start: [{}], state=[{}], retries=[{}], ttl=[{}]", taskId, task.getState(), task.getRetries(), task.getTtl());
		while (true) {
			TaskConsumeResult result = TaskConsumeResult.retry();
			String prevState = task.getState();
			if (task.getTtl() <= 0) {
				break;
			}
			try {
				if (task.getLastRun() == null) {
					consumer.onTaskStart(task);
				}
				task.setLastRun(new Date());
				consumer.onStateEnter(task);
				result = consumer.consume(task);
			} catch (Throwable e) {
				try {
					result = consumer.onException(task, e);
				} catch (RuntimeException re) {
					logger.error("Task fatal: [" + taskId + "], bad exception handler: orig=[" + e.getMessage() + "], now=[" + re.getMessage() + "], orig detail: ", e);
				}
				logger.error("Task error: [" + taskId + "], msg=[" + e.getMessage() + "], detail: ", e);
			}
			if (result == null) {
				logger.error("Task failed: [{}], bad task result null, orig state: [{}]", taskId, prevState);
				result = TaskConsumeResult.hang();
			}
			if (result.isDone()) {
				taskQueueService.done(task, result, prevState);
				consumer.onStateLeave(task, prevState);
				consumer.onTaskEnd(task, result.isSucceed());
				logger.info("Task done : [{}], state=[{}->{}]", taskId, prevState, result.getState());
			} else if (result.isRetry(prevState)) {
				taskQueueService.retry(task, result.getInterval());
				consumer.onRetry(task, result.getInterval());
				logger.info("Task retry: [{}], state=[{}], delay=[{}], reties=[{}], at=[{}]", taskId, prevState, result.getInterval(), task.getRetries(), DateUtils.HH_mm_ss(task.getNotBefore()));
			} else {
				taskQueueService.next(task, result, prevState);
				consumer.onStateLeave(task, prevState);
				logger.info("Task next : [{}], state=[{}->{}], delay=[{}], ttl=[{}]", taskId, prevState, result.getState(), result.getInterval(), task.getTtl());
				if (result.getInterval() == 0) {
					continue;
				}
			}
			return;
		}
	}
}
