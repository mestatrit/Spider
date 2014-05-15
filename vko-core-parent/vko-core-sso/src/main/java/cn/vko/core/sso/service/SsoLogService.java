/**
 * SsoLogService.java
 * cn.vko.sso.service
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.service;

import java.util.List;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.sso.service.entity.SsoLog;
import cn.vko.core.sso.service.entity.SsoLog.LogType;

/**
 * 
 * @author 彭文杰
 * @Date 2013-11-25
 */
@IocBean
public class SsoLogService {
	@Inject
	private IDbDao ssoDao;

	/**
	 * 记录sso相关日志
	 * 
	 * @param ssoType
	 *            sso类别
	 * @param logType
	 *            记录类别
	 * @param userId
	 *            用户id
	 */
	public SsoLog log(final int ssoType, final LogType logType,
			final String userId) {
		SsoLog sl = new SsoLog();
		sl.setLogType(logType.getKeyInt());
		sl.setSsoType(ssoType);
		sl.setUserId(userId);
		ssoDao.insert(sl);
		return sl;
	}

	/**
	 * 记录sso相关日志
	 * 
	 * @param ssoType
	 *            sso类别
	 * @param logType
	 *            记录类别
	 * @param userId
	 *            用户id
	 */
	public void batch(final List<SsoLog> ssoLogs) {
		ssoDao.insert(ssoLogs);
	}
}
