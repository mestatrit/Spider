/**
 * RedisLiveUtil.java
 * cn.vko.dao.redis.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.util;

import static cn.vko.core.redis.util.RedisUtil.*;

import java.util.Set;

import cn.vko.core.redis.IRedisDao;
import cn.vko.core.redis.support.RedisKeyPrefix;

/**
 * redis当前信息的工具类<br/>
 * 例如当前用户列表
 * 
 * @author 彭文杰
 * @Date 2013-11-25
 */
public final class RedisLiveUtil {

	private RedisLiveUtil() {
	}

	/**
	 * 将数据放到sortedset
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @param score
	 *            权重
	 * @param member
	 *            成员值
	 * @return 放入的个数
	 */
	public static long add(final IRedisDao dao, final String key,
			final double score, final String member) {
		checkDao(dao);
		return dao.zadd(key(RedisKeyPrefix.LIVE, key), score, member);
	}

	/**
	 * 判断数据是否存在
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @param member
	 *            成员值
	 * @return 数据是否存在
	 */
	public static boolean isLive(final IRedisDao dao, final String key,
			final String member) {
		checkDao(dao);
		Double d = dao.zscore(key(RedisKeyPrefix.LIVE, key), member);
		return d != null;
	}

	/**
	 * 移除成员
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @param members
	 *            成员值
	 * @return 被移除的成员数量
	 */
	public static long rm(final IRedisDao dao, final String key,
			final String... members) {
		checkDao(dao);
		return dao.zrem(key(RedisKeyPrefix.LIVE, key), members);
	}

	/**
	 * 列出过期成员
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @param score
	 *            值
	 * @return 过期成员列表
	 */
	public static Set<String> outDate(final IRedisDao dao, final String key,
			final double min, final double max) {
		checkDao(dao);
		return dao.zrangeByScore(key(RedisKeyPrefix.LIVE, key), min, max);
	}

	/**
	 * 将数据clear
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @return 删除key的个数
	 */
	public static long clear(final IRedisDao dao, final String key) {
		checkDao(dao);
		return dao.del(key(RedisKeyPrefix.LIVE, key));
	}

	/**
	 * 计数
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @return 包含元素数
	 */
	public static long count(final IRedisDao dao, final String key) {
		checkDao(dao);
		return dao.zcard(key(RedisKeyPrefix.LIVE, key));
	}
}
