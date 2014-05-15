/**
 * IValidEntity.java
 * cn.vko.core.db.dao
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.entity;

/**
 * 所有假删的数据的需要实现的接口
 * 
 * @author 庄君祥
 * @Date 2013-12-6
 */
public interface IValidEntity {
	/**
	 * 是否有效
	 * 
	 * @return 如果有效返回true，反之返回false
	 */
	public boolean isValid();

	/**
	 * 设置有效值
	 * 
	 * @param valid
	 *            有效值
	 */
	public void setValid(final boolean valid);
}
