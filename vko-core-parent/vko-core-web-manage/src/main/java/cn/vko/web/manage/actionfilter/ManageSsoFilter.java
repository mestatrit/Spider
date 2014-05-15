/**
 * ManageSsoFilter.java
 * cn.vko.web.manage.actionfilter
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.actionfilter;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.View;

import cn.vko.core.sso.actionfilter.AbstractSsoFilter;
import cn.vko.core.web.actionfilter.support.AuthUtil;
import cn.vko.core.web.actionfilter.support.AuthUtil.Scope;

/**
 * sso过滤器
 * 
 * @author 彭文杰
 * @Date 2013-11-14
 */
public class ManageSsoFilter extends AbstractSsoFilter {
	@Override
	protected int getSsoType() {
		return 2;
	}

	@Override
	public View match(final ActionContext ac) {
		if (AuthUtil.isLogin(Scope.SESSION)) {
			return null;
		}
		super.match(ac);
		return null;

	}

	@Override
	protected void afterGet(ActionContext ac, String userId) {
		AuthUtil.cacheAuth(Scope.SESSION, userId, null, null);
	}
}
