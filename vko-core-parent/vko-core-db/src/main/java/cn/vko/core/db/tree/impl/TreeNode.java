/**
 * TreeNode.java
 * cn.vko.dao.tree.impl
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.tree.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标配的树节点
 * 
 * 从数据库获取树结构时，尽量使用本对象
 * 
 * @author 彭文杰
 * @Date 2013-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeNode extends AbstractTree {
	private long id;
	private long parentId;
	private String name;
	private int orderNum;

	@Override
	public String getTName() {
		return name;
	}

	@Override
	public String getTValue() {
		return String.valueOf(id);
	}

	@Override
	public boolean isFirst() {
		return parentId <= 0;
	}
}
