package cn.vko.business.spider.urlfilter.impl;

import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.MurmurHash;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import cn.vko.business.spider.urlfilter.IRequestFilter;
import cn.vko.core.common.util.MapUtil;

public class RedisBloomFilter implements IRequestFilter {
	private JedisPool pool;
	private static Map<Long, Long> KEY_MEM = MapUtil.map();
	private static final int HASH_KEY_NUM = 11;
	static {
		KEY_MEM.put(100000L, 187628L);
		KEY_MEM.put(1000000L, 1875128L);
		KEY_MEM.put(10000000L, 18750128L);
		KEY_MEM.put(100000000L, 187500128L);
	}
	private static Map<String, Long> UUID_MEM = MapUtil.map();

	public RedisBloomFilter(String host, int port) {
		pool = new JedisPool(new JedisPoolConfig(), host, port);
	}

	@Override
	public boolean canPush(Request request, Task task) {
		if (exist(task.getUUID(), request.getUrl())) {
			return false;
		}
		addOneUrl(task.getUUID(), request.getUrl());
		return true;
	}

	public void addOneUrl(String uuid, String url) {
		Jedis jedis = pool.getResource();
		try {
			Pipeline pipeline = jedis.pipelined();
			int[] offsets = getHashBuckets(url);
			for (int offset : offsets) {
				pipeline.setbit(uuid, offset, true);
			}
			pipeline.sync();
		} catch (JedisException e) {
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	@Override
	public void clear(String uuid) {
		Jedis jedis = pool.getResource();
		try {
			jedis.del(uuid);
		} finally {
			pool.returnResource(jedis);
		}
	}

	@Override
	public void addUrl(String uuid, List<String> url) {
		Jedis jedis = pool.getResource();
		try {
			Pipeline pipeline = jedis.pipelined();
			int num = 0;
			for (String one : url) {
				int[] offsets = getHashBuckets(one);
				for (int offset : offsets) {
					pipeline.setbit(uuid, offset, true);
				}
				num++;
				if (num == 100) {
					num = 0;
					pipeline.sync();
				}
			}
			if (num > 0) {
				pipeline.sync();
			}
		} catch (JedisException e) {
			pool.returnBrokenResource(jedis);
		} finally {
			pool.returnResource(jedis);
		}
	}

	@Override
	public boolean isFilted(String uuid) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.exists(uuid);
		} catch (JedisException e) {
			pool.returnBrokenResource(jedis);
			return true;
		} finally {
			pool.returnResource(jedis);
		}
	}

	/**
	 * url是否已存在
	 * 
	 * @param uuid
	 * @return
	 */
	public boolean exist(String uuid, String url) {
		Jedis jedis = pool.getResource();
		try {
			int[] offsets = getHashBuckets(url);
			for (int offset : offsets) {
				if (jedis.getbit(uuid, offset) == false)
					return false;
			}
			return true;
		} catch (JedisException e) {
			pool.returnBrokenResource(jedis);
			return true;
		} finally {
			pool.returnResource(jedis);
		}
	}

	private int[] getHashBuckets(String key) {
		long max = 100000000L;
		if (UUID_MEM.containsKey(key)) {
			max = UUID_MEM.get(key);
		}
		return getHashBuckets(key.getBytes(), HASH_KEY_NUM, max);
	}

	public static int[] getHashBuckets(byte[] b, int hashCount, long max) {
		int[] result = new int[hashCount];
		int hash1 = MurmurHash.hash(b, 0, b.length, 0);
		int hash2 = MurmurHash.hash(b, 0, b.length, hash1);
		for (int i = 0; i < hashCount; ++i) {
			result[i] = (int) (Math.abs((hash1 + i * hash2) % max));
		}
		return result;
	}

	public static void initMem(String uuid, long keynum) {

	}
}
