/**
 * Cache.java
 * cn.vko.web.cache
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.chain.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识页面缓存时间
 * 
 * @author 彭文杰
 * @Date 2013-3-29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface Cache {
	/**
	 * 缓存时间，单位秒 默认时间3600秒
	 * 
	 * @return 缓存时间
	 */
	int value() default 3600;
}
