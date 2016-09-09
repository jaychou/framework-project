package com.liyang.webapps.modules.redis;

import java.util.Map;
import java.util.Set;


public  abstract class AbstractRedisApi implements RedisApi{
	protected RedisUtils redisUtils;
	
	@Override
	public String get(String key) {
		return redisUtils.get(key);
	}

	@Override
	public String set(String key, String value) {
		return redisUtils.set(key, value);
		
	}

	@Override
	public void hset(String key, String field, String value) {
		redisUtils.hset(key, field, value);
		
	}

	@Override
	public String hget(String key, String field) {
		return redisUtils.hget(key, field);
	}



	@Override
	public Map<String, String> hgetall(String key) {
		return redisUtils.hgetall(key);
	}



	@Override
	public void rename(String oldkey, String newkey) {
		redisUtils.rename(oldkey, newkey);
		
	}



	@Override
	public void del(String key) {
		redisUtils.del(key);
		
	}
	
	@Override
	public String lpop(String key) {
		return redisUtils.lpop(key);
	}
	@Override
	public String rpop(String key) {
		return redisUtils.rpop(key);
	}
	@Override
	public void lpush(String key, String strs) {
		redisUtils.lpush(key, strs);
	}
	
	@Override
	public void rpush(String key, String value) {
		redisUtils.rpush(key, value);
	}
	@Override
	public long llen(String key) {
		return redisUtils.llen(key);
	}

	@Override
	public Set<String> keys(String pattern) {
		return redisUtils.keys(pattern);
	}

	@Override
	public Set<String> hkeys(String key) {
		return redisUtils.hkeys(key);
	}

}
