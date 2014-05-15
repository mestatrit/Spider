/**
 * IFetchUser.java
 * cn.vko.webbmp.db.interfaces
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.dao.impl.support;

/**
 * 用来取当前用户信息接口
 * 
 * @author 庄君祥
 * @Date 2013-9-24
 */
public interface IFetchUser {
	/**
	 * 获取当前用户id
	 * 
	 * @return 当前用户id
	 */
	public long getCurrentUserId();

	/**
	 * 获取当前用户id
	 * 
	 * @return 当前用户id
	 */
	public long getCurrentUserIdWithEx();

	/**
	 * 获取当前用户名称
	 * <p>
	 * 建议：这里最好是唯一标识，最好是不能修改的
	 * 
	 * @return 当前用户名称
	 */
	public String getCurrentUserName();
}
