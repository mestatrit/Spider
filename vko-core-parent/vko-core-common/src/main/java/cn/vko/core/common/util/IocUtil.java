/**
 * IocUtil.java
 * cn.vko.util
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.IocException;

/**
 * 用于持有ioc对象的工具类
 * <p>
 * 持有ioc并对ioc的方法进行封装
 * 
 * @author 彭文杰
 * @author 赵立伟
 * @Date 2012-4-24
 * @version 5.0.2
 */
public class IocUtil {
	private static Ioc INSTANCE;

	public static void setIoc(final Ioc ioc) {
		INSTANCE = ioc;
	}

	@Deprecated
	public static Ioc get() {
		return INSTANCE;
	}

	/**
	 * 从容器中获取一个对象
	 * 
	 * @param type
	 *            类型
	 * @return 对象本身
	 */
	@Deprecated
	public static <T> T get(final Class<T> type) {
		if (INSTANCE == null) {
			throw ExceptionUtil.bEx("IocUtil未被初始化!");
		}
		try {
			return INSTANCE.get(type);
		} catch (IocException e) {
			throw ExceptionUtil.bEx(e.getMessage(), e);
		}
	}

	/**
	 * 从容器中获取一个对象
	 * 
	 * @param type
	 *            类型
	 * @param name
	 *            对象的名称
	 * @return 对象本身
	 */
	@Deprecated
	public static <T> T get(final Class<T> type, final String name) {
		if (INSTANCE == null) {
			throw ExceptionUtil.bEx("IocUtil未被初始化!");
		}
		try {
			return INSTANCE.get(type, name);
		} catch (IocException e) {
			throw ExceptionUtil.bEx("获取Ioc内对象出错", e);
		}
	}

}
