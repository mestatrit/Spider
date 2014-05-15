/**
 * RedisSetUtil.java
 * cn.vko.dao.redis.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.util;

import static cn.vko.core.redis.support.RedisKeyPrefix.*;
import static cn.vko.core.redis.util.RedisUtil.*;

import java.util.Set;

import cn.vko.core.redis.IRedisDao;

/**
 * 处理redis set工具类
 * 
 * @author 彭文杰
 * @Date 2013-4-11
 * @version 5.0.0
 */
public class RedisSetUtil {
	/**
	 * 将数据放到set
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @param objects
	 *            对象
	 * @return 放入的个数
	 */
	public static long add(final IRedisDao dao, final String key,
			final String... members) {
		checkDao(dao);
		return dao.sadd(getSetKey(key), members);
	}

	/**
	 * 获取带set前缀的key
	 * 
	 * @param key
	 *            键
	 * @return 带set前缀的key
	 */
	private static String getSetKey(final String key) {
		return key(SET, key);
	}

	/**
	 * 判断是否是成员
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @param member
	 *            成员
	 * @return 是否是成员
	 */
	public static boolean isMember(final IRedisDao dao, final String key,
			final String member) {
		checkDao(dao);
		return dao.sismember(getSetKey(key), member);
	}

	/**
	 * 返回所有成员
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @return 所有成员信息
	 */
	public static Set<String> members(final IRedisDao dao, final String key) {
		checkDao(dao);
		return dao.smembers(getSetKey(key));
	}

	/**
	 * 求set的条数
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @return 条数
	 */
	public static long card(final IRedisDao dao, final String key) {
		checkDao(dao);
		return dao.scard(getSetKey(key));
	}

	/**
	 * 移出成员
	 * 
	 * @param dao
	 *            redisDao
	 * @param key
	 *            键
	 * @param members
	 *            待移出的成员
	 * @return 移出的个数
	 */
	public static long rem(final IRedisDao dao, final String key,
			final String... members) {
		checkDao(dao);
		return dao.srem(getSetKey(key), members);
	}
}
