/**
 * WwwFetchUser.java
 * cn.vko.core.web.www.db
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.www.db;

import cn.vko.core.common.exception.impl.TimeoutException;
import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.db.dao.impl.support.IFetchUser;
import cn.vko.core.web.actionfilter.support.AuthUtil;
import cn.vko.core.web.actionfilter.support.AuthUtil.Scope;

/**
 * 获取当前用户
 * 
 * @author 彭文杰
 * @Date 2013-12-23
 */
public class WwwFetchUser implements IFetchUser {

	@Override
	public long getCurrentUserId() {
		long userId = ConvertUtil.obj2long(AuthUtil.getUserId(Scope.REQ));
		if (userId < 0) {
			return -1;
		}
		return userId;
	}

	@Override
	public long getCurrentUserIdWithEx() {
		long userId = getCurrentUserId();
		if (userId == -1) {
			throw new TimeoutException("您尚未登录,请先登录!");
		}
		return userId;
	}

	@Override
	public String getCurrentUserName() {
		return "todo";
	}

}
