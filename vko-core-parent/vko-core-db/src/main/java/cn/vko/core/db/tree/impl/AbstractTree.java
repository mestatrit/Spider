/**
 * AbstractTree.java
 * cn.vko.dao.tree.impl
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.tree.impl;

/**
 * tree的底层封装
 * @author   彭文杰
 * @Date	 2013-11-19 	 
 */
import java.util.Collections;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.tree.ITree;

@Data
@EqualsAndHashCode(exclude = { "subs" })
@ToString(exclude = { "subs" })
public abstract class AbstractTree implements ITree<AbstractTree> {

	private boolean checked;
	protected List<AbstractTree> subs;

	@Override
	public int compareTo(AbstractTree o) {
		if (o == null) {
			return 1;
		}
		if (o.getOrderNum() > this.getOrderNum()) {
			return -1;
		}
		if (o.getOrderNum() < this.getOrderNum()) {
			return 1;
		}
		if (o.getId() > this.getId()) {
			return -1;
		}
		if (o.getId() < this.getId()) {
			return 1;
		}
		return 0;
	}

	@Override
	public AbstractTree addSub(AbstractTree sub) {
		if (subs == null) {
			subs = CollectionUtil.list();
		}
		subs.add(sub);
		Collections.sort(subs);
		return this;
	}

	@Override
	public void clearSub() {
		subs = CollectionUtil.list();
	}

	@Override
	public boolean isLeaf() {
		return Util.isEmpty(subs);
	}
}
