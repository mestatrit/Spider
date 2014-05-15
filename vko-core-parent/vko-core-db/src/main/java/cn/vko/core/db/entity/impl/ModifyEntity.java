/**
 * AbstractEntity.java
 * cn.vko.webbmp.entity
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.entity.impl;

import java.sql.Timestamp;

import lombok.Data;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;

import cn.vko.core.db.entity.IModifyEntity;

/**
 * 修改时需要记录实体的父类
 * 
 * @author 庄君祥
 * @Date 2013-9-23
 */
@Data
public class ModifyEntity implements IModifyEntity {

	@Column
	@Comment("修改者主键")
	@ColDefine(type = ColType.INT, width = 20)
	private long mdId;
	@Column
	@Comment("修改者名称")
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String mdName;
	@Column
	@Comment("修改时间")
	@ColDefine(type = ColType.DATETIME)
	private Timestamp mdTime;
}
