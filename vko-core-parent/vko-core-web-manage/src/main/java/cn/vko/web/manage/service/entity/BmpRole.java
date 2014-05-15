/**
 * Role.java
 * cn.vko.webbmp.entity
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import cn.vko.core.db.entity.IValidEntity;
import cn.vko.core.db.entity.impl.OperateEntity;
import cn.vko.core.web.form.support.Condition;

/**
 * 角色POJO
 * 
 * @author 刘一卓
 * @author 庄君祥
 * @Date 2013-9-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("bmp_role")
public class BmpRole extends OperateEntity implements IValidEntity {
	@Id
	@ColDefine(type = ColType.INT, width = 20)
	@Comment("主键")
	private long id;
	@Column
	@Condition
	@Comment("角色名称")
	@ColDefine(type = ColType.VARCHAR, width = 20)
	private String name;
	@Column
	@Condition
	@Comment("角色描述")
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String description;
	@Column
	@Condition
	@Comment("角色启用状态")
	@ColDefine(type = ColType.BOOLEAN, notNull = true)
	private boolean valid;
}
