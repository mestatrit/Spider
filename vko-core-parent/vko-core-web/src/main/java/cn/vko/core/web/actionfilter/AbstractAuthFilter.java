/**
 * AuthFilter.java
 * cn.vko.web.common.actionfilter
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.actionfilter;

import java.lang.reflect.Method;

import lombok.Data;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import cn.vko.core.web.actionfilter.support.AuthUtil;
import cn.vko.core.web.actionfilter.support.Auth;

/**
 * 权限校验过滤器
 * 
 * @author 彭文杰
 * @Date 2013-11-6
 */
@Data
public abstract class AbstractAuthFilter implements ActionFilter {

	@Override
	public View match(final ActionContext ac) {
		Method method = ac.getMethod();
		if (!method.isAnnotationPresent(Auth.class)) {
			return null;
		}
		String authKey = AuthUtil.getAuthKey(method);
		if (hasAuth(ac, authKey)) {
			return null;
		}
		return getLoginView(ac);
	}

	/**
	 * 判断是否存在权限
	 * 
	 * @param authKey
	 *            权限key
	 * @return 是否有本权限
	 */
	protected abstract boolean hasAuth(final ActionContext ac,
			final String authKey);

	protected abstract View getLoginView(final ActionContext ac);
}
