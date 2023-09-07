package com.ohmmx.common.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ohmmx.common.core.service.TaskService;
import com.ohmmx.common.entity.TaskQueue;
import com.ohmmx.common.mapper.TaskQueueRepository;

public abstract class AbstractTaskConsumer implements TaskConsumer, BeanNameAware {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected TaskQueueRepository taskQueueRepository;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	protected TaskService taskService;

	protected String taskType;

	@Override
	public TaskConsumeResult consume(TaskQueue task) throws Exception {
		TaskConsumeResult result = doInternal(task);
		return result;
	}

	@Override
	public void setBeanName(String name) {
		this.taskType = name;
	}

	public abstract TaskConsumeResult doInternal(TaskQueue task) throws Exception;

	@Override
	public TaskConsumeResult onException(TaskQueue task, Throwable exception) {
		return TaskConsumeResult.retry();
	}

	@Override
	public void onStateEnter(TaskQueue task) {
	}

	@Override
	public void onStateLeave(TaskQueue task, String prevState) {
	}

	@Override
	public void onTaskStart(TaskQueue task) {
	}

	@Override
	public void onTaskEnd(TaskQueue task, boolean succeed) {
	}

	@Override
	public void onRetry(TaskQueue queue, int interval) {
	}

	protected boolean hasPrevious(String type, String ref) {
		return hasPrevious(type, ref, false);
	}

	protected boolean hasTask(String type) {
		return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TSK_QUEUE q WHERE q.TYPE = ?", Number.class, type).intValue() > 0;
	}

	protected boolean hasTask(String type, String ref) {
		return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TSK_QUEUE q WHERE q.TYPE = ? AND q.REFERENCE = ?", Number.class, type, ref).intValue() > 0;
	}

	protected boolean hasPrevious(String type, String ref, boolean include) {
		if (include) {
			return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TSK_QUEUE q WHERE q.TYPE = ? AND q.REFERENCE <= ?", Number.class, type, ref).intValue() > 0;
		} else {
			return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TSK_QUEUE q WHERE q.TYPE = ? AND q.REFERENCE < ?", Number.class, type, ref).intValue() > 0;
		}
	}

	protected boolean notifyTask(String type, String ref) {
		return notifyTask(type, ref, false);
	}

	protected boolean notifyTask(String type, String ref, boolean extendNotAfter) {
		if (extendNotAfter) {
			return jdbcTemplate.update("UPDATE TSK_QUEUE q SET q.NOT_BEFORE = NOW(), q.NOT_AFTER = DATE_ADD(NOW(), INTERVAL 1 DAY) WHERE q.TYPE = ? and q.REFERENCE = ?", type, ref) > 0;
		} else {
			return jdbcTemplate.update("UPDATE TSK_QUEUE q SET q.NOT_BEFORE = NOW() WHERE q.TYPE = ? and q.REFERENCE = ?", type, ref) > 0;
		}
	}

	/** @see TaskConsumeResult#retry() */
	protected TaskConsumeResult retry() {
		return TaskConsumeResult.retry();
	}

	/** @see TaskConsumeResult#retry(int) */
	protected TaskConsumeResult retry(int interval) {
		return TaskConsumeResult.retry(interval);
	}

	/** @see TaskConsumeResult#next(String) */
	protected TaskConsumeResult next(String state) {
		return TaskConsumeResult.next(state);
	}

	/** @see TaskConsumeResult#next(String, int) */
	protected TaskConsumeResult next(String state, int interval) {
		return TaskConsumeResult.next(state, interval);
	}

	/** @see TaskConsumeResult#hang() */
	protected TaskConsumeResult hang() {
		return TaskConsumeResult.hang();
	}

	/** @see TaskConsumeResult#hang(String) */
	protected TaskConsumeResult hang(String state) {
		return TaskConsumeResult.hang(state);
	}

	/** @see TaskConsumeResult#succeed() */
	protected TaskConsumeResult succeed() {
		return TaskConsumeResult.succeed();
	}

	/** @see TaskConsumeResult#succeed(String) */
	protected TaskConsumeResult succeed(String state) {
		return TaskConsumeResult.succeed(state);
	}
}
