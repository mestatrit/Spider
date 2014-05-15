/**
 * RecordUtil.java
 * cn.vko.util
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.util;

import static org.nutz.castor.Castors.me;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;

import org.nutz.castor.Castors;
import org.nutz.dao.entity.Record;

import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.common.util.MapUtil;

/**
 * 用来处理record工具类
 * <p>
 * 参见Record的getXxx()方法
 * 
 * @author 彭文杰
 * @author 庄君祥
 * @author 赵立伟
 * @Date 2013-3-27
 * @version 0.0.0
 * @see Record
 */
public class RecordUtil {
	/**
	 * 获取Long型数据
	 * <p>
	 * 如果取不到值返回0
	 * 
	 * @param r
	 *            记录
	 * @param name
	 *            数据键值
	 * @return 数据
	 */
	public static long getLong(final Record r, final String name) {
		if (r == null) {
			return -1L;
		}
		if (name == null) {
			return -1L;
		}
		Object value = r.get(name);
		if (value == null) {
			return -1L;
		}
		return ConvertUtil.obj2long(value);
	}

	/**
	 * 获取int型数据
	 * <p>
	 * 如果取不到值返回0
	 * 
	 * @param r
	 *            记录
	 * @param name
	 *            数据键值
	 * @return 数据
	 */
	public static int getInt(final Record r, final String name) {
		if (r == null) {
			return -1;
		}
		if (name == null) {
			return -1;
		}
		Object value = r.get(name);
		if (value == null) {
			return -1;
		}
		return ConvertUtil.obj2int(value);
	}

	/**
	 * 获取java.sql.Date数据
	 * 
	 * @param r
	 *            记录
	 * @param name
	 *            数据的键值
	 * @return 数据
	 */
	public static Date getDate(final Record r, final String name) {
		if (r == null) {
			return null;
		}
		if (name == null) {
			return null;
		}
		Object value = r.get(name);
		if (value == null) {
			return null;
		}
		return me().castTo(value, Date.class);
	}

	/**
	 * 获取布尔数据
	 * 
	 * @param r
	 *            记录
	 * @param name
	 *            数据的键值
	 * @return 数据
	 */
	public static Boolean getBoolean(final Record r, final String name) {
		if (r == null) {
			return false;
		}
		if (name == null) {
			return false;
		}
		Object value = r.get(name);
		if (value == null) {
			return false;
		}
		return me().castTo(value, Boolean.class);
	}

	/**
	 * 获取Double数据
	 * 
	 * @param r
	 *            记录
	 * @param name
	 *            数据的键值
	 * @return 数据
	 */
	public static double getDouble(final Record r, final String name) {
		if (r == null) {
			return 0;
		}
		if (name == null) {
			return 0;
		}
		Object value = r.get(name);
		if (value == null) {
			return 0;
		}
		return ConvertUtil.obj2double(value);
	}

	public static <T> Map<T, Record> list2map(final Class<T> keyClass,
			final Collection<Record> coll, final String keyFieldName) {
		Map<T, Record> map = MapUtil.map();
		for (Record one : coll) {
			map.put(Castors.me().castTo(one.get(keyFieldName), keyClass), one);
		}
		return map;
	}
}
