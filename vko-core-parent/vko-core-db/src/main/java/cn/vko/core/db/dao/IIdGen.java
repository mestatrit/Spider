/**
 * IIdGen.java
 * cn.vko.core.db.dao
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.dao;

/**
 * 主键生成策略
 * 
 * @author 庄君祥
 * @Date 2013-12-7
 */
public interface IIdGen {
	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId();
}
