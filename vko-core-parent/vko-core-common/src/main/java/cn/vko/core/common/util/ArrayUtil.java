/**
 * ArrayUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.nutz.lang.Lang;

/**
 * 数组工具类
 * 
 * @author 宋星明
 * @author 庄君祥
 * @author 赵立伟
 * @Date 2013-2-19
 * @version 5.1.0
 */
public class ArrayUtil {

	/**
	 * 将数组转换成一个列表
	 * 
	 * @param array
	 *            原始数组
	 * @return 新列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> array2List(final T[] array) {
		if (null == array) {
			return CollectionUtil.list();
		}
		return Lang.array2list(array);
	}

	/**
	 * 将数组转换成一个列表
	 * 
	 * @param array
	 *            原始数组
	 * @return 新列表
	 */
	public static <T> Set<T> array2Set(final T[] array) {
		if (null == array) {
			return new LinkedHashSet<T>(0);
		}
		Set<T> re = new LinkedHashSet<T>(array.length);
		for (T obj : array) {
			re.add(obj);
		}
		return re;
	}

	/**
	 * 将long数组转换为set
	 * 
	 * @param array
	 * @return set
	 */
	public static Set<Long> array2Set(final long[] array) {
		if (null == array) {
			return new LinkedHashSet<Long>(0);
		}
		Set<Long> re = new LinkedHashSet<Long>(array.length);
		for (long obj : array) {
			re.add(obj);
		}
		return re;
	}

	/**
	 * 将数组转换成一个列表
	 * 
	 * @param array
	 *            原始数组
	 * @return 新列表
	 */
	public static List<Long> array2List(final long[] array) {
		if (null == array) {
			return CollectionUtil.list();
		}
		List<Long> re = new ArrayList<Long>(array.length);
		for (long obj : array) {
			re.add(obj);
		}
		return re;
	}

	/**
	 * 生成array的简便方法
	 * 
	 * @param arr
	 *            参数
	 * @return 数组
	 */
	public static <T> T[] array(final T... arr) {
		return arr;
	}

	/**
	 * 将数组转化为制定类型的数组
	 * 
	 * @param arr
	 *            数组
	 * @param eleType
	 *            转化的类别
	 * @return 转化后数组
	 */
	public static Object array2array(final Object arr, final Class<?> eleType) {
		return Lang.array2array(arr, eleType);
	}

	/**
	 * 把数组转化为字符串数组
	 * 
	 * @param arr
	 *            待转换数组
	 * @return 字符串数组
	 */
	public static String[] array2StringArray(final Object arr) {
		Object array2array = Lang.array2array(arr, String.class);
		if (array2array == null) {
			return array();
		}
		return (String[]) array2array;
	}

	/**
	 * 将Object对象转换成String[]
	 * 
	 * @param member
	 *            被转换的对象值
	 * @return String 数组
	 */
	@SuppressWarnings("rawtypes")
	public static String[] arrayObjectString(final Object member) {
		String[] strings = null;
		if (member.getClass().isArray()) {
			strings = array2StringArray(member);
		} else if (member instanceof Collection) {
			strings = CollectionUtil.collection2array((Collection) member,
					String.class);
		} else {
			strings = array(member.toString());
		}
		return strings;
	}
}
