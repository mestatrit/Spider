/**
 * AuthUtil.java
 * cn.vko.web.common.actionfilter.support
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.actionfilter.support;

import java.lang.reflect.Method;

import org.nutz.mvc.Mvcs;

import cn.vko.core.common.exception.impl.TimeoutException;
import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.common.util.EncryptUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.web.actionfilter.IAuth;

/**
 * 权限校验工具类
 * 
 * @author 彭文杰
 * @Date 2013-11-13
 */
public class AuthUtil {
	/**
	 * http请求中获取当前用户的主键的键值
	 */
	public final static String USER_ID_KEY = "r_userid";
	/**
	 * http请求中获取当前用户真实姓名的键值
	 */
	public final static String REAL_NAME_KEY = "r_realname";
	/**
	 * http请求中获取当前用户真实姓名的键值
	 */
	public final static String USER_AUTHS_KEY = "r_auths";

	/**
	 * 缓存的范围
	 * 
	 */
	public static enum Scope {
		REQ, SESSION
	}

	/**
	 * 获取key
	 * 
	 * @param method
	 *            方法类
	 * @return 权限唯一key
	 */
	public static String getAuthKey(final Method method) {
		if (method == null) {
			return "";
		}
		return getAuthKey(method.getDeclaringClass().getName(),
				method.getName());

	}

	/**
	 * 获取key
	 * 
	 * @param moduleName
	 *            类名
	 * @param methodName
	 *            方法名
	 * @return 权限唯一key
	 */
	public static String getAuthKey(final String moduleName,
			final String methodName) {
		StringBuilder sb = new StringBuilder(moduleName).append("_").append(
				methodName);
		return EncryptUtil.encrypt(sb.toString());
	}

	public static void cacheAuth(final Scope scope, final Object userId,
			final String realName, final IAuth auth) {
		set(scope, USER_ID_KEY, userId);
		set(scope, REAL_NAME_KEY, realName);
		set(scope, USER_AUTHS_KEY, auth);
	}

	private static void set(final Scope scope, final String key,
			final Object value) {
		switch (scope) {
		case REQ:
			Mvcs.getReq().setAttribute(key, value);
			break;
		case SESSION:
			Mvcs.getHttpSession().setAttribute(key, value);
			break;
		default:
			Mvcs.getReq().setAttribute(key, value);
			break;
		}
	}

	public static void rmAuth(final Scope scope) {
		rm(scope, USER_ID_KEY);
		rm(scope, REAL_NAME_KEY);
		rm(scope, USER_AUTHS_KEY);
	}

	private static void rm(final Scope scope, final String key) {
		switch (scope) {
		case REQ:
			Mvcs.getReq().removeAttribute(key);
			break;
		case SESSION:
			Mvcs.getHttpSession().removeAttribute(key);
			break;
		default:
			Mvcs.getReq().removeAttribute(key);
			break;
		}
	}

	public static String getUserId(final Scope scope) {
		return ConvertUtil.obj2str(get(scope, USER_ID_KEY));
	}

	public static long getUserIdLong(final Scope scope) {
		return ConvertUtil.obj2long(get(scope, USER_ID_KEY));
	}

	public static long getUserIdEx(final Scope scope) {
		long userId = getUserIdLong(scope);
		if (userId <= 0L) {
			throw new TimeoutException("用户未登录！");
		}
		return userId;
	}

	private static Object get(final Scope scope, String key) {
		switch (scope) {
		case REQ:
			return Mvcs.getReq().getAttribute(key);
		case SESSION:
			return Mvcs.getHttpSession().getAttribute(key);
		default:
			return Mvcs.getReq().getAttribute(key);
		}
	}

	public static String getRealName(final Scope scope) {
		return ConvertUtil.obj2str(get(scope, REAL_NAME_KEY));
	}

	public static IAuth getAuth(final Scope scope) {
		Object o = get(scope, REAL_NAME_KEY);
		if (o instanceof IAuth) {
			return (IAuth) o;
		}
		return null;
	}

	public static boolean isLogin(final Scope scope) {
		return !Util.isEmpty(getUserId(scope));
	}
}
