/**
 * BeanUtil.java
 * cn.vko.util
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.Util.*;

import java.lang.reflect.Field;

import org.nutz.castor.Castors;
import org.nutz.lang.Mirror;

/**
 * 封装了bean的操作
 * <p>
 * 从一个bean copy到另一个bean
 * <p>
 * 依赖了nutz 的Mirror
 * 
 * @author 庄君祥
 * @author 刘一卓
 * @Date 2012-5-4
 * @see Mirror
 * @version 5.0.1
 */
public class BeanUtil {
	/**
	 * 拷贝一个对象属性到另一个对象
	 * <p>
	 * 依赖Mirror的功能
	 * 
	 * @param target
	 *            目标对象
	 * @param source
	 *            源对象
	 * @see Mirror
	 */
	public static void copyProperties(final Object source, final Object target) {
		Mirror<?> sourceMirror = Mirror.me(source);
		Mirror<?> targetMirror = Mirror.me(target);
		Field[] targetFds = targetMirror.getFields();

		for (Field tfd : targetFds) {
			Object value = null;
			try {
				value = sourceMirror.getValue(source, tfd.getName());
				targetMirror.setValue(target, tfd.getName(), value);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 判断是否存在某属性
	 * 
	 * @param fds
	 *            属性列表
	 * @param name
	 *            属性名
	 * @return 是否存在
	 */
	private static boolean contain(final Field[] fds, final String name) {
		if (isEmpty(fds) || isEmpty(name)) {
			return false;
		}
		for (Field fd : fds) {
			if (fd == null) {
				continue;
			}
			if (name.equals(fd.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对象是否包含某个属性
	 * 
	 * @param obj
	 *            需要判断的对象
	 * @param propertyName
	 *            属性名称
	 * @return 如果对象包含该属性返回true,否则返回false
	 */
	public static boolean contain(final Object obj, final String propertyName) {
		Mirror<?> mirror = Mirror.me(obj);
		try {
			Field fd = mirror.getField(propertyName);
			return fd != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 直接获取某属性
	 * 
	 * @param obj
	 *            对象
	 * @param propertyName
	 *            属性名
	 * @return 属性值
	 */
	public static Object get(final Object obj, final String propertyName) {
		Mirror<?> mirror = Mirror.me(obj);
		try {
			return mirror.getValue(obj, propertyName);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 直接获取某属性
	 * 
	 * @param obj
	 *            对象
	 * @param propertyName
	 *            属性名
	 * @return 属性值
	 */
	public static String getString(final Object obj, final String propertyName) {
		return Castors.me().castToString(get(obj, propertyName));
	}

	/**
	 * 判断属性是否为空
	 * <p>
	 * <ul>
	 * <li>如果是int或者long时，值为0时，则为empty
	 * <li>对象为null的时候，为empty
	 * </ul>
	 * <p>
	 * 其他的参见Util.isEmpty()方法
	 * 
	 * @param fieldName
	 *            属性名
	 * @return 是否为空
	 */
	public static boolean isFieldEmpty(final Object source,
			final String fieldName) {
		if (source == null || isEmpty(fieldName)) {
			return true;
		}
		Object value = Mirror.me(source).getValue(source, fieldName);
		return isEmpty(value);
	}
}
