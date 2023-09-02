package com.ohmmx.common.redis;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

public class RedisLocker {

	private static final Long RELEASE_SUCCESS = Long.valueOf(1);

	private static final String releaseLuaScriptWithOwner = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
	private static RedisScript<Long> scriptReleaseWithOwner = new DefaultRedisScript<>(releaseLuaScriptWithOwner, Long.class);

	private static final String releaseLuaScriptWithOwnerPrefix = "if string.match(tostring(redis.call('get', KEYS[1])), ARGV[1]) then return redis.call('del', KEYS[1]) else return 0 end";
	private static RedisScript<Long> scriptReleaseWithOwnerLike = new DefaultRedisScript<>(releaseLuaScriptWithOwnerPrefix, Long.class);

	public static boolean getLock(RedisTemplate<String, String> template, String key, String owner, int seconds) {
		if (StringUtils.isBlank(owner)) {
			throw new IllegalArgumentException("lock owner must not be empty.");
		}
		return template.opsForValue().setIfAbsent(key, owner);
	}

	public static boolean releaseLockWithOwner(RedisTemplate<String, String> template, String key, String owner) {
		Object result = template.execute(scriptReleaseWithOwner, Collections.singletonList(key), owner);
		return RELEASE_SUCCESS.equals(result);
	}

	public static boolean releaseLockWithOwnerPrefix(RedisTemplate<String, String> template, String key, String owner) {
		Object result = template.execute(scriptReleaseWithOwnerLike, Collections.singletonList(key), "^" + owner);
		return RELEASE_SUCCESS.equals(result);
	}
}
