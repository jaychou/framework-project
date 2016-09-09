package com.liyang.webapps.modules.redis;

import org.apache.log4j.Logger;

public class RedisApiFactory {
	private static Logger logger = Logger.getLogger(RedisApiFactory.class);
	private static RedisApi defaultRedisApi = new DefaultRedisApi(); 
	
	public static RedisApi getDefaultRedisApi() {
		RedisApi redisApi = defaultRedisApi;
		try {
			if(redisApi==null) {
				redisApi = new DefaultRedisApi(); 
			}
		} catch (Exception e) {
			logger.error(e,e);
		}
		
		return redisApi;
	}

}
