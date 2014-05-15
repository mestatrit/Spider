package cn.vko.core.common.exception.impl;

import cn.vko.core.common.exception.ITimeoutException;

/**
 * 用户登录超时的异常，直接显示给用户
 * <p>
 * 不需要记录日志或者提示相关人员
 * 
 * @author 彭文杰
 * @Date 2011-9-1
 */
@SuppressWarnings("serial")
public class TimeoutException extends RuntimeException implements
		ITimeoutException {
	public TimeoutException() {
		super();
	}

	public TimeoutException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TimeoutException(final String message) {
		super(message);
	}

	public TimeoutException(final Throwable cause) {
		super(cause);
	}
}
