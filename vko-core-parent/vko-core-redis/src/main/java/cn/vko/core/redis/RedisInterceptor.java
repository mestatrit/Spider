/**
 * RedisInterceptor.java
 * cn.vko.dao.redis
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis;

import static cn.vko.core.redis.support.RedisTransController.*;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.interceptor.AbstractMethodInterceptor;

import redis.clients.jedis.exceptions.JedisException;

/**
 * Redis拦截器
 * 
 * @author 彭文杰
 * @author 庄君祥
 * @author 赵立伟
 * @Date 2013-1-22
 */
public class RedisInterceptor extends AbstractMethodInterceptor {
	@Override
	public void filter(final InterceptorChain chain) throws Throwable {
		if (!isNoTransaction()) {
			chain.doChain();
			return;
		}
		try {
			begin();
			chain.doChain();
		} catch (JedisException e) {
			closeWithEx();
		} finally {
			close();
		}
	}
}
