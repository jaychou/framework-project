package com.liyang.webapps.myframe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;








import com.liyang.webapps.modules.config.Global;
import com.liyang.webapps.modules.quartz.Executor;
import com.liyang.webapps.modules.redis.RedisApi;
import com.liyang.webapps.modules.redis.RedisApiFactory;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })  
/*
 * 使用注册来完成对profile的激活,
 * 传入对应的profile名字即可,可以传入produce或者dev
 */
@ActiveProfiles("development")
public class AppTest {
  
    @Autowired
	private Executor executor;
    
   
    
    @Before
    public void setup() {
    }
	
    @Test
	public void testJob() {
		executor.execute();
	}
	
    
    
    
    @Test
    public void testRedis() {
    	RedisApi redisApi = RedisApiFactory.getDefaultRedisApi();
    	redisApi.set("username", "niehaiquan");
    	System.out.println(redisApi.get("username"));
    }

	

}
