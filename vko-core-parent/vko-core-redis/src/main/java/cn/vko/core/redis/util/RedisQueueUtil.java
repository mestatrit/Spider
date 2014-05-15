/**
 * RedisQueueUtil.java
 * cn.vko.dao.redis.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.util;

import static cn.vko.core.common.util.CollectionUtil.*;
import static cn.vko.core.common.util.ExceptionUtil.*;
import static cn.vko.core.common.util.Util.*;
import static cn.vko.core.redis.support.RedisKeyPrefix.*;
import static cn.vko.core.redis.util.RedisUtil.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.MapUtil;
import cn.vko.core.redis.IRedisDao;

/**
 * redis队列处理工具类
 * 
 * @author 彭文杰
 * @author 庄君祥
 * @author 赵立伟
 * @Date 2013-2-5
 * @version 5.2.0
 */
public class RedisQueueUtil {
	/**
	 * 将数据push到list中
	 * 
	 * @param dao
	 *            dao对象
	 * @param type
	 *            类型
	 * @param values
	 *            数据
	 * @return 队列中元素个数
	 */
	public static long push(final IRedisDao dao, final String type,
			final Map<String, String> values) {
		if (isEmpty(values)) {
			return 0;
		}
		checkDao(dao);
		List<String> idList = list();
		for (Entry<String, String> value : values.entrySet()) {
			if (value == null) {
				continue;
			}
			String id = value.getKey();
			if (isEmpty(id)) {
				continue;
			}
			idList.add(id);
		}
		dao.lpush(key(QUEUE, type),
				CollectionUtil.collection2array(idList, String.class));
		dao.set(key(QM, type), values);
		return values.size();
	}

	/**
	 * 将数据push到list中
	 * 
	 * @param dao
	 *            dao对象
	 * @param type
	 *            类型
	 * @param values
	 *            数据
	 * @return 队列中元素个数
	 */
	public static long push(final IRedisDao dao, final String type,
			final String key, final String value) {
		return push(dao, type, MapUtil.map(key, value));
	}

	/**
	 * 弹出一个主键进行处理
	 * 
	 * @param dao
	 *            dao对象
	 * @param clazz
	 *            对象类别
	 * @return 弹出的主键
	 */
	public static String pop(final IRedisDao dao, final String type) {
		checkDao(dao);
		checkEmpty(type, "类型不能为空");
		return dao.rpop(key(QUEUE, type));
	}

	/**
	 * 弹出队尾的主键，并将对象id计入备份队列
	 * 
	 * @param dao
	 *            dao对象
	 * @param type
	 *            类别
	 * @return 队尾对应的主键
	 */
	public static String safePop(final IRedisDao dao, final String type) {
		checkDao(dao);
		checkEmpty(type, "类型不能为空");
		return dao.rpoplpush(key(QUEUE, type), key(QUEUE_BACK, type));
	}

	/**
	 * 弹出备份队列队尾的主键，并将对象id计入
	 * 
	 * @param dao
	 *            dao对象
	 * @param type
	 *            类别
	 * @param id
	 *            需要处理的id
	 * @return 队尾对应的主键
	 */
	public static void back2Error(final IRedisDao dao, final String type,
			final String id) {
		checkDao(dao);
		checkEmpty(type, "类型不能为空");
		checkEmpty(id, "主键不能为空");

		dao.lrem(key(QUEUE_BACK, type), 0, id);
		dao.lpush(key(QUEUE_ERROR, type), id);
	}

	/**
	 * 处理完成
	 * <p>
	 * 将主键从处理队列中移出，表示处理完毕
	 * 
	 * @param dao
	 *            dao对象
	 * @param type
	 *            类型
	 * @param id
	 *            主键
	 */
	public static void finish(final IRedisDao dao, final String type,
			final String id) {
		checkDao(dao);
		checkEmpty(type, "类型不能为空");
		checkEmpty(id, "对象不能为空");
		dao.lrem(key(QUEUE_BACK, type), 0, id);
		dao.del(key(QM, type, id));
	}

	/**
	 * 重做任务
	 * <p>
	 * 将任务从处理队列中移出，重新放入待处理队列
	 * 
	 * @param dao
	 *            dao对象
	 * @param entity
	 *            待重做的任务
	 */
	public static void redo(final IRedisDao dao, final String type,
			final String id) {
		checkDao(dao);
		checkEmpty(type, "类型不能为空");
		checkEmpty(id, "主键不能为空");

		dao.lrem(key(QUEUE_BACK, type), 0, id);
		dao.lpush(key(QUEUE, type), id);
	}
}
