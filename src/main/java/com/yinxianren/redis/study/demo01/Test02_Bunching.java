package com.yinxianren.redis.study.demo01;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class Test02_Bunching {

	private JedisCluster jc=null;
	
	@Before
	public void init() {
		Set<HostAndPort> set=new HashSet<>();
		set.add(new HostAndPort("192.168.0.102", 6379));
		set.add(new HostAndPort("192.168.0.102", 6380));
		set.add(new HostAndPort("192.168.0.108", 6379));
		set.add(new HostAndPort("192.168.0.108", 6380));
		set.add(new HostAndPort("192.168.0.109", 6379));
		set.add(new HostAndPort("192.168.0.109", 6380));
		
		jc=new JedisCluster(set);
		
	}
	
	@Test
	public void test() {
		String result=jc.get("a");
		System.out.println("返回结果："+result);
	}
	
	
}
