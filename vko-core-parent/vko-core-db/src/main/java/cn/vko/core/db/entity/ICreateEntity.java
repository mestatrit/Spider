/**
 * ICreateEntity.java
 * cn.vko.webbmp.db.interfaces
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.entity;

import java.sql.Timestamp;

/**
 * 新添的实体父类
 * 
 * @author 庄君祥
 * @Date 2013-12-6
 */
public interface ICreateEntity {
	/**
	 * 获取创建人主键
	 * 
	 * @return 创建人主键
	 */
	public long getCrId();

	/**
	 * 设置创建人主键
	 * 
	 * @param crId
	 *            创建人主键
	 */
	public void setCrId(final long crId);

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public Timestamp getCrTime();

	/**
	 * 设置创建时间
	 * 
	 * @param crTime
	 *            创建时间
	 */
	public void setCrTime(final Timestamp crTime);

	/**
	 * 获取创建人姓名
	 * 
	 * @return 创建人姓名
	 */
	public String getCrName();

	/**
	 * 设置创建人姓名
	 * 
	 * @param crName
	 *            创建人姓名
	 */
	public void setCrName(final String crName);
}
