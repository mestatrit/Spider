/**
 * ValidException.java
 * cn.vko.web.chain.support
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.chain.support;

import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cn.vko.core.common.exception.IParamException;

/**
 * 配合hibernate-validtor错误的处理异常
 * 
 * @author 庄君祥
 * @Date 2013-3-7
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
public class ValidException extends RuntimeException implements
		IParamException {
	private Set<ConstraintViolation<Object>> errors;

	public ValidException() {
		super();
	}

	public ValidException(final String message) {
		super(message);
	}

	public ValidException(final String message,
			final Set<ConstraintViolation<Object>> errors) {
		super(message);
		this.errors = errors;
	}
}
