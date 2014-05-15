/**
 * AbstractLoginService.java
 * cn.vko.websso.service.impl
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.service.impl;

import static cn.vko.core.common.util.ExceptionUtil.bEx;
import lombok.Data;
import cn.vko.core.common.util.RandomUtil;
import cn.vko.core.sso.common.ErrorType;
import cn.vko.core.sso.service.ILoginService;
import cn.vko.core.sso.service.RedisSsoService;
import cn.vko.core.sso.service.dto.SsoUser;

/**
 * 基础的基于redis进行key缓存实现的类
 * 
 * @author 彭文杰
 * @Date 2013-10-18
 */
@Data
public abstract class AbstractLoginService implements ILoginService {
	private RedisSsoService redisSsoService;

	@Override
	public String login(final String loginName, final String password,
			final boolean rememberme) {
		SsoUser u = getUser(loginName);
		if (u == null) {
			throw bEx(ErrorType.NOTEXIST.sname());
		}
		u.checkPass(password);
		if (!u.isValid()) {
			throw bEx(ErrorType.INVALID.sname());
		}
		String token = RandomUtil.uu16();
		redisSsoService.save(getSsoType(), token, u.getId(), rememberme);
		return token;
	}

	@Override
	public void logout(final String token) {
		redisSsoService.rm(getSsoType(), token);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param loginName
	 *            登录名
	 * @return 用户信息，不存在则抛出异常
	 */
	public abstract SsoUser getUser(final String loginName);

}
