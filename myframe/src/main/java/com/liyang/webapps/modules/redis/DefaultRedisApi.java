package com.liyang.webapps.modules.redis;

import java.util.ResourceBundle;

public class DefaultRedisApi extends AbstractRedisApi implements RedisApi{
	
	
	private static ResourceBundle bundle;
	
	static {
		 bundle = ResourceBundle.getBundle("redis"); 
	}
	public DefaultRedisApi() {
		String host = bundle.getString("redis.host");
		int port = Integer.parseInt(bundle.getString("redis.port"));
		String password = bundle.getString("redis.password");
		redisUtils = new RedisUtils(host, port, password);
	}

	
	

	
	
	

}
