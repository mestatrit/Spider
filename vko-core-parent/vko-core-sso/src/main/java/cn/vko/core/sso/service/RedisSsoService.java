/**
 * RedisSsoService.java
 * cn.vko.sso.service
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.sso.service;

import static cn.vko.core.common.util.CollectionUtil.collection2array;
import static cn.vko.core.common.util.CollectionUtil.list;
import static cn.vko.core.common.util.DateTimeUtil.millis;
import static cn.vko.core.common.util.DateTimeUtil.nowDateTime;
import static cn.vko.core.common.util.ExceptionUtil.bEx;
import static cn.vko.core.common.util.Util.isEmpty;
import static cn.vko.core.redis.util.RedisUtil.del;
import static cn.vko.core.redis.util.RedisUtil.expire;
import static cn.vko.core.redis.util.RedisUtil.expireMin;
import static cn.vko.core.redis.util.RedisUtil.get;
import static cn.vko.core.redis.util.RedisUtil.set;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.vko.core.redis.IRedisDao;
import cn.vko.core.redis.util.RedisLiveUtil;
import cn.vko.core.sso.service.entity.SsoLog;
import cn.vko.core.sso.service.entity.SsoLog.LogType;

/**
 * 使用redis进行sso缓存的服务
 * 
 * @author 彭文杰
 * @Date 2013-11-6
 */
@IocBean
public class RedisSsoService {
	public static final String SSO_KEY = "sso";
	public static final String SSO_LIVE_USER_KEY = "user";
	// 单位是秒，总共1小时
	public static final int SSO_DEFAULT_TIMEOUT = 60 * 60;
	// 单位是秒，总共2个周时间
	public static final int SSO_RM_TIMEOUT = 2 * 7 * 24 * 60 * 60;

	@Inject
	private IRedisDao ssoRedis;
	@Inject
	private SsoLogService ssoLogService;

	/**
	 * 保持token和id对应关系，并设置超时时间
	 * 
	 * @param ssoType
	 *            sso类别
	 * @param token
	 *            唯一标示
	 * @param userId
	 *            用户id
	 * @param rememberme
	 *            是否记住我
	 */
	public void save(final int ssoType, final String token,
			final String userId, final boolean rememberme) {
		set(ssoRedis, getRedisPrefix(SSO_KEY, ssoType), token, userId);
		if (rememberme) {
			expire(ssoRedis, getRedisPrefix(SSO_KEY, ssoType), token,
					SSO_RM_TIMEOUT);
		} else {
			expire(ssoRedis, getRedisPrefix(SSO_KEY, ssoType), token,
					SSO_DEFAULT_TIMEOUT);
		}
		RedisLiveUtil.add(ssoRedis, getRedisPrefix(SSO_LIVE_USER_KEY, ssoType),
				millis(), userId);
		ssoLogService.log(ssoType, LogType.LOGIN, userId);
	}

	/**
	 * 获取存储的key前缀
	 * 
	 * @param ssoType
	 *            sso类型
	 * @return 拼好的前缀
	 */
	private String getRedisPrefix(final String key, final int ssoType) {
		if (isEmpty(ssoType)) {
			throw bEx("单点登录类型为空！");
		}
		return key + ssoType;
	}

	/**
	 * 删除
	 * 
	 * @param ssoType
	 *            sso类别
	 * @param token
	 *            唯一标示
	 */
	public void rm(final int ssoType, final String token) {
		String userId = fetch(ssoType, token);
		if (userId == null) {
			return;
		}
		del(ssoRedis, getRedisPrefix(SSO_KEY, ssoType), token);
		RedisLiveUtil.add(ssoRedis, getRedisPrefix(SSO_LIVE_USER_KEY, ssoType),
				millis(), userId);
		ssoLogService.log(ssoType, LogType.LOGOUT, userId);
	}

	/**
	 * 获取用户id
	 * 
	 * @param ssoType
	 *            sso类别
	 * @param token
	 *            唯一标示
	 * @return 用户id
	 */
	public String fetch(final int ssoType, final String token) {
		return get(ssoRedis, getRedisPrefix(SSO_KEY, ssoType), token);
	}

	/**
	 * 获取用户id并更新超时时间
	 * 
	 * @param ssoType
	 *            sso类别
	 * @param token
	 *            唯一标示
	 * @return 用户id
	 */
	public String fetchRefreshExpire(final int ssoType, final String token) {
		String userId = fetch(ssoType, token);
		if (userId == null) {
			return null;
		}
		if (!RedisLiveUtil.isLive(ssoRedis,
				getRedisPrefix(SSO_LIVE_USER_KEY, ssoType), userId)) {
			ssoLogService.log(ssoType, LogType.LOGIN, userId);
		}
		RedisLiveUtil.add(ssoRedis, getRedisPrefix(SSO_LIVE_USER_KEY, ssoType),
				millis(), userId);
		expireMin(ssoRedis, getRedisPrefix(SSO_KEY, ssoType), token,
				SSO_DEFAULT_TIMEOUT);
		return userId;
	}

	/**
	 * 移除过期的没有反映的用户 每隔5分钟执行一次
	 */
	public void rmOutDate(final int ssoType) {
		if (ssoType <= 0) {
			return;
		}
		Set<String> outDates = RedisLiveUtil.outDate(ssoRedis,
				getRedisPrefix(SSO_LIVE_USER_KEY, ssoType), 0, millis()
						- SSO_DEFAULT_TIMEOUT * 1000);
		if (isEmpty(outDates)) {
			return;
		}
		RedisLiveUtil.rm(ssoRedis, getRedisPrefix(SSO_LIVE_USER_KEY, ssoType),
				collection2array(outDates));
		Timestamp tt = nowDateTime();
		List<SsoLog> sls = list();
		for (String outDate : outDates) {
			SsoLog sl = new SsoLog();
			sl.setUserId(outDate);
			sl.setLogType(LogType.TIMEOUT.getKeyInt());
			sl.setSsoType(ssoType);
			sl.setLogTime(tt);
			sls.add(sl);
		}
		ssoLogService.batch(sls);
	}
}
