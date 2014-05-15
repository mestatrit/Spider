/**
 * ValidProcessor.java
 * cn.vko.web.chain
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.chain;

import java.lang.annotation.Annotation;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.impl.processor.AbstractProcessor;

import cn.vko.core.web.chain.support.Cache;

/**
 * 页面缓存时间设定
 * 
 * @author 彭文杰
 * @author 赵立伟
 * @Date 2013-3-6
 */
public class CacheProcessor extends AbstractProcessor {

	@Override
	public void process(final ActionContext ac) throws Throwable {
		Annotation[] annotations = ac.getMethod().getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Cache) {
				Cache a = (Cache) annotation;
				ac.getResponse().setHeader("Cache-Control",
						"max-age=" + a.value());
			}
		}
		doNext(ac);
	}

}
