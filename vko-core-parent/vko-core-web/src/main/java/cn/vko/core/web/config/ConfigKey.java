/**
 * ConfigKey.java
 * cn.vko.web.manage.common
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.config;

import cn.vko.core.common.enums.IEnum;

/**
 * 配置的关键key
 * 
 * @author 彭文杰
 * @Date 2013-11-8
 */
public enum ConfigKey implements IEnum {
	STATIC, LOGIN_PAGE, LOGOUT_URL, LOGOUT_TO, SSO_COOKIE_KEY, SSO_COOKIE_DOMAIN;

	@Override
	public String key() {
		return this.name().toLowerCase();
	}

	@Override
	public String sname() {
		return this.name().toLowerCase();
	}
}
