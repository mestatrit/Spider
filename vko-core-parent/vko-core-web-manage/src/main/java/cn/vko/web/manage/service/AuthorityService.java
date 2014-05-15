/**
 * AbstractAuthoritySerivce.java
 * cn.vko.bmp.service.authority
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.service;

import static cn.vko.core.common.util.CollectionUtil.*;
import static cn.vko.core.common.util.MapUtil.*;
import static cn.vko.core.common.util.Util.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.impl.Loadings;

import cn.vko.core.common.util.EncryptUtil;
import cn.vko.core.common.util.StringUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.util.DbSqlUtil;
import cn.vko.core.web.actionfilter.support.Auth;
import cn.vko.web.manage.service.entity.BmpAuthority;

/**
 * @author 刘一卓
 * @author 庄君祥
 * @Date 2013-9-13
 */
@IocBean
public class AuthorityService {

	@Inject
	private IDbDao dbDao;

	/**
	 * 查询全部有效权限
	 */
	private List<BmpAuthority> queryValidAuthroties() {
		return dbDao.query(BmpAuthority.class, Cnd.where("valid", "=", true),
				null);
	}

	/**
	 * 重置系统的权限
	 * 
	 * @param mainModule
	 *            项目的入口Module
	 */
	@Aop({ "txDb" })
	public void reset(final Class<?> mainModule) {
		if (isEmpty(mainModule)) {
			return;
		}
		Map<String, BmpAuthority> auths = loadAuthMapping(mainModule);
		List<String> dbAuthKeys = queryValidAuthKes();
		saveOrUpdate(auths);
		delete(dbAuthKeys, auths.keySet());
	}

	/**
	 * 查询有效的权限key列表
	 * 
	 * @return 有效的权限key列表
	 */
	private List<String> queryValidAuthKes() {
		Sql sql = Sqls
				.create("select authKey from bmp_authority where valid=@valid");
		sql.params().set("valid", true);
		List<String> dbAuthKeys = DbSqlUtil.queryStringList(dbDao, sql);
		return dbAuthKeys;
	}

	/**
	 * 保存或者更新
	 * 
	 * @param programeAuths
	 *            程序权限
	 */
	private void saveOrUpdate(final Map<String, BmpAuthority> programeAuths) {
		if (isEmpty(programeAuths)) {
			return;
		}
		List<BmpAuthority> insertAuths = list();
		List<BmpAuthority> updateAuths = list();
		Set<String> keySet = programeAuths.keySet();
		List<BmpAuthority> dbAuthorities = queryValidAuthroties();
		for (String key : keySet) {
			BmpAuthority programeAuth = programeAuths.get(key);
			if (isEmpty(programeAuth)) {
				continue;
			}
			BmpAuthority dbAuth = isExists(dbAuthorities, programeAuth);
			if (isEmpty(dbAuth)) {
				insertAuths.add(programeAuth);
				continue;
			}
			if (dbAuth.eq(programeAuth)) {
				continue;
			}
			dbAuth.setName(programeAuth.getName());
			dbAuth.setUrl(programeAuth.getUrl());
			dbAuth.setDescription(programeAuth.getDescription());
			updateAuths.add(dbAuth);
		}
		dbDao.update(updateAuths, "name", "url", "description");
		dbDao.insert(insertAuths);
	}

	public BmpAuthority isExists(final List<BmpAuthority> dbAuthorities,
			final BmpAuthority programeAuth) {
		if (isEmpty(dbAuthorities)) {
			return null;
		}
		for (BmpAuthority dbAuth : dbAuthorities) {
			if (dbAuth.getAuthKey().equals(programeAuth.getAuthKey())) {
				return dbAuth;
			}
		}
		return null;
	}

	/**
	 * 删除程序没有的权限
	 * 
	 * @param dbAuthKeys
	 *            数据库已有的权限
	 * @param programeKeySets
	 *            程序的key集合
	 */
	private void delete(final List<String> dbAuthKeys,
			final Set<String> programeKeySets) {
		if (isEmpty(dbAuthKeys) || isEmpty(programeKeySets)) {
			return;
		}
		List<String> deleteAuthKeys = list();
		for (String key : dbAuthKeys) {
			if (isEmpty(key)) {
				continue;
			}
			if (programeKeySets.contains(key)) {
				continue;
			}
			deleteAuthKeys.add(key);
		}
		if (isEmpty(deleteAuthKeys)) {
			return;
		}
		Sql sql = Sqls
				.create("update bmp_authority set valid=@valid $condition");
		sql.setCondition(Cnd.where("authKey", "in", deleteAuthKeys));
		sql.params().set("valid", false);
		dbDao.excute(sql);
	}

	/**
	 * 加载程序有的权限
	 * 
	 * @param mainModule
	 *            主module
	 * @return map(key:权限key,value:权限对象)
	 */
	private Map<String, BmpAuthority> loadAuthMapping(final Class<?> mainModule) {
		Set<Class<?>> modules = Loadings.scanModules(mainModule);
		if (isEmpty(modules)) {
			return map();
		}
		Map<String, BmpAuthority> auths = map();
		for (Class<?> module : modules) {
			if (isEmpty(module)) {
				continue;
			}
			ActionInfo moduleInfo = Loadings.createInfo(module);
			for (Method method : module.getMethods()) {
				if (isEmpty(method)) {
					continue;
				}
				if (!method.isAnnotationPresent(Auth.class)) {
					continue;
				}
				if (!isAt(method)) {
					continue;
				}
				ActionInfo info = Loadings.createInfo(method).mergeWith(
						moduleInfo);

				BmpAuthority auth = new BmpAuthority();
				Auth vkoAuth = method.getAnnotation(Auth.class);
				auth.setName(vkoAuth.name());
				auth.setDescription(isEmpty(vkoAuth.description()) ? vkoAuth
						.name() : vkoAuth.description());
				auth.setUrl(StringUtil.join(",", info.getPaths()));
				String key = genKey(module.getName(), method.getName());
				auth.setAuthKey(key);
				auths.put(key, auth);
			}
		}
		return auths;
	}

	/**
	 * 生成key
	 * 
	 * @param moduleName
	 *            类名
	 * @param methodName
	 *            方法名
	 * @return 权限唯一key
	 */
	public String genKey(final String moduleName, final String methodName) {
		StringBuilder sb = new StringBuilder(moduleName).append("_").append(
				methodName);
		return EncryptUtil.encrypt(sb.toString());
	}

	/**
	 * 是否为At方法
	 * 
	 * @param method
	 *            方法
	 * @return 如果是返回true,如果不是返回false
	 */
	private boolean isAt(final Method method) {
		return Modifier.isPublic(method.getModifiers())
				&& method.isAnnotationPresent(At.class);
	}
}
