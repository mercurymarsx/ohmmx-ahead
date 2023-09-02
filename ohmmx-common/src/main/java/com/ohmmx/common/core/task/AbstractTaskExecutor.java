package com.ohmmx.common.core.task;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;

import com.ohmmx.common.core.jmx.ConfigurableBean;
import com.ohmmx.common.entity.ConfigParameter;
import com.ohmmx.common.entity.TaskQueue;

public abstract class AbstractTaskExecutor implements TaskExecutor, InitializingBean, DisposableBean, ConfigurableBean {

	protected static ThreadGroup ROOT_GROUP = new ThreadGroup("c360.tasks.root");

	protected static ThreadGroup PEEKER_GROUP = new ThreadGroup(ROOT_GROUP, "peeker");

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String name;

	private int poolSize;

	protected Thread poller;

	private Deque<TaskQueue> deque = new ConcurrentLinkedDeque<>();

	private Map<TaskQueue.ID, TaskQueue> tasks = new ConcurrentHashMap<>();

	private LinkedBlockingQueue<Runnable> queue;

	private ThreadPoolExecutor executorService;

	private ThreadGroup threadGroup;

	private boolean running = true;

	private boolean pause = false;

	@Autowired
	protected TaskRunner runner;

	public void setName(String name) {
		this.name = name;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(name, "name can't be empty.");
		threadGroup = new ThreadGroup(ROOT_GROUP, name);
		CustomizableThreadFactory threadFactory = new CustomizableThreadFactory(name + "-");
		threadFactory.setThreadGroup(threadGroup);

		queue = new LinkedBlockingQueue<>(1000);

		executorService = new ThreadPoolExecutor(poolSize, poolSize, 60L, TimeUnit.SECONDS, queue, threadFactory);

		poller = new Thread(PEEKER_GROUP, this::start, "peeker-" + name);
		poller.start();
	}

	@Override
	public void destroy() throws Exception {
		running = false;
		executorService.shutdown();
	}

	@Override
	public void refreshConfig(Map<String, ConfigParameter> parameters) {
		ConfigParameter pause = parameters.get("bc-batch.pause");
		this.pause = pause == null ? false : pause.isBooleanValue();
	}

	@Override
	public void addFirst(TaskQueue e) {
		if (tasks.containsKey(e.getId())) {
			return;
		}
		tasks.put(e.getId(), e);
		deque.addFirst(e);
	}

	@Override
	public void addLast(TaskQueue e) {
		if (tasks.containsKey(e.getId())) {
			return;
		}
		tasks.put(e.getId(), e);
		deque.addLast(e);
	}

	@Override
	public void clear() {
		deque.clear();
		tasks.clear();
	}

	@Override
	public int size() {
		return deque.size();
	}

	@Override
	public int depth() {
		return queue.size();
	}

	@Override
	public int poolSize() {
		return executorService.getPoolSize();
	}

	@Override
	public int activeCount() {
		return executorService.getActiveCount();
	}

	public void start() {
		while (running) {
			while (pause || queue.size() > 800) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
			TaskQueue task = deque.poll();
			if (task == null) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
				continue;
			}
			logger.info("TaskExecutor [{}]: schedule task: [{}]/[{}]", name, task.getId(), queue.size());
			executorService.submit(() -> {
				runner.consume(task, tasks);
			});
		}
	}
}
