package com.ohmmx.common.core.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ohmmx.common.core.jmx.ConfigurableBean;
import com.ohmmx.common.entity.ConfigParameter;
import com.ohmmx.common.redis.RedisLockerTools;

public abstract class AbstractLockedTask implements InitializingBean, ConfigurableBean {

	public static final int LOCK_SECONDS_DEFAULT = 1800;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public abstract String jobKey();

	@Autowired
	protected RedisLockerTools locker;

	protected boolean running;

	protected boolean pause;

	protected int lockSeconds() {
		return LOCK_SECONDS_DEFAULT;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		locker.releaseWithOwnerLike(jobKey());
	}

	@Override
	public void refreshConfig(Map<String, ConfigParameter> parameters) {
		ConfigParameter pause = parameters.get("bc-batch.pause");
		this.pause = pause == null ? false : pause.isBooleanValue();
	}

	public void execute() throws Exception {
		if (running || pause) {
			return;
		}
		try {
			running = true;
			locker.inLock(jobKey(), () -> {
				try {
					executeInternal();
				} catch (Exception e) {
					logger.error("Job Execulation Failed: ", e);
				}
			}, 60);

		} finally {
			running = false;
		}
	}

	public abstract void executeInternal() throws Exception;
}
