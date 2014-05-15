/**
 * RedisTrans.java
 * cn.vko.dao.redis.support
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.support;

import static cn.vko.core.common.util.CollectionUtil.*;
import static cn.vko.core.common.util.Util.*;

import java.util.List;

import redis.clients.jedis.JedisCommands;

/**
 * Redis线程事务控制
 * 
 * @author 彭文杰
 * @author 赵立伟
 * @Date 2013-1-30
 */
public class RedisTransController {
	/**
	 * 每个线程事务管理复本
	 */
	private static final ThreadLocal<List<RedisTransaction>> TRANS = new ThreadLocal<List<RedisTransaction>>();

	/**
	 * @return 当前线程的最后一个事务，如果没有事务，返回 null
	 */
	public static RedisTransaction get() {
		List<RedisTransaction> trans = TRANS.get();
		if (isEmpty(trans)) {
			return null;
		}
		return last(trans);
	}

	/**
	 * @return 当前线程是否存在事务
	 */
	public static boolean isNoTransaction() {
		return get() == null;
	}

	/**
	 * 开启事务
	 */
	public static void begin() {
		if (isNoTransaction()) {
			TRANS.set(list(new RedisTransaction()));
		}
	}

	/**
	 * 开启新事务
	 * <p>
	 * 请注意如果您自己开启了新事务，请自己Try catch finally进行关闭
	 */
	public static void beginNew() {
		if (isNoTransaction()) {
			TRANS.set(list(new RedisTransaction()));
			return;
		}
		TRANS.get().add(new RedisTransaction());
	}

	/**
	 * 关闭连接
	 */
	public static void close() {
		RedisTransaction trans = get();
		if (trans == null) {
			return;
		}
		trans.close();
		TRANS.get().remove(trans);
	}

	/**
	 * 异常时连接关闭
	 */
	public static void closeWithEx() {
		RedisTransaction trans = get();
		if (trans == null) {
			return;
		}
		trans.closeWithEx();
		TRANS.get().remove(trans);
	}

	/**
	 * 获取当前jedis链接
	 * 
	 * @param ds
	 *            数据源
	 * @return jedis链接
	 */
	public static JedisCommands getCurrentJedis(final IRedisDataSource ds) {
		begin();
		JedisCommands jedis = get().getRedis(ds);
		return jedis;
	}
}
