/**
 * RedisTopUtil.java
 * cn.vko.dao.redis.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.util;

import static cn.vko.core.common.util.ArrayUtil.*;
import static cn.vko.core.common.util.CollectionUtil.*;
import static cn.vko.core.common.util.Util.*;
import static cn.vko.core.redis.support.RedisKeyPrefix.*;
import static cn.vko.core.redis.util.RedisUtil.*;

import java.util.List;

import cn.vko.core.redis.IRedisDao;

/**
 * 最新的消息redis工具类
 * 
 * @author 宋星明
 * @author 赵立伟
 * @author 庄君祥
 * @Date 2013-1-31
 * @version 5.1.0
 */
public class RedisLatestUtil {
	/**
	 * 全局变量最大条数500条
	 */
	private static final int MAX = 500;

	/**
	 * 根据传递过来的 OBject数组，插入到redis中 最多可插入500条
	 * 
	 * @param key
	 *            键值
	 * @param values
	 *            键值
	 * @param dao
	 *            dao对象
	 * @param number
	 *            插入的条数
	 * @return >0 成功，<0 失败
	 */
	public static long add(final IRedisDao dao, final int number,
			final String key, final Object... values) {
		checkDao(dao);
		String realKey = key(TOP, key);
		if (isEmpty(values)) {
			return 0;
		}
		for (Object value : values) {
			dao.lrem(realKey, 0, String.valueOf(value));
		}
		dao.lpush(realKey, array2StringArray(values));
		dao.ltrim(realKey, 0, number - 1);
		return values.length;
	}

	/**
	 * 根据传递过来的 OBject数组，插入到redis中 最多可插入500条
	 * 
	 * @param key
	 *            键值
	 * @param values
	 *            键值
	 * @param dao
	 *            dao对象
	 * @return >0 成功，<0 失败
	 */
	public static long addMax(final IRedisDao dao, final String key,
			final Object... values) {
		return add(dao, MAX, key, values);
	}

	/**
	 * 根据传递的集合，插入到redis中 最多可插入500条
	 * 
	 * @param key
	 *            键值
	 * @param ids
	 * @param dao
	 *            dao对象
	 * @return >0 成功，<0 失败
	 */
	public static long addMax(final IRedisDao dao, final String key,
			final List<?> ids) {
		checkDao(dao);
		return add(dao, MAX, key, ids);
	}

	/**
	 * 根据传递的集合，插入到redis中 最多可插入500条
	 * 
	 * @param key
	 *            键值
	 * @param ids
	 * @param dao
	 *            dao对象
	 * @param number
	 *            插入的条数
	 * @return >0 成功，<0 失败
	 */
	public static long add(final IRedisDao dao, final int number,
			final String key, final List<?> ids) {
		checkDao(dao);
		String realKey = key(TOP, key);
		if (isEmpty(ids)) {
			return 0;
		}
		dao.lpush(realKey, collection2array(ids, String.class));
		dao.ltrim(realKey, 0, number - 1);
		return ids.size();
	}

	/**
	 * 根据key 查询出redis中存的TOP最新的数据
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @return List泛型数组
	 */
	public static List<String> query(final IRedisDao dao, final String key) {
		checkDao(dao);
		return dao.lrange(key(TOP, key), 0, -1);
	}

	/**
	 * 根据key 查询出redis中存的TOP最新的数据分页显示
	 * 
	 * @param key
	 *            键值
	 * @param offset
	 *            开始
	 * @param count
	 *            个数
	 * @param dao
	 *            dao对象
	 * @return List泛型数组
	 */
	public static List<String> query(final IRedisDao dao, final String key,
			final int offset, final int count) {
		checkDao(dao);
		return dao.lrange(key(TOP, key), offset, offset + count - 1);
	}

	/**
	 * 清空列表数据
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 */
	public static void clear(final IRedisDao dao, final String key) {
		checkDao(dao);
		dao.ltrim(key(TOP, key), 2, 1);
	}

	/**
	 * 列表数据长度
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 */
	public static long len(final IRedisDao dao, final String key) {
		checkDao(dao);
		return dao.llen(key(TOP, key));
	}

	/**
	 * 将成员移除队列
	 * 
	 * @param dao
	 * @param key
	 *            键值
	 * @param values
	 *            移除的成员集合
	 * @return 移除的个数
	 */
	public static long lrem(final IRedisDao dao, final String key,
			final Object... values) {
		checkDao(dao);
		String realKey = key(TOP, key);
		if (isEmpty(values)) {
			return 0;
		}
		long count = 0;
		for (Object value : values) {
			count += dao.lrem(realKey, 0, String.valueOf(value));
		}
		return count;
	}
}
