package com.ykhd.office.component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis操作类
 */
@Component
public final class RedisService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * set K_V
	 */
	public void setKV(String key, String value, Long expire_seconds) {
		if (expire_seconds != null && expire_seconds > 0)
			stringRedisTemplate.opsForValue().set(key, value, expire_seconds.longValue(), TimeUnit.SECONDS);
		else
			stringRedisTemplate.opsForValue().set(key, value);
	}
	
	/**
	 * get K_V
	 */
	public String getValue(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}
	
	/**
	 * set Set
	 */
	public void setSet(Long expire_seconds, String key, String... value) {
		stringRedisTemplate.opsForSet().add(key, value);
		if (expire_seconds != null && expire_seconds > 0)
			setExpire(key, expire_seconds.longValue());
	}
	
	/**
	 * if in Set
	 */
	public boolean inSet(String key, String value) {
		return stringRedisTemplate.opsForSet().isMember(key, value);
	}
	
	/**
	 * delete key
	 */
	public boolean delKV(String key) {
		return stringRedisTemplate.delete(key);
	}
	
	/**
	 * expire key
	 */
	public void setExpire(String key, long expire_seconds) {
		if (expire_seconds > 0)
			stringRedisTemplate.expire(key, expire_seconds, TimeUnit.SECONDS);
	}
	
	/**
	 * exist key
	 */
	public boolean existKey(String key) {
		return stringRedisTemplate.hasKey(key);
	}
	
	/**
	 * set Map
	 */
	public void setMap(String key, Map<Object, Object> map) {
		stringRedisTemplate.opsForHash().putAll(key, map);
	}
	
	/**
	 * get Map
	 */
	public Map<Object, Object> getMap(String key) {
		return stringRedisTemplate.opsForHash().entries(key);
	}
}
