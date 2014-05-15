/**
 * ILoginService.java
 * cn.vko.websso.service
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.service;

/**
 * 登录的服务接口
 * 
 * @author 彭文杰
 * @Date 2013-10-18
 */
public interface ILoginService {
	/**
	 * 登录
	 * 
	 * @param loginName
	 *            登录名
	 * @param password
	 *            密码
	 * @return 唯一token 如果存在问题则抛出异常
	 */
	public String login(final String loginName, final String password,
			final boolean rememberme);

	/**
	 * 退出
	 * 
	 * @param token
	 *            唯一标示key
	 */
	public void logout(final String token);

	/**
	 * 获取sso的类别
	 */
	public int getSsoType();
}
