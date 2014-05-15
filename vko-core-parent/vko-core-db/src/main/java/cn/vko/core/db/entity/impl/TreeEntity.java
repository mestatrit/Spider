package cn.vko.core.db.entity.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;

import cn.vko.core.db.tree.impl.AbstractTree;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TreeEntity extends AbstractTree {
	@Column
	@ColDefine(type = ColType.INT, width = 20)
	@Comment("父节点id")
	private long parentId;
	@Column
	@ColDefine(type = ColType.VARCHAR, width = 100)
	@Comment("名称")
	private String name;
	@Column
	@ColDefine(type = ColType.INT, width = 5)
	@Comment("顺序号")
	protected int orderNum;

	@Override
	public String getTName() {
		return name;
	}

	@Override
	public boolean isChecked() {
		return false;
	}

	@Override
	public boolean isFirst() {
		return parentId <= 0;
	}
}
