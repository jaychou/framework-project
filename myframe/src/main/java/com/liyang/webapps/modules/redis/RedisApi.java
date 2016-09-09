package com.liyang.webapps.modules.redis;

import java.util.Map;
import java.util.Set;

public interface RedisApi {
	
	public String get(String key);
	
	public String set(String key, String value);
	
	public void hset(String key, String field, String value);
	
	public String hget(String key, String field);
	
	/*public void expire(String key, int seconds);*/

	public Map<String,String> hgetall(String key);
	
	public void rename(String oldkey, String newkey);

	public void del(String key);
	
	public String lpop(String key);

	public String rpop(String key);

	public void lpush(String key, String value);

	public void rpush(String key, String value);

	public long llen(String key);
	
	  public Set<String> keys(String pattern);
	  
	  public Set<String> hkeys(String key);

}
