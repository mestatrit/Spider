/**
 * BmpMenu.java
 * cn.vko.webbmp.service.auth.entity
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Cnd;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.IPreDelete;
import cn.vko.core.db.entity.impl.TreeEntity;
import cn.vko.core.db.util.DbExtendUtil;

/**
 * 功能菜单
 * 
 * @author 庄君祥
 * @Date 2013-10-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("bmp_menu")
@Comment("功能菜单")
public class BmpMenu extends TreeEntity implements IPreDelete {
	@Id
	@ColDefine(type = ColType.INT, width = 20)
	@Comment("主键")
	private long id;
	@Column
	@Comment("权限功能URL")
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String url;
	@Column
	@Comment("描述")
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String description;
	@Column
	@Comment("是否为叶子节点")
	@ColDefine(type = ColType.BOOLEAN, notNull = true)
	private boolean leaf;

	@Override
	public void preDelete(final IDbDao dao) {
		if (DbExtendUtil.count(dao, BmpMenu.class,
				Cnd.where("parentId", "=", id)) > 0) {
			throw ExceptionUtil.bEx("还存在子节点，不可以删除");
		}
	}

	@Override
	public String getTValue() {
		return String.valueOf(id);
	}
}
