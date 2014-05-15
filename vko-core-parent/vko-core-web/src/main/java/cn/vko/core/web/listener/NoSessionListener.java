/**
 * NoSessionFilter.java
 * cn.vko.filter
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VKO监听器用于检测是否错误的创建了session，如果创建了就记录异常
 * 
 * @author 彭文杰
 * @Date 2013-09-05
 */
public class NoSessionListener implements HttpSessionListener {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(NoSessionListener.class);

	@Override
	public void sessionCreated(final HttpSessionEvent se) {
		logger.error("session被错误的创建了,请检查代码！！"); //$NON-NLS-1$
	}

	@Override
	public void sessionDestroyed(final HttpSessionEvent se) {

	}

}
