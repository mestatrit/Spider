/**
 * EnumUtil.java
 * cn.vko.core.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import java.util.Map;

import cn.vko.core.common.enums.IEnum;
import cn.vko.core.common.enums.StringKeyLinkedMap;

/**
 * 自定义枚举的工具类
 * 
 * @author 彭文杰
 * @Date 2013-1-26
 * @version 5.0.2
 */
public final class EnumUtil {
	/**
	 * 将enum转换为map对象
	 * <p>
	 * key是对应的key,value是显示的值
	 * 
	 * @param en
	 *            enum的类型
	 * @return map对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IEnum> Map<String, String> enum2(final Class<T> en) {
		StringKeyLinkedMap re = new StringKeyLinkedMap();
		if (en == null) {
			return re;
		}
		if (!en.isEnum()) {
			return re;
		}
		T[] arr = en.getEnumConstants();
		for (T one : arr) {
			if (one == null) {
				continue;
			}
			re.put(one.key(), one.sname());
		}
		return re;
	}

	/**
	 * 获取枚举
	 * 
	 * @param en
	 *            枚举类别
	 * @param key
	 *            值
	 * @return 枚举
	 */
	public static <T extends IEnum> T get(final Class<T> en, final String key) {
		if (en == null) {
			return null;
		}
		T[] arr = en.getEnumConstants();
		for (T one : arr) {
			if (Util.eq(key, one.key())) {
				return one;
			}
		}
		return null;
	}

	/**
	 * 判断枚举是否存在
	 * 
	 * @param en
	 *            枚举类别
	 * @param key
	 *            值
	 * @return 枚举
	 */
	public static <T extends IEnum> boolean isContain(final Class<T> en,
			final String key) {
		return get(en, key) != null;
	}

	/**
	 * 获取枚举
	 * 
	 * @param en
	 *            枚举类别
	 * @param key
	 *            值
	 * @return 枚举
	 */
	public static <T extends IEnum> T get(final Class<T> en, final int key) {
		return get(en, String.valueOf(key));
	}

	/**
	 * 获取枚举
	 * 
	 * @param en
	 *            枚举类别
	 * @param key
	 *            值
	 * @return 枚举
	 */
	public static <T extends IEnum> String getName(final Class<T> en,
			final int key) {
		T t = get(en, String.valueOf(key));
		return Util.isEmpty(t) ? "" : t.sname();
	}

	/**
	 * 获取字符形键值
	 * 
	 * @param en
	 *            枚举类别
	 * @param ele
	 *            枚举
	 * @return 键值
	 */
	public static <T extends IEnum> Object getKey(final T ele) {
		return Util.isEmpty(ele) ? "-1" : ele.key();
	}

	/**
	 * 获取整形键值
	 * 
	 * @param en
	 *            枚举类别
	 * @param ele
	 *            枚举
	 * @return 键值
	 */
	public static <T extends IEnum> int getKeyInt(final T ele) {
		return Util.isEmpty(ele) ? -1 : ConvertUtil.obj2int(ele.key());
	}
}
