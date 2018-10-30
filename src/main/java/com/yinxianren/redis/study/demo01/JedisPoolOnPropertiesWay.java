package com.yinxianren.redis.study.demo01;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolOnPropertiesWay {

	private Jedis jedis=null;
	private JedisPool jedisPool=null;
	
	
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
		//(poolConfig,String host, int port,int timeout,final String password,final int database)
	     
		jedisPool=new JedisPool(jedisPoolConfig,
				bundle.getString("redis.ip"),
				Integer.parseInt(bundle.getString("redis.port")),
				5000,
				bundle.getString("redis.password"),
				0);
		// 从池中获取一个Jedis对象
		jedis=jedisPool.getResource();
	}
	

	
	@Test
	public void test() {
		String keys = "name";
		// 删数据 
		jedis.del(keys);
		// 存数据 
		jedis.set(keys, "snowolf");
		// 取数据 
		String value = jedis.get(keys); 
		System.out.println(value);
		// 释放对象池
		// jedisPool.returnResource(jedis);

	}
	
	
	
	
	public void add(String sn) {
		jedis.sadd("snSet", sn);
		jedisPool.destroy();
	}

	public void remove(String sn) {
		jedis.srem("snSet", sn);
	}

	public boolean isExist(String sn) {
		Set<String> snSet = jedis.smembers("snSet");
		return snSet.contains(sn);
	}
	
	
	
	
	
	
	
	
}
