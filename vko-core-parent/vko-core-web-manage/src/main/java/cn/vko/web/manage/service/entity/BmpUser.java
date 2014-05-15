/**
 * Role.java
 * cn.vko.webbmp.entity
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

import cn.vko.core.common.util.EncryptUtil;
import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.IPreInsert;
import cn.vko.core.db.entity.IValidEntity;
import cn.vko.core.db.entity.impl.OperateEntity;
import cn.vko.core.db.util.DbExtendUtil;
import cn.vko.core.web.form.support.Condition;

/**
 * 用户
 * 
 * @author 刘一卓
 * @author 庄君祥
 * @Date 2013-9-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("bmp_user")
public class BmpUser extends OperateEntity implements IValidEntity, IPreInsert {
	@Id
	@ColDefine(type = ColType.INT, width = 20)
	@Comment("主键")
	private long id;
	@Column
	@Condition
	@Comment("登录名")
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String loginName;
	@Column
	@Condition
	@Comment("密码")
	@ColDefine(type = ColType.VARCHAR, width = 50)
	private String password;
	@Column
	@Condition
	@Comment("真实姓名")
	@ColDefine(type = ColType.VARCHAR, width = 20)
	private String realName;
	@Column
	@Condition
	@Comment("用户启用状态")
	@ColDefine(type = ColType.BOOLEAN, notNull = true)
	private boolean valid;

	public BmpUser() {
		super();
	}

	public BmpUser(final String loginName, final String realName) {
		super();
		this.loginName = loginName;
		this.password = EncryptUtil.md5("999999");
		this.realName = realName;
	}

	@Override
	public void preInsert(final IDbDao dao) {
		if (DbExtendUtil.count(dao, BmpUser.class,
				Cnd.where("loginName", "=", loginName)) > 0) {
			throw ExceptionUtil.bEx("用户名：{0}已经存在，请使用其他用户名", loginName);
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param dbDao
	 *            dao对象
	 * @param orginalPwd
	 *            原始密码
	 * @param newPwd
	 *            新密码
	 */
	public void changePwd(final IDbDao dbDao, final String orginalPwd,
			final String newPwd) {
		if (!password.equals(EncryptUtil.md5(orginalPwd))) {
			throw ExceptionUtil.bEx("原始密码不正确");
		}
		ExceptionUtil.checkEmptyBEx(newPwd, "原始密码不可以为空");
		password = EncryptUtil.md5(newPwd);
		dbDao.update(this, "password");
	}

}
