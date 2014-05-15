/**
 * ExceptionUtil.java
 * cn.vko.util
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.StringUtil.format;
import static cn.vko.core.common.util.Util.isEmpty;

import java.io.IOException;
import java.sql.SQLException;

import cn.vko.core.common.exception.IBusinessException;
import cn.vko.core.common.exception.IParamException;
import cn.vko.core.common.exception.impl.BusinessException;
import cn.vko.core.common.exception.impl.ParamException;

/**
 * 异常处理工具类
 * 
 * @author 彭文杰
 * @author 赵立伟
 * @author 庄君祥
 * @Date 2012-5-1
 * @version 5.1.0
 */
public class ExceptionUtil {
	/**
	 * 根据不同的异常种类，返回对应中文意义
	 * <p>
	 * 对于vkoException直接返回message，对于其他常见异常返回常用信息
	 * 
	 * @param th
	 *            异常
	 * @return 异常信息
	 */
	public static String getSimpleMessage(final Throwable th) {
		if (th instanceof IBusinessException || th instanceof IParamException) {
			return th.getMessage();
		} else if (th instanceof NullPointerException) {
			return "调用了未经初始化的对象或者是不存在的对象！";
		} else if (th instanceof IOException) {
			return "IO异常！";
		} else if (th instanceof ClassNotFoundException) {
			return "指定的类不存在！";
		} else if (th instanceof ArithmeticException) {
			return "数学运算异常！";
		} else if (th instanceof ArrayIndexOutOfBoundsException) {
			return "数组下标越界!";
		} else if (th instanceof IllegalArgumentException) {
			return "方法的参数错误！";
		} else if (th instanceof ClassCastException) {
			return "类型强制转换错误！";
		} else if (th instanceof SecurityException) {
			return "违背安全原则异常！";
		} else if (th instanceof SQLException) {
			return "操作数据库异常！";
		} else if (th instanceof NoSuchMethodError) {
			return "方法末找到异常！";
		} else if (th instanceof InternalError) {
			return "Java虚拟机发生了内部错误";
		} else {
			return "程序内部错误，操作失败！";
		}
	}

	/**
	 * 生成businessException
	 * 
	 * @param message
	 *            异常信息
	 * @return 业务异常
	 */
	public static BusinessException bEx(final String message,
			final Object... args) {
		return new BusinessException(format(message, args));
	}

	/**
	 * 生成businessException
	 * 
	 * @param message
	 *            异常信息
	 * @param e
	 *            包装的其他异常
	 * @return 业务异常
	 */
	public static BusinessException bEx(final String message,
			final Throwable e, final Object... args) {
		return new BusinessException(format(message, args), e);
	}

	/**
	 * 生成IParamException
	 * 
	 * @param message
	 *            异常信息
	 * @return 数据异常
	 */
	public static ParamException pEx(final String message, final Object... args) {
		return new ParamException(format(message, args));
	}

	/**
	 * 生成IParamException
	 * 
	 * @param message
	 *            异常信息
	 * @param e
	 *            包装的其他异常
	 * @return 数据异常
	 */
	public static ParamException pEx(final String message, final Throwable e,
			final Object... args) {
		return new ParamException(format(message, args), e);
	}

	/**
	 * 判断是否为空，如果为空则抛出业务异常
	 * 
	 * @param obj
	 *            判断对象
	 * @param msg
	 *            提示信息
	 * @pa
	 */
	public static void checkNull(final Object obj, final String msg,
			final Object... objects) {
		if (obj == null) {
			throw pEx(msg, objects);
		}
	}

	/**
	 * 判断是否为空，如果为空则抛出数据异常
	 * 
	 * @param obj
	 *            判断对象
	 * @param msg
	 *            提示信息
	 */
	public static void checkEmpty(final Object obj, final String msg,
			final Object... objects) {
		if (isEmpty(obj)) {
			throw pEx(msg, objects);
		}
	}

	/**
	 * 判断是否为空，如果为空则抛出数据异常
	 * 
	 * @param obj
	 *            判断对象
	 * @param msg
	 *            提示信息
	 */
	public static void checkNullBEx(final Object obj, final String msg,
			final Object... objects) {
		if (obj == null) {
			throw pEx(msg, objects);
		}
	}

	/**
	 * 判断是否为空，如果为空则抛出业务异常
	 * 
	 * @param obj
	 *            判断对象
	 * @param msg
	 *            提示信息
	 */
	public static void checkEmptyBEx(final Object obj, final String msg,
			final Object... objects) {
		if (isEmpty(obj)) {
			throw bEx(msg, objects);
		}
	}

}
