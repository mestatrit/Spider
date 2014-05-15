/**
 * UserService.java
 * cn.vko.webbmp.service.auth
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.service;

import static cn.vko.core.common.util.ExceptionUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;
import cn.vko.core.web.actionfilter.support.AuthUtil;
import cn.vko.core.web.actionfilter.support.AuthUtil.Scope;
import cn.vko.web.manage.service.entity.BmpRole;
import cn.vko.web.manage.service.entity.BmpUser;

/**
 * 用户信息维护服务
 * 
 * @author 刘一卓
 * @author 庄君祥
 * @author 赵立伟
 * @Date 2013-9-17
 */
@IocBean
public class UserService {
	private static final long MANAGER_ROLE_ID = 34210282106729L;
	@Inject
	private IDbDao dbDao;

	/**
	 * 修改用户密码
	 */
	public void modifyPassword(final String loginName, final String newPwd,
			final String oldPwd) {
		BmpUser user = dbDao.fetch(BmpUser.class,
				Cnd.where("loginName", "=", loginName).and("valid", "=", true));
		if (user.getId() != AuthUtil.getUserIdEx(Scope.SESSION)) {
			throw bEx("您不能修改别人的密码");
		}
		checkNull(user, "没有对应的{0}的用户信息", loginName);
		user.changePwd(dbDao, oldPwd, newPwd);
	}

	/**
	 * 增加角色至用户
	 */
	public void giveRoleToUser(final long userId, final long roleId) {
		List<Long> roleIds = getRoleIdsInUser(userId);
		if (roleIds.contains(roleId)) {
			return;
		}

		BmpRole role = dbDao.fetch(BmpRole.class, roleId);
		if (!role.isValid()) {
			throw bEx("当前角色被禁用：" + role.getName());
		}

		Sql insertSql = Sqls.create("insert_user_role_rel");
		insertSql.params().set("userId", userId).set("roleId", roleId);
		dbDao.excute(insertSql);
	}

	/**
	 * 查看用户拥有哪些角色
	 */
	public List<Long> getRoleIdsInUser(final long userId) {

		List<Long> roleIds = new ArrayList<Long>();
		Sql sql = Sqls.create("select_user_role_rel_by_user");
		sql.params().set("userId", userId);
		List<Record> records = DbSqlUtil.query(dbDao, sql, null, null);
		for (Record record : records) {
			roleIds.add((Long) record.get("roleid"));
		}

		return roleIds;
	}

	/**
	 * 获取有效的用户对象
	 * 
	 * @param userId
	 *            用户主键
	 * @return 用户对象信息
	 */
	public BmpUser fetchValid(final long userId) {
		return dbDao.fetch(BmpUser.class,
				Cnd.where("id", "=", userId).and("valid", "=", true));
	}

	/**
	 * 是否为管理员
	 * 
	 * @param userId
	 *            用户主键
	 * @return 是否为管理员
	 */
	public boolean isManager(final long userId) {
		List<Long> roleIds = getRoleIdsInUser(userId);
		if (Util.isEmpty(roleIds)) {
			return false;
		}
		return roleIds.contains(MANAGER_ROLE_ID);

	}
}
