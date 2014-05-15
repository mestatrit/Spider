/**
 * RedisKey.java
 * cn.vko.dao.redis
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.redis.support;

import cn.vko.core.common.enums.IEnum;

/**
 * redis存储的key前缀
 * 
 * @author 彭文杰
 * @author 赵立伟
 * @author 庄君祥
 * @Date 2013-1-26
 */
public enum RedisKeyPrefix implements IEnum {
	COUNT("cnt", "计数"), EXP_COUNT("expcnt", "会过期的计数"), ENTITY("en", "实体"), TOP(
			"top", "最新"), RANK("rank", "排行"), QUEUE("q", "队列"), QUEUE_BACK(
			"qback", "队列备份"), QM("qm", "队列的消息"), RELATION("rel", "关系"), UNI(
			"uni", "单值的key,value"), HASH("hash", "哈希表"), SET("set", "set"), QUEUE_ERROR(
			"qerror", "错误数据队列"), LIVE("live", "当前");

	private String key;
	private String value;

	private RedisKeyPrefix(final String key, final String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public String sname() {
		return value;
	}
}
