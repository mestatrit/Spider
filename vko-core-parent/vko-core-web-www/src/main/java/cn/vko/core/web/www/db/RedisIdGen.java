/**
 * RedisIdGen.java
 * cn.vko.core.web.www
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.www.db;

import static cn.vko.core.common.util.ExceptionUtil.*;
import static cn.vko.core.redis.support.RedisTransController.*;
import lombok.Data;
import redis.clients.jedis.exceptions.JedisException;
import cn.vko.core.db.dao.IIdGen;
import cn.vko.core.redis.IRedisDao;
import cn.vko.core.redis.util.RedisCountUtil;

/**
 * 使用redis唯一主键生成策略
 * 
 * @author 彭文杰
 * @Date 2013-1-26
 */
@Data
public class RedisIdGen implements IIdGen {
	private static final String IDKEY = "unique_import";
	private IRedisDao dao;

	@Override
	public long getId() {
		try {
			beginNew();
			return RedisCountUtil.incr(dao, IDKEY);
		} catch (JedisException e) {
			closeWithEx();
			throw pEx("读取全局唯一主键失败！", e);
		} finally {
			close();
		}
	}
}
