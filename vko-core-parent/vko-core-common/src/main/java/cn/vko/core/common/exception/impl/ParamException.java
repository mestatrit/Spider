package cn.vko.core.common.exception.impl;

import cn.vko.core.common.exception.IParamException;

/**
 * 系统参数异常，直接显示给用户
 * <p>
 * 不需要记录日志或者提示相关人员
 * 
 * @author 彭文杰
 * @Date 2011-9-1
 */
@SuppressWarnings("serial")
public class ParamException extends RuntimeException implements IParamException {
	public ParamException() {
		super();
	}

	public ParamException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ParamException(final String message) {
		super(message);
	}

	public ParamException(final Throwable cause) {
		super(cause);
	}
}
