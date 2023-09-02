package com.ohmmx.common.core.service.impl;

import java.util.Date;
import java.util.List;

import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ohmmx.common.core.TaskConsumeResult;
import com.ohmmx.common.core.service.TaskQueueService;
import com.ohmmx.common.entity.TaskQueue;
import com.ohmmx.common.mapper.TaskQueueRepository;
import com.ohmmx.common.utils.DateUtils;

@Service
public class TaskQueueServiceImpl implements TaskQueueService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected TaskQueueRepository taskQueueRepository;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	private int[] retryFrequencies = DEFAULT_FREQ_RETRIES;

	public void setRetryFrequencies(int[] retryFrequencies) {
		this.retryFrequencies = retryFrequencies;
	}

	public int retryFrequency(int retries) {
		int index = retries >= retryFrequencies.length ? retryFrequencies.length - 1 : retries;
		return 60 * retryFrequencies[index];
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void createHistory(TaskConsumeResult result, TaskQueue task) {
		taskQueueRepository.flush();
		taskQueueRepository.delete(task);
	}

	@Override
	public List<TaskQueue> list(int limit) {
		Pageable pageable = PageRequest.of(0, limit);
		List<TaskQueue> tasks = taskQueueRepository.findRunner(pageable);
		logger.info("TaskQueueConsumer schedule tasks count: [{}]", tasks.size());
		return tasks;
	}

	@Override
	public void retry(TaskQueue task) {
		int retries = task.getRetries() + 1;
		task.setLastRun(new Date());
		task.setRetries(retries);
		task.setNotBefore(DateUtils.next(Period.seconds(retryFrequency(retries))));
		taskQueueRepository.save(task);
	}

	@Override
	public void retry(TaskQueue task, int interval) {
		int retries = task.getRetries() + 1;
		task.setLastRun(new Date());
		task.setRetries(retries);
		int delay = interval == 0 ? retryFrequency(retries) : interval;
		task.setNotBefore(DateUtils.next(Period.seconds(delay)));

		int ttl = jdbcTemplate.queryForObject("select TTL from TSK_QUEUE where TYPE = ? and REFERENCE = ?", Number.class, task.getId().getType(), task.getId().getReference()).intValue();
		task.setTtl(ttl);
		taskQueueRepository.save(task);
	}

	@Override
	public void done(TaskQueue task, TaskConsumeResult result, String prevState) {
		internalNext(task, result, prevState);
		createHistory(result, task);
	}

	protected void internalNext(TaskQueue task, TaskConsumeResult result, String prevState) {
		int delay = result.getInterval();
		Date now = new Date();
		long cost = task.getLastRun() == null ? 0 : now.getTime() - task.getLastRun().getTime();
		task.setState(result.getState());
		task.setStatePrevious(prevState);
		if (task.getStateHistory() == null) {
			task.setStateHistory("");
		}
		task.setStateHistory(task.getStateHistory() + prevState + ":" + task.getRetries() + ":" + DateUtils.yyyyMMddHHmmss() + ":" + cost + ",");
		task.setRetries(0);
		task.setLastRun(now);
		task.setNotBefore(DateUtils.next(Period.seconds(delay)));
		int ttl = jdbcTemplate.queryForObject("SELECT TTL FROM TSK_QUEUE WHERE TYPE = ? AND REFERENCE = ?", Number.class, task.getId().getType(), task.getId().getReference()).intValue();
		task.setTtl(ttl - 1);
		taskQueueRepository.save(task);
	}

	@Override
	public void next(TaskQueue task, TaskConsumeResult result, String prevState) {
		internalNext(task, result, prevState);
	}
}
