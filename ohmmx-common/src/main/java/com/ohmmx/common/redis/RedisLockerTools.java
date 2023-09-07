package com.ohmmx.common.redis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockerTools implements InitializingBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Value("${redis.locker.owner:}")
	private String lockOwner;

	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}

	public String getLockOwner() {
		return lockOwner;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isEmpty(lockOwner)) {
			try {
				lockOwner = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
			}
			if (StringUtils.isEmpty(lockOwner) || "localhost".equals(lockOwner)) {
				lockOwner = UUID.randomUUID().toString();
				logger.warn("Using Random UUID for hostname: [{}]", lockOwner);
			}
		}
		logger.info("AbstractLockedTask: lock key value: [{}]", lockOwner);
	}

	public void clearPrefix(String prefix) {
		logger.info("Clear lock prefix: [{}] for [{}]", prefix, lockOwner);
		Set<String> keys = redisTemplate.keys(prefix + "*");
		for (String key : keys) {
			RedisLocker.releaseLockWithOwnerPrefix(redisTemplate, key, lockOwner);
		}
	}

	public void clear(String key) {
		logger.info("Clear lock key: [{}] for [{}]", key, lockOwner);
		RedisLocker.releaseLockWithOwnerPrefix(redisTemplate, key, lockOwner);
	}

	public void waitLock(String key, Runnable func, int retry, long waitMillis, int expiredSeconds) {
		String owner = lockOwner + ":" + UUID.randomUUID().toString();
		try {
			for (int i = 0; i < retry; i++) {
				if (RedisLocker.getLock(redisTemplate, key, owner, expiredSeconds * 1000)) {
					func.run();
					return;
				} else {
					try {
						Thread.sleep(waitMillis);
					} catch (InterruptedException e) {
					}
				}
			}
			throw new RuntimeException("Redis Locker Timeout: " + key);
		} finally {
			RedisLocker.releaseLockWithOwner(redisTemplate, key, owner);
		}
	}

	public boolean inLock(String key, Runnable func, int expiredSeconds) {
		String owner = lockOwner + ":" + UUID.randomUUID().toString();
		try {
			if (RedisLocker.getLock(redisTemplate, key, owner, expiredSeconds * 1000)) {
				func.run();
				return true;
			}
			return false;
		} finally {
			RedisLocker.releaseLockWithOwner(redisTemplate, key, owner);
		}
	}

	public boolean acquire(String key) {
		return RedisLocker.getLock(redisTemplate, key, lockOwner, 60 * 1000);
	}

	public boolean acquire(String key, String owner) {
		return RedisLocker.getLock(redisTemplate, key, owner, 60 * 1000);
	}

	public boolean releaseWithOwner(String key) {
		return RedisLocker.releaseLockWithOwner(redisTemplate, key, lockOwner);
	}

	public boolean releaseWithOwnerLike(String key) {
		return RedisLocker.releaseLockWithOwnerPrefix(redisTemplate, key, lockOwner);
	}

	public boolean release(String key, String owner) {
		return RedisLocker.releaseLockWithOwner(redisTemplate, key, owner);
	}
}
