/**
 * VkoLinkedMap.java
 * cn.vko.util.support
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.enums;

import java.util.LinkedHashMap;

/**
 * 继承默认的linkmap
 * <p>
 * 覆盖其get方法和put方法，让key转换为String 为了jsp页面获取code时使用
 * 
 * @author 彭文杰
 * @Date 2012-6-7
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class StringKeyLinkedMap extends LinkedHashMap {

	@Override
	public Object get(final Object key) {
		if (key == null) {
			return null;
		}
		return super.get(String.valueOf(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(final Object key, final Object value) {
		if (key == null) {
			return value;
		}
		super.put(String.valueOf(key), value);
		return value;
	}
}
