/**
 * CollectionUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.Util.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nutz.lang.Lang;

/**
 * collection操作相关的工具
 * 
 * @author 彭文杰
 * @author 赵立伟
 * @Date 2013-1-22
 * @version 5.1.0
 */
public class CollectionUtil {

	/**
	 * 根据传递的参数 参数List泛型数组
	 * 
	 * @param eles
	 *            需要转换成的数据
	 * @return 转换后的List泛型数组
	 */
	public static <T> List<T> list(final T... eles) {
		return Lang.list(eles);
	}

	/**
	 * 根据传递的参数 参数Set泛型数组
	 * 
	 * @param eles
	 *            需要转换成的数据
	 * @return 转换后的Set泛型数组
	 */
	public static <T> Set<T> set(final T... eles) {
		return Lang.set(eles);
	}

	/**
	 * 取的数组中末尾的数据
	 * 
	 * @param ls
	 *            被取的数组
	 * @return 末尾的数据
	 */
	public static <T> T last(final List<T> ls) {
		if (isEmpty(ls)) {
			return null;
		}
		return ls.get(ls.size() - 1);
	}

	/**
	 * 将集合变成指定类型的列表
	 * 
	 * @param col
	 *            集合
	 * @param eleType
	 *            列表类型
	 * @return 列表对象
	 */
	public static <T> List<T> collection2list(final Collection<?> col,
			final Class<T> eleType) {
		return Lang.collection2list(col, eleType);
	}

	/**
	 * 将集合变成指定类型的列表
	 * 
	 * @param col
	 *            集合
	 * @return 列表对象
	 */
	public static <T> List<T> collection2list(final Collection<T> col) {
		return Lang.collection2list(col);
	}

	/**
	 * 将集合变成指定类型的数组
	 * 
	 * @param col
	 *            集合
	 * @param eleType
	 *            数组元素类型
	 * @return 数组
	 */
	public static <T> T[] collection2array(final Collection<?> col,
			final Class<T> eleType) {
		return Lang.collection2array(col, eleType);
	}

	/**
	 * 将集合变成指定类型的数组
	 * 
	 * @param col
	 *            集合
	 * @return 数组
	 */
	public static <T> T[] collection2array(final Collection<T> col) {
		return Lang.collection2array(col);
	}

	/**
	 * 将集合变成指定类型的map
	 * 
	 * @param map类型
	 * @param col
	 *            集合
	 * @return 对应的属性名称
	 */
	public static <T extends Map<Object, Object>> Map<?, ?> collection2map(
			final Class<T> mapClass, final Collection<?> coll,
			final String keyFieldName) {
		return Lang.collection2map(mapClass, coll, keyFieldName);
	}

	/**
	 * 截取list
	 * 
	 * @param allList
	 *            完整的list
	 * @param start
	 *            开始位置
	 * @param stop
	 *            结束位置
	 * @return 截取的list
	 */
	public static List<?> subList(final List<?> allList, final int start,
			final int stop) {
		if (isEmpty(allList)) {
			return list();
		}
		if (start > stop || stop < 0) {
			return list();
		}
		int size = allList.size();
		if (start > size) {
			return list();
		}
		int usedStop = stop >= size ? size : stop;
		return allList.subList(start, usedStop);
	}
}
