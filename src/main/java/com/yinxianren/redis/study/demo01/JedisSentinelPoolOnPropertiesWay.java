package com.yinxianren.redis.study.demo01;

import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.Before;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinelPoolOnPropertiesWay {


	private Jedis jedis=null;
	private JedisSentinelPool jedisSentinelPool=null;


	@Before
	public void initJedisPool() {
		//Gets a resource bundle using the specified base name, the default locale,
		//and the caller's class loader. Calling this method is equivalent to calling
		ResourceBundle bundle=ResourceBundle.getBundle("jedisPool");
		if(null==bundle) {
			throw new MissingResourceException("配置文件为找到！",
					"com.yinxianren.redis.study.demo01.JedisPoolOnPropertiesWay",
					"jedisPool.properties");
		}

		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(Integer.parseInt(bundle.getString("redis.pool.maxTotal")));
		jedisPoolConfig.setMaxIdle(Integer.parseInt(bundle.getString("redis.pool.maxIdle")));
		jedisPoolConfig.setMaxWaitMillis(Long.parseLong(bundle.getString("redis.pool.maxWait")));
		jedisPoolConfig.setTestOnBorrow(Boolean.parseBoolean(bundle.getString("redis.pool.testOnBorrow")));
		jedisPoolConfig.setTestOnReturn(Boolean.parseBoolean(bundle.getString("redis.pool.testOnReturn")));


		//监听器列表   
		Set<String> sentinels = new HashSet<>();

		//监听器1 
		//sentinels.add(new HostAndPort("172.24.4.183", 26379).toString());
		//监听器2 
		//sentinels.add(new HostAndPort("172.24.4.184", 26379).toString());
		//实际使用的时候在properties里配置即可：redis.sentinel.hostandports=172.24.4.183:26379, 172.24.4.184:26379   
		//然后使用 bundle.getString("redis.sentinel.hostandports");获取地址
		
		
		
		// mastername是服务器上的master的名字，在master服务器的sentinel.conf中配置 
		String masterName = bundle.getString("redis.sentinel.masterName");
		sentinels.add(bundle.getString("redis.sentinel.hostandports"));
		 //初始化连接池 
		jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig);
		// 从池中获取一个Jedis对象 
		jedis = jedisSentinelPool.getResource();

	}


}
