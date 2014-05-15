/**
 * RedisHashUtil.java
 * cn.vko.dao.redis.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.util;

import static cn.vko.core.redis.util.RedisUtil.*;
import cn.vko.core.redis.IRedisDao;

/**
 * Redis hash工具类
 * 
 * @author 庄君祥
 * @author 赵立伟
 * @Date 2013-4-17
 * @version 5.1.0
 */
public final class RedisHashUtil {
	private RedisHashUtil() {
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment 。<br/>
	 * 增量也可以为负数，相当于对给定域进行减法操作。<br/>
	 * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。<br/>
	 * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。<br/>
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
	 * 
	 * @param key
	 *            键值
	 * @param field
	 *            域
	 * @param value
	 *            增减量
	 * @return 哈希表 key 中域 field 的值
	 */
	public static long hincrBy(final IRedisDao dao, final String key,
			final String field, final long value) {
		checkDao(dao);
		if (!dao.exists(key)) {
			return -1;
		}
		return dao.hincrBy(key, field, value);
	}

	/**
	 * 每次加1
	 * <p>
	 * {@link #hincrBy(IRedisDao, String, String, long)}
	 */
	public static long hincrBy(final IRedisDao dao, final String key,
			final String field) {
		checkDao(dao);
		if (!dao.exists(key)) {
			return -1;
		}
		return dao.hincrBy(key, field, 1);
	}

	/**
	 * 每次减1
	 * <p>
	 * {@link #hincrBy(IRedisDao, String, String, long)}
	 */
	public static long hdecrBy(final IRedisDao dao, final String key,
			final String field) {
		return hincrBy(dao, key, field, -1);
	}
}
