/**
 * RedisRankUtil.java
 * cn.vko.dao.redis.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.util;

import static cn.vko.core.redis.support.RedisKeyPrefix.*;
import static cn.vko.core.redis.util.RedisUtil.*;

import java.util.Set;

import cn.vko.core.redis.IRedisDao;

/**
 * 最新排行 redis工具类
 * 
 * @author 宋星明
 * @author 庄君祥
 * @Date 2013-2-1
 * @version 5.0.2
 */
public final class RedisTopUtil {
	/**
	 * member元素及其 score值加入到有序集 key 当中。<br/>
	 * 如果某个 member已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该
	 * member 在正确的位置上。<br/>
	 * score 值可以是整数值或双精度浮点数。<br/>
	 * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。<br/>
	 * 当 key 存在但不是有序集类型时，返回一个错误。
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @param score
	 *            权重，分值等
	 * @param member
	 *            成员
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
	 */
	public static long zadd(final IRedisDao dao, final String key,
			final double score, final String member) {
		checkDao(dao);
		return dao.zadd(key(RANK, key), score, member);
	}

	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * <p>
	 * 如果不传参数，则不移除任何成员
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @param members
	 *            一个或多个成员
	 * @return 被成功移除的成员的数量，不包括被忽略的成员
	 */
	public static long zrem(final IRedisDao dao, final String key,
			final String... members) {
		checkDao(dao);
		return dao.zrem(key(RANK, key), members);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名
	 * <p>
	 * 按正常顺序，比如：第1返回1，第2返回2
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @param member
	 * @return 返回有序集 key 中成员 member 的排名
	 */
	public static long zrank(final IRedisDao dao, final String key,
			final String member) {
		checkDao(dao);
		return zcard(dao, key) - dao.zrank(key(RANK, key), member);
	}

	/**
	 * 返回有序集 key 的基数(集合无数的个数)
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @return 当 key 存在且是有序集类型时，返回有序集的基数。<br/>
	 *         当 key 不存在时，返回 0 。
	 */
	public static long zcard(final IRedisDao dao, final String key) {
		checkDao(dao);
		return dao.zcard(key(RANK, key));
	}

	/**
	 * 返回有序集 key 中，成员 member 的 score 值。<br/>
	 * 如果 member 元素不是有序集 key 的成员，或 key 不存在，返回-1 。
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @param member
	 *            成员
	 * @return member 成员的 score 值
	 */
	public static double zscore(final IRedisDao dao, final String key,
			final String member) {
		checkDao(dao);
		Double score = dao.zscore(key(RANK, key), member);
		return score != null ? score : -1L;
	}

	/**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。<br/>
	 * 可以通过传递一个负数值 increment ，让 score 减去相应的值，比如 ZINCRBY key -5 member ，就是让
	 * member 的 score 值减去 5 。<br/>
	 * 当 key 不存在，或 member 不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key
	 * increment member 。<br/>
	 * 当 key 不是有序集类型时，返回一个错误。<br/>
	 * score 值可以是整数值或双精度浮点数。
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @param score
	 *            权重，分值等
	 * @param member
	 *            成员
	 * @return 成员的新 score 值
	 */
	public static double zincrby(final IRedisDao dao, final String key,
			final double score, final String member) {
		return dao.zincrby(key(RANK, key), score, member);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序,即和正常的排行榜一样
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @param start
	 *            开始下标
	 * @param end
	 *            结束下标
	 * @return 如果 member 是有序集 key 的成员，返回 member 的排名。<br/>
	 *         如果 member 不是有序集 key 的成员，返回 nil 。
	 */
	public static Set<String> zrange(final IRedisDao dao, final String key,
			final long start, final long end) {
		checkDao(dao);
		return dao.zrevrange(key(RANK, key), start, end);
	}

	/**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。<br/>
	 * 排名以 0 为底，也就是说， score 值最大的成员排名为 0
	 * 
	 * @param dao
	 *            dao对象
	 * @param key
	 *            键值
	 * @param end
	 *            结束下标
	 * @return 如果 member 是有序集 key 的成员，返回 member 的排名。<br/>
	 *         如果 member 不是有序集 key 的成员，返回 nil 。
	 */
	public static Set<String> zrange(final IRedisDao dao, final String key,
			final long end) {
		return zrange(dao, key, 0, end);
	}
}
