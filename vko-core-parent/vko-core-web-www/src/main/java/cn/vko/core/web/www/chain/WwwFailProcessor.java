/**
 * WwwFailProcessor.java
 * cn.vko.core.web.www.chain
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.www.chain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.mvc.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.core.common.exception.IBusinessException;
import cn.vko.core.common.exception.IParamException;
import cn.vko.core.common.exception.ITimeoutException;
import cn.vko.core.common.util.Util;
import cn.vko.core.web.chain.FailProcessor;
import cn.vko.core.web.config.ConfigKey;
import cn.vko.core.web.config.KvConfig;

/**
 * 网站端异常处理类
 * 
 * @author 彭文杰
 * @Date 2013-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WwwFailProcessor extends FailProcessor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WwwFailProcessor.class);

	private KvConfig config;

	@Override
	protected void handleError(final ActionContext ac, final Throwable th) {
		if (th instanceof IBusinessException || th instanceof ITimeoutException
				|| th instanceof IParamException) {
			return;
		}
		String url = "unknown";
		if (ac.getRequest() != null
				&& !Util.isEmpty(ac.getRequest().getRequestURL())) {
			url = ac.getRequest().getRequestURL().toString();
		}
		logger.error("exception happen,please handle it!the url is " + url, th); //$NON-NLS-1$
	}

	@Override
	protected String getLoginUrl() {
		return config.getValue(ConfigKey.LOGIN_PAGE);
	}
}
