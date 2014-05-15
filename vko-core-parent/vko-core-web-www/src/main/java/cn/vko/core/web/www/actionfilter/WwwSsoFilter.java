/**
 * WwwSsoFilter.java
 * cn.vko.web.www.actionfilter
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.www.actionfilter;

import org.nutz.mvc.ActionContext;

import cn.vko.core.sso.actionfilter.AbstractSsoFilter;
import cn.vko.core.web.actionfilter.support.AuthUtil;
import cn.vko.core.web.actionfilter.support.AuthUtil.Scope;

/**
 * sso过滤器
 * 
 * @author 彭文杰
 * @Date 2013-11-14
 */
public class WwwSsoFilter extends AbstractSsoFilter {

	@Override
	protected int getSsoType() {
		return 1;
	}

	@Override
	protected void afterGet(ActionContext ac, String userId) {
		AuthUtil.cacheAuth(Scope.REQ, userId, null, null);
	}

}
