/**
 * ValidProcessor.java
 * cn.vko.web.chain
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.chain;

import static cn.vko.core.common.util.Util.*;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.impl.processor.AbstractProcessor;

import cn.vko.core.common.util.Util;
import cn.vko.core.web.chain.support.NotValid;
import cn.vko.core.web.chain.support.ValidException;

/**
 * 后台数据检验
 * <p>
 * 基于hibernate-validtor
 * 
 * @author 庄君祥
 * @Date 2013-3-6
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ValidProcessor extends AbstractProcessor {

	private static Validator validator; // 它是线程安全的
	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Override
	public void process(final ActionContext ac) throws Throwable {
		Object[] args = ac.getMethodArgs();
		if (isEmpty(args)) {
			doNext(ac);
			return;
		}
		for (Object arg : args) {
			if (isEmpty(arg)) {
				continue;
			}
			Annotation[] annotations = ac.getMethod().getParameterAnnotations()[0];
			if (isEmpty(annotations)) {
				continue;
			}
			boolean isValid = true;
			for (Annotation annotation : annotations) {
				if (annotation instanceof NotValid) {
					isValid = false;
				}
			}
			if (!isValid) {
				break;
			}
			for (Annotation annotation : annotations) {
				// 如果有注释验证，验证完结束判断
				if (annotation instanceof Param) {
					errorHandler(valid(arg));
				}
			}
		}
		doNext(ac);
	}

	/**
	 * 验证信息合法
	 * 
	 * @param obj
	 *            需要验证的对象
	 * @return 不合法信息的Map
	 * @throws Throwable
	 */
	private Set<ConstraintViolation<Object>> valid(final Object obj)
			throws Throwable {
		if (isEmpty(obj)) {
			return null;
		}
		return validator.validate(obj);

	}

	/**
	 * 异常出现后的处理逻辑
	 * 
	 * @param errors
	 *            异常信息
	 */
	public void errorHandler(final Set<ConstraintViolation<Object>> errors) {
		if (Util.isEmpty(errors)) {
			return;
		}
		throw new ValidException("校验错误", errors);
	}
}
