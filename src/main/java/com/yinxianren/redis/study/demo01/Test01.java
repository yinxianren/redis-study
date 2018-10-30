package com.yinxianren.redis.study.demo01;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;

public class Test01 {
	/**
	 * junit 4 单元测试插件
	 * 直接测试方法，不用再编写主方法
	 * 再需要运行的方法上面添加@Test
	 * 方法要求：
	 * 必须是public
	 * 必须有没返回值
	 * 没有参数
	 * 
	 *当前项目中不要有Test类
	 * 
	 * 
	 */
	private Jedis jedis=null;

	@Before
	public void init() {
		jedis=new Jedis("192.168.0.102",6379);
		jedis.auth("123456");//认证密码
	}


	@Test
	public void testtestKey() throws InterruptedException {

		System.out.println("清空数据："+jedis.flushDB()); //清空数据：OK
		System.out.println("判断某个键是否存在："+jedis.exists("username")); //判断某个键是否存在：false
		System.out.println("新增<'username','xmr'>的键值对："+jedis.set("username", "xmr"));//新增<'username','xmr'>的键值对：OK
		System.out.println("判断username是否存在："+jedis.exists("username"));//判断username是否存在：true
		System.out.println("新增<'password','password'>的键值对："+jedis.set("password", "123"));	//新增<'password','password'>的键值对：OK

		System.out.print("系统中所有的键如下：");
		Set<String> keys = jedis.keys("*");
		System.out.println(keys);//系统中所有的键如下：[password, username]
		System.out.println("删除键password:"+jedis.del("password"));//删除键password:1
		System.out.println("判断键password是否存在："+jedis.exists("password"));//判断键password是否存在：false
		System.out.println("设置键username的过期时间为5s:"+jedis.expire("username", 8));//设置键username的过期时间为5s:1
		TimeUnit.SECONDS.sleep(2);
		System.out.println("查看键username的剩余生存时间："+jedis.ttl("username"));//查看键username的剩余生存时间：6
		System.out.println("移除键username的生存时间："+jedis.persist("username"));//移除键username的生存时间：1
		System.out.println("查看键username的剩余生存时间："+jedis.ttl("username"));//查看键username的剩余生存时间：-1
		System.out.println("查看键username所存储的值的类型："+jedis.type("username"));//查看键username所存储的值的类型：string



	}


	@Test
	public void testString() throws InterruptedException {
		jedis.flushDB();
		System.out.println("===========增加数据===========");
		System.out.println("添加key1："+jedis.set("key1", "value1"));
		System.out.println("添加key2："+jedis.set("key2", "value2"));
		System.out.println("添加key3："+jedis.set("key3", "value3"));
		System.out.println("删除键key2:" + jedis.del("key2"));
		System.out.println("获取键key2:" + jedis.get("key2"));//获取键key2:null
		System.out.println("修改key1:" + jedis.set("key1", "1"));
		System.out.println("获取key1的值：" + jedis.get("key1"));//获取key1的值：1
		System.out.println("在key3后面加入值：" + jedis.append("key3", "End"));//在key3后面加入值：9
		System.out.println("key3的值：" + jedis.get("key3"));//3的值：value3End
		System.out.println("增加多个键值对：" + jedis.mset("key01", "value01", "key02", "value02", "key03", "value03"));//增加多个键值对：OK
		System.out.println("获取多个键值对：" + jedis.mget("key01", "key02", "key03"));//获取多个键值对：[value01, value02, value03]
		System.out.println("获取多个键值对：" + jedis.mget("key01", "key02", "key03", "key04"));//获取多个键值对：[value01, value02, value03, null]
		System.out.println("删除多个键值对：" + jedis.del(new String[]{"key01", "key02"}));//删除多个键值对：2
		System.out.println("获取多个键值对：" + jedis.mget("key01", "key02", "key03"));//获取多个键值对：[null, null, value03]

		jedis.flushDB();
		System.out.println("===========新增键值对,防止覆盖原先值==============");
		System.out.println(jedis.setnx("key1", "value1"));//1
		System.out.println(jedis.setnx("key2", "value2"));//1
		System.out.println(jedis.setnx("key2", "value2-new"));//0
		System.out.println(jedis.get("key1"));//value1
		System.out.println(jedis.get("key2"));//value2

		System.out.println("===========新增键值对并设置有效时间=============");
		System.out.println(jedis.setex("key3", 2, "value3"));//OK
		System.out.println(jedis.get("key3"));//value3
		TimeUnit.SECONDS.sleep(3);
		System.out.println(jedis.get("key3"));//null

		System.out.println("===========获取原值，更新为新值==========");//GETSET is an atomic set this value and return the old value command.
		System.out.println(jedis.getSet("key2", "key2GetSet"));//value2
		System.out.println(jedis.get("key2"));//key2GetSet

		System.out.println("获得key2的值的字串：" + jedis.getrange("key2", 2, 4));//获得key2的值的字串：y2G
	}



	/** * redis操作Hash */
	@Test
	public void testHash() {    
		jedis.flushDB();    
		Map<String, String> map = new HashMap<>();    
		map.put("key1", "value1");    
		map.put("key2", "value2");    
		map.put("key3", "value3");    
		map.put("key4", "value4");    
		jedis.hmset("hash", map);    
		jedis.hset("hash", "key5", "value5");  
		//散列hash的所有键值对为：{key1=value1, key2=value2, key5=value5, key3=value3, key4=value4}
		System.out.println("散列hash的所有键值对为：" + jedis.hgetAll("hash"));//return Map<String,String>  
		//		散列hash的所有键为：[key1, key2, key5, key3, key4]
		System.out.println("散列hash的所有键为：" + jedis.hkeys("hash"));//return Set<String> 
		//		散列hash的所有值为：[value3, value1, value2, value4, value5]
		System.out.println("散列hash的所有值为：" + jedis.hvals("hash"));//return List<String>    
		//		将key6保存的值加上一个整数，如果key6不存在则添加key6：6
		System.out.println("将key6保存的值加上一个整数，如果key6不存在则添加key6：" + jedis.hincrBy("hash", "key6", 6));    
		//散列hash的所有键值对为：{key1=value1, key2=value2, key5=value5, key6=6, key3=value3, key4=value4}
		System.out.println("散列hash的所有键值对为：" + jedis.hgetAll("hash")); 
		//		将key6保存的值加上一个整数，如果key6不存在则添加key6：9
		System.out.println("将key6保存的值加上一个整数，如果key6不存在则添加key6：" + jedis.hincrBy("hash", "key6", 3));  
		//		散列hash的所有键值对为：{key1=value1, key2=value2, key5=value5, key6=9, key3=value3, key4=value4}
		System.out.println("散列hash的所有键值对为：" + jedis.hgetAll("hash")); 
		//		删除一个或者多个键值对：1
		System.out.println("删除一个或者多个键值对：" + jedis.hdel("hash", "key2")); 
		//		散列hash的所有键值对为：{key1=value1, key5=value5, key6=9, key3=value3, key4=value4}
		System.out.println("散列hash的所有键值对为：" + jedis.hgetAll("hash")); 
		//		散列hash中键值对的个数：5
		System.out.println("散列hash中键值对的个数：" + jedis.hlen("hash"));  
		//		判断hash中是否存在key2：false
		System.out.println("判断hash中是否存在key2：" + jedis.hexists("hash", "key2"));   
		//		判断hash中是否存在key3：true
		System.out.println("判断hash中是否存在key3：" + jedis.hexists("hash", "key3")); 
		//		获取hash中的值：[value3]
		System.out.println("获取hash中的值：" + jedis.hmget("hash", "key3"));  
		//		获取hash中的值：[value3, value4]
		System.out.println("获取hash中的值：" + jedis.hmget("hash", "key3", "key4"));
	}


	@Test
	public void testList() {
		jedis.flushDB();
		System.out.println("===========添加一个list===========");
		jedis.lpush("lists", "ArrayList", "Vector", "Stack", "HashMap", "WeakHashMap", "LinkedHashMap");
		jedis.lpush("lists", "HashSet");
		jedis.lpush("lists", "TreeSet");
		jedis.lpush("lists", "TreeMap");
		//		lists的内容：[TreeMap, TreeSet, HashSet, LinkedHashMap, WeakHashMap, HashMap, Stack, Vector, ArrayList]
		System.out.println("lists的内容：" + jedis.lrange("lists", 0, -1));
		// -1代表倒数第一个元素，-2代表倒数第二个元素
		//		lists区间0-3的元素：[TreeMap, TreeSet, HashSet, LinkedHashMap]
		System.out.println("lists区间0-3的元素：" +jedis.lrange("lists", 0, 3));
		System.out.println("===============================");
		// // 删除列表指定的值
		// ，第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈
		System.out.println("删除指定元素个数：" + jedis.lrem("lists", 2, "HashMap"));//删除指定元素个数：1
		//lists的内容：[TreeMap, TreeSet, HashSet, LinkedHashMap, WeakHashMap, Stack, Vector, ArrayList]
		System.out.println("lists的内容：" + jedis.lrange("lists", 0, -1));
		//删除下表0-3区间之外的元素：OK
		System.out.println("删除下表0-3区间之外的元素：" + jedis.ltrim("lists", 0, 3));
		//lists的内容：[TreeMap, TreeSet, HashSet, LinkedHashMap]
		System.out.println("lists的内容：" + jedis.lrange("lists", 0, -1));
		//lists列表出栈（左端）：TreeMap
		System.out.println("lists列表出栈（左端）：" + jedis.lpop("lists"));
		//lists的内容：[TreeSet, HashSet, LinkedHashMap]
		System.out.println("lists的内容：" + jedis.lrange("lists", 0, -1));
		//		lists添加元素，从列表右端，与lpush相对应：4
		System.out.println("lists添加元素，从列表右端，与lpush相对应：" + jedis.rpush("lists", "EnumMap"));
		//lists的内容：[TreeSet, HashSet, LinkedHashMap, EnumMap]
		System.out.println("lists的内容：" + jedis.lrange("lists", 0, -1));
		//lists列表出栈（右端）：EnumMap
		System.out.println("lists列表出栈（右端）：" + jedis.rpop("lists"));
		//lists的内容：[TreeSet, HashSet, LinkedHashMap]
		System.out.println("lists的内容：" + jedis.lrange("lists", 0, -1));
		//修改lists指定下标1的内容：OK
		System.out.println("修改lists指定下标1的内容：" + jedis.lset("lists", 1, "LinkedArrayList"));
		//lists的内容：[TreeSet, LinkedArrayList, LinkedHashMap]
		System.out.println("lists的内容：" + jedis.lrange("lists", 0, -1));
		System.out.println("===============================");
		//lists的长度：3
		System.out.println("lists的长度：" + jedis.llen("lists"));
		//获取lists下标为2的元素：LinkedHashMap
		System.out.println("获取lists下标为2的元素：" + jedis.lindex("lists", 2));
		System.out.println("===============================");
		jedis.lpush("sortedList", "3", "6", "2", "0", "7", "4");
		//sortedList排序前：[4, 7, 0, 2, 6, 3]
		System.out.println("sortedList排序前：" + jedis.lrange("sortedList", 0, -1));
		System.out.println(jedis.sort("sortedList"));//[0, 2, 3, 4, 6, 7]
		System.out.println("sortedList排序后：" + jedis.lrange("sortedList", 0, -1));//sortedList排序后：[4, 7, 0, 2, 6, 3]
	}
	
	
	@Test
	public void testSet() {
		jedis.flushDB();
		System.out.println("============向集合中添加元素============");
		System.out.println(jedis.sadd("eleSet", "e1", "e2", "e4", "e3", "e0", "e8", "e7", "e5"));//8
		System.out.println(jedis.sadd("eleSet", "e6"));//1
		System.out.println("eleSet的所有元素为：" + jedis.smembers("eleSet"));//eleSet的所有元素为：[e0, e5, e1, e8, e4, e3, e2, e7, e6]
		System.out.println("删除一个元素e0：" + jedis.srem("eleSet", "e0"));//1
		System.out.println("eleSet的所有元素为：" + jedis.smembers("eleSet"));//[e5, e1, e8, e4, e3, e2, e7, e6]
		System.out.println("删除两个元素e7和e6：" + jedis.srem("eleSet", "e7", "e6"));//2
		System.out.println("eleSet的所有元素为：" + jedis.smembers("eleSet"));//[e4, e3, e2, e1, e8, e5]
		System.out.println("随机的移除集合中的一个元素：" + jedis.spop("eleSet"));//e1
		System.out.println("eleSet的所有元素为：" + jedis.smembers("eleSet"));//[e8, e4, e2, e5, e3]
		//Return the set cardinality (number of elements). If the key does not exist 0 is returned, likefor empty sets.
		System.out.println("eleSet中包含元素的个数：" + jedis.scard("eleSet"));//5
		//Return 1 if member is a member of the set stored at key, otherwise 0 is returned. 
		System.out.println("e1是否在eleSet中：" + jedis.sismember("eleSet", "e1"));//false
		System.out.println("=================================");
		System.out.println(jedis.sadd("eleSet1", "e1", "e2", "e4", "e3", "e0", "e8", "e7", "e5"));//8
		System.out.println(jedis.sadd("eleSet2", "e1", "e2", "e4", "e3", "e0", "e8"));//6
		System.out.println("将eleSet1中删除e1并存入eleSet3中：" + jedis.smove("eleSet1", "eleSet3", "e1"));//1
		System.out.println("eleSet1中的元素：" + jedis.smembers("eleSet1"));//[e0, e5, e8, e4, e3, e2, e7]
		System.out.println("eleSet3中的元素：" + jedis.smembers("eleSet3"));//[e1]
		System.out.println("============集合运算=================");
		System.out.println("eleSet1中的元素：" + jedis.smembers("eleSet1"));//[e0, e5, e8, e4, e3, e2, e7]
		System.out.println("eleSet2中的元素：" + jedis.smembers("eleSet2"));//[e2, e1, e0, e8, e4, e3]
		System.out.println("eleSet1和eleSet2的交集:" + jedis.sinter("eleSet1", "eleSet2"));//[e2, e0, e8, e4, e3]
		System.out.println("eleSet1和eleSet2的并集:" + jedis.sunion("eleSet1", "eleSet2"));//[e4, e3, e2, e7, e0, e1, e8, e5]
		System.out.println("eleSet1和eleSet2的差集:" + jedis.sdiff("eleSet1", "eleSet2"));//[e5, e7]  

	}
	/**
	 * 有序集合
	 */
	@Test
	public void testSortedSet() {
		jedis.flushDB();
		Map<String, Double> map = new HashMap<>();
		map.put("key2", 1.5);
		map.put("key3", 1.6);
		map.put("key4", 1.9);
		System.out.println(jedis.zadd("zset", 3, "key1"));//1
		System.out.println(jedis.zadd("zset", map));//2
		System.out.println("zset中的所有元素：" + jedis.zrangeByScore("zset", 0, 100));//[key2, key3, key4, key1]
		System.out.println("zset中key2的分值：" + jedis.zscore("zset", "key2"));//1.5
		System.out.println("zset中key2的排名：" + jedis.zrank("zset", "key2"));//0
		System.out.println("删除zset中的元素key3：" + jedis.zrem("zset", "key3"));//1
		System.out.println("zset中的所有元素：" + jedis.zrange("zset", 0, -1));//[key2, key4, key1]
		System.out.println("zset中元素的个数：" + jedis.zcard("zset"));//3
		System.out.println("zset中分值在1-4之间的元素的个数：" + jedis.zcount("zset", 1, 4));//3
		System.out.println("key2的分值加上5：" + jedis.zincrby("zset", 5, "key2"));//6.5
		System.out.println("key3的分值加上4：" + jedis.zincrby("zset", 4, "key3"));//4
		/*
		 *  If member does not already exist in the sorted set it is added with increment as score 
		 *  (that is, like if the previous score wasvirtually zero).
		 */
		System.out.println("key30的分值加上30：" + jedis.zincrby("zset", 30, "key30"));//key30的分值加上30：30.0
		System.out.println("zset中的所有元素：" + jedis.zrange("zset", 0, -1));//[key4, key1, key3, key2]

	}
	
	@Test
	public void testSort() {
		jedis.flushDB();
		jedis.lpush("collections", "ArrayList", "Vector", "Stack", "HashMap", "WeakHashMap", "LinkedHashMap");
		//[LinkedHashMap, WeakHashMap, HashMap, Stack, Vector, ArrayList]
		System.out.println("collections的内容：" + jedis.lrange("collections", 0, -1));
		SortingParams sortingParameters = new SortingParams();
		//[ArrayList, HashMap, LinkedHashMap, Stack, Vector, WeakHashMap]
		System.out.println(jedis.sort("collections", sortingParameters.alpha()));
		//[ArrayList, HashMap, LinkedHashMap]
		System.out.println(jedis.sort("collections", sortingParameters.limit(0,3)));
		System.out.println("===============================");
		jedis.lpush("sortedList", "3", "6", "2", "0", "7", "4");
		System.out.println("sortedList排序前：" + jedis.lrange("sortedList", 0, -1));//[4, 7, 0, 2, 6, 3]
		System.out.println("升序：" + jedis.sort("sortedList", sortingParameters.asc()));//[0, 2, 3]
		System.out.println("降序：" + jedis.sort("sortedList", sortingParameters.desc()));//[7, 6, 4]
		
		
		System.out.println("===============================");
		jedis.lpush("userlist", "33");
		jedis.lpush("userlist", "22");
		jedis.lpush("userlist", "55");
		jedis.lpush("userlist", "11");
		jedis.hset("user:66", "name", "66");
		jedis.hset("user:55", "name", "55");
		jedis.hset("user:33", "name", "33");
		jedis.hset("user:22", "name", "79");
		jedis.hset("user:11", "name", "24");
		jedis.hset("user:11", "add", "beijing");
		jedis.hset("user:22", "add", "shanghai");
		jedis.hset("user:33", "add", "guangzhou");
		jedis.hset("user:55", "add", "chongqing");
		jedis.hset("user:66", "add", "xi'an");
		sortingParameters = new SortingParams();
		System.out.println(sortingParameters.get("user:*->name"));//redis.clients.jedis.SortingParams@1963006a
		System.out.println(sortingParameters.get("user:*->add"));//redis.clients.jedis.SortingParams@1963006a
		System.out.println(jedis.sort("userlist", sortingParameters));
		//[24, beijing, 79, shanghai, 33, guangzhou, 55, chongqing]

	}
	

}
