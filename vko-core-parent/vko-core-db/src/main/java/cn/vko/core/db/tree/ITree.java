/**
 * ITree.java
 * cn.vko.dao
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.tree;

import java.util.List;

/**
 * 新版树的接口
 * 
 * @author 彭文杰
 * @Date 2013-11-19
 */
public interface ITree<T> extends Cloneable, Comparable<T> {
	/**
	 * 获取父节点id
	 * 
	 * @return 父节点id
	 */
	public long getParentId();

	/**
	 * 获取当前id
	 * 
	 * @return 当前对象id
	 */
	public long getId();

	/**
	 * 获取显示在树控件的名称
	 * 
	 * @return 名称
	 */
	public String getTName();

	/**
	 * 获取显示在树控件值
	 * 
	 * @return 值
	 */
	public String getTValue();

	/**
	 * 获取显示在树控件是否勾选
	 * 
	 * @return 是否勾选
	 */
	public boolean isChecked();

	/**
	 * 获取顺序号
	 * 
	 * @return 顺序号
	 */
	public int getOrderNum();

	/**
	 * 获取子列表
	 * 
	 * @return 当前对象的子列表
	 */
	public List<T> getSubs();

	/**
	 * 新增子对象
	 * 
	 * @param sub
	 *            子对象
	 * @return 当前对象
	 */
	public T addSub(final T sub);

	/**
	 * 判断是否是第一层
	 * 
	 * @return 是否是第一层
	 */
	public boolean isFirst();

	/**
	 * 清空所有子节点
	 */
	public void clearSub();

	/**
	 * 是否叶子节点
	 * 
	 * @return
	 */
	public boolean isLeaf();
}
