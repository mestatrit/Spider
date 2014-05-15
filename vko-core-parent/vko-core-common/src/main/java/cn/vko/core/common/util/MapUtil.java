/**
 * MapUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.CollectionUtil.*;
import static cn.vko.core.common.util.ExceptionUtil.*;
import static cn.vko.core.common.util.Util.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Lang;

/**
 * 有关map处理的工具类
 * 
 * @author 彭文杰
 * @author 赵立伟
 * @Date 2013-1-22
 * @version 5.0.2
 */
public class MapUtil {
	/**
	 * 
	 * 较方便的创建一个map
	 * <p>
	 * 注，这里的 Map，是 HashMap 的实例
	 * 
	 * @param <K>
	 *            key的类型
	 * @param <V>
	 *            value的类型
	 * @return map对象
	 */
	public static <K, V> Map<K, V> map() {
		return new HashMap<K, V>();
	}

	/**
	 * 
	 * 较方便的创建一个map
	 * <p>
	 * 注，这里的 Map，是 HashMap 的实例
	 * 
	 * @param <K>
	 *            key的类型
	 * @param <V>
	 *            value的类型
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return map对象，包含key、value
	 */
	public static <K, V> Map<K, V> map(final K key, final V value) {
		Map<K, V> map = map();
		map.put(key, value);
		return map;
	}

	/**
	 * 较方便的创建一个map
	 * <p>
	 * 奇数个作为key，偶数个作为value 生成map对象 请注意数据的类型，要是不匹配，在获取时会提示
	 * java.lang.ClassCastException
	 * 
	 * @param args
	 *            参数
	 * @return map对象
	 * @throws DataException
	 *             如果参数为奇数个抛出该异常
	 */
	public static <K, V> Map<K, V> map(final Object... args) {
		Map<K, V> map = map();
		evenoddMap(map, args);
		return map;
	}

	/**
	 * 将数据奇数个作为key，偶数个作为value，填充到对象中 请注意数据的类型，要是不匹配，在获取时会提示
	 * java.lang.ClassCastException
	 * 
	 * @param <K>
	 *            key的类型
	 * @param <V>
	 *            value的类型
	 * @param map
	 *            填充的map
	 * @param args
	 *            参数列表
	 * @throws DataException
	 *             如果参数为奇数个抛出该异常
	 */
	@SuppressWarnings({ "unchecked" })
	private static <K, V> void evenoddMap(final Map<K, V> map,
			final Object... args) {
		if (args.length % 2 != 0) {
			throw pEx("生成map时,元素为单数!");
		}
		for (int i = 0; i < args.length; i += 2) {
			map.put((K) args[i], (V) args[i + 1]);
		}
	}

	/**
	 * 
	 * 较方便的创建一个map
	 * <p>
	 * 注，这里的 Map，是 LinkedHashMap 的实例
	 * 
	 * @param <K>
	 *            key的类型
	 * @param <V>
	 *            value的类型
	 * @return map对象
	 */
	public static <K, V> Map<K, V> linkedMap() {
		return new LinkedHashMap<K, V>();
	}

	/**
	 * 
	 * 较方便的创建一个map
	 * <p>
	 * 注，这里的 Map，是 LinkedHashMap 的实例
	 * 
	 * @param <K>
	 *            key的类型
	 * @param <V>
	 *            value的类型
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return map对象，包含key、value
	 */
	public static <K, V> Map<K, V> linkedMap(final K key, final V value) {
		Map<K, V> map = linkedMap();
		map.put(key, value);
		return map;
	}

	/**
	 * 较方便的创建一个LinkedHashMap
	 * <p>
	 * 奇数个作为key，偶数个作为value 生成map对象
	 * 
	 * @param args
	 *            参数
	 * @return map对象
	 * @throws DataException
	 *             如果参数为基数个抛出该异常
	 */
	public static <K, V> Map<K, V> linkedMap(final Object... args) {
		Map<K, V> map = linkedMap();
		evenoddMap(map, args);
		return map;
	}

	/**
	 * 向map的列表中添加对象
	 * <p>
	 * 如果map不存在
	 * 
	 * @param <K>
	 *            key类型
	 * @param <V>
	 *            value类型
	 * @param map
	 *            要添加的map
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @throws DataException
	 *             map为null，则提示DataException
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, List<V>> addMapList(final Map<K, List<V>> map,
			final K key, final V value) {
		checkNull(map, "填充map时,数据为空!");
		if (map.containsKey(key)) {
			map.get(key).add(value);
		} else {
			map.put(key, list(value));
		}
		return map;
	}

	/**
	 * 获取map里列表
	 * 
	 * @param map
	 *            外层map
	 * @param key
	 *            列表的键值
	 * @return 列表
	 */
	@SuppressWarnings("unchecked")
	public static <K1, K2, V> List<V> getMapMap(final Map<K1, Map<K2, V>> map,
			final K1 key) {
		if (isEmpty(map)) {
			return Lang.list();
		}
		if (map.containsKey(key)) {
			return Lang.collection2list(map.get(key).values());
		}
		return Lang.list();

	}

	/**
	 * 向map里面添加map
	 * 
	 * @param map
	 *            待添加的map
	 * @param key
	 *            第一层key
	 * @param key2
	 *            第2层key
	 * @param value
	 *            值
	 * @return map对象
	 */
	public static <K1, K2, V> Map<K1, Map<K2, V>> addMapMap(
			final Map<K1, Map<K2, V>> map, final K1 key, final K2 key2,
			final V value) {
		checkNull(map, "填充map时,数据为空!");
		if (map.containsKey(key)) {
			map.get(key).put(key2, value);
		} else {
			Map<K2, V> inMap = linkedMap();
			inMap.put(key2, value);
			map.put(key, inMap);
		}
		return map;
	}

	/**
	 * 获取map里map的值
	 * 
	 * @param map
	 *            外层map
	 * @param key
	 *            外层map的键值
	 * @param key2
	 *            里层map的键值
	 * @return value
	 */
	public static <K1, K2, V> V getMapMap(final Map<K1, Map<K2, V>> map,
			final K1 key, final K2 key2) {
		if (isEmpty(map)) {
			return null;
		}
		if (map.containsKey(key)) {
			return map.get(key).get(key2);
		}
		return null;
	}

	/**
	 * 移除map里map的值
	 * 
	 * @param map
	 *            外层map
	 * @param key
	 *            外层map的键值
	 * @param key2
	 *            里层map的键值
	 */
	public static <K1, K2, V> void removeMapMap(final Map<K1, Map<K2, V>> map,
			final K1 key, final K2 key2) {
		if (isEmpty(map)) {
			return;
		}
		if (map.containsKey(key)) {
			map.get(key).remove(key2);
		}
	}
}
