/**
 * IModifyEntity.java
 * cn.vko.webbmp.db.interfaces
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.entity;

import java.sql.Timestamp;

/**
 * 修改的实体父类 TODO 获取修改人姓名这两个接口，我比较纠结
 * <p>
 * 1、这个姓名是否指的是修改人的唯一标识：比如前台的nickName,后台的realName 2、允许不允许
 * 修改，如果允许修改的话，这两个接口只是记录历史标识
 * 
 * @author 庄君祥
 * @Date 2013-12-6
 */
public interface IModifyEntity {
	/**
	 * 获取修改人主键
	 * 
	 * @return 修改人主键
	 */
	public long getMdId();

	/**
	 * 设置修改人主键
	 * 
	 * @param mdId
	 *            修改人主键
	 */
	public void setMdId(final long mdId);

	/**
	 * 获取修改时间
	 * 
	 * @return 修改时间
	 */
	public String getMdName();

	/**
	 * 设置修改时间
	 * 
	 * @param mdTime
	 *            修改时间
	 */
	public void setMdName(final String mdName);

	/**
	 * 获取修改人姓名
	 * 
	 * @return 修改人姓名
	 */
	public Timestamp getMdTime();

	/**
	 * 设置修改人姓名
	 * 
	 * @param mdName
	 *            修改人姓名
	 */
	public void setMdTime(final Timestamp mdTime);
}
