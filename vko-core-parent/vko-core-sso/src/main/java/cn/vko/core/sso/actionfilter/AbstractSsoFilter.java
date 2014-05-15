/**
 * SsoFilter.java
 * cn.vko.web.common.actionfilter
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.actionfilter;

import javax.servlet.http.Cookie;

import lombok.Data;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import cn.vko.core.common.util.Util;
import cn.vko.core.sso.service.RedisSsoService;
import cn.vko.core.web.config.ConfigKey;
import cn.vko.core.web.config.KvConfig;
import cn.vko.core.web.util.RequestUtil;

/**
 * 单点登录过滤器
 * 
 * @author 彭文杰
 * @Date 2013-11-6
 */
@Data
public abstract class AbstractSsoFilter implements ActionFilter {
	private KvConfig config;
	private RedisSsoService redisSsoService;

	@Override
	public View match(final ActionContext ac) {
		String token = getToken(ac);
		if (Util.isEmpty(token)) {
			return null;
		}
		String userId = redisSsoService.fetch(getSsoType(), token);
		if (Util.isEmpty(userId)) {
			return null;
		}
		afterGet(ac, userId);
		return null;

	}

	/**
	 * 从请求中获取token
	 * 
	 * @param ac
	 *            请求上下文
	 * @return token
	 */
	protected String getToken(final ActionContext ac) {
		Cookie ck = RequestUtil.getCookie(ac.getRequest(),
				config.getValue(ConfigKey.SSO_COOKIE_KEY));
		if (ck == null) {
			return null;
		}
		String token = ck.getValue();
		return token;
	}

	/**
	 * 获取当前sso的类别
	 * 
	 * @return sso的类别
	 */
	protected abstract int getSsoType();

	/**
	 * 获取用户id之后的操作
	 * 
	 * @param userId
	 * @return
	 */
	protected abstract void afterGet(final ActionContext ac, final String userId);
}
