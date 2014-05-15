/**
 * LoginServiceFactory.java
 * cn.vko.websso.service.support
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.service.support;

import java.util.List;

import org.nutz.ioc.loader.annotation.IocBean;

import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.sso.service.ILoginService;

/**
 * 登录服务的工厂类
 * 
 * @author dell
 * @Date 2013-10-23
 */
@IocBean
public class LoginServiceFactory {
	private List<ILoginService> services;

	public ILoginService getService(final int type) {
		if (Util.isEmpty(services)) {
			throw ExceptionUtil.bEx("登录服务未设置，请检查单点登录服务器！");
		}
		for (ILoginService s : services) {
			if (s.getSsoType() == type) {
				return s;
			}
		}
		throw ExceptionUtil.bEx("没有对应的单点登录服务，请检查服务类别是否正确！");
	}
}
