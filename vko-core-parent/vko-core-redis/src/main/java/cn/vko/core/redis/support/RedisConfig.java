/**
 * JedisConfig.java
 * cn.vko.dao.redis
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.support;

import lombok.Data;

/**
 * jedis相关配置
 * 
 * @author 彭文杰
 * @author 庄君祥
 * @author 赵立伟
 * @Date 2013-1-22
 */
@Data
public class RedisConfig {
	/**
	 * ip
	 */
	private String host;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * 超时时间
	 */
	private int timeout;

	public RedisConfig(final String host, final int port, final int timeout) {
		super();
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}
}
