/**
 * Authority.java
 * cn.vko.webbmp.entity
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.Sqls;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.sql.Sql;

import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.IPreInsert;
import cn.vko.core.db.entity.IValidEntity;
import cn.vko.core.db.entity.impl.OperateEntity;
import cn.vko.core.db.util.DbSqlUtil;

/**
 * 权限POJO
 * 
 * @author 刘一卓
 * @author 庄君祥
 * @Date 2013-9-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("bmp_authority")
@Comment("权限")
public class BmpAuthority extends OperateEntity implements IValidEntity,
		IPreInsert {
	@Id
	@ColDefine(type = ColType.INT, width = 20)
	@Comment("主键")
	private long id;
	@Column
	@Comment("权限功能key")
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String name;
	@Column
	@Comment("权限功能key")
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String authKey;
	@Column
	@Comment("权限功能URL")
	@ColDefine(type = ColType.VARCHAR, width = 100)
	private String url;
	@Column
	@Comment("状态")
	@ColDefine(type = ColType.BOOLEAN, notNull = true)
	private boolean valid = true;
	@Column
	@Comment("描述")
	@ColDefine(type = ColType.VARCHAR, width = 200)
	private String description;

	private boolean checked;

	/**
	 * (non-Javadoc)
	 * 
	 * @see cn.vko.dao.entity.impl.IdEntity#preInsert(cn.vko.dao.db.DbDao)
	 */
	@Override
	public void preInsert(final IDbDao dao) {
		if (Util.isEmpty(url)) {
			return;
		}
		Sql sql = Sqls
				.create("select count(1) from bmp_authority where url=@url and isEnable=1");
		sql.params().set("url", url);
		if (DbSqlUtil.fetchInt(dao, sql) > 0) {
			throw ExceptionUtil.bEx("当前权限URL:{0}已经被使用，请更换URL", url);
		}
	}

	public boolean eq(final BmpAuthority otherAuth) {
		if (otherAuth == null) {
			return false;
		}
		if (otherAuth == this) {
			return true;
		}
		if (valid != otherAuth.isValid()) {
			return false;
		}
		if (!url.equals(otherAuth.getUrl())) {
			return false;
		}
		if (!name.equals(otherAuth.getName())) {
			return false;
		}
		if (!description.equals(otherAuth.getDescription())) {
			return false;
		}
		return true;

	}
}
