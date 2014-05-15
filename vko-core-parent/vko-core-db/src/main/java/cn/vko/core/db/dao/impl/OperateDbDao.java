/**
 * OperateDbDao.java
 * cn.vko.core.db.dao.impl
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.dao.impl;

import static cn.vko.core.common.util.ExceptionUtil.*;
import static cn.vko.core.common.util.Util.*;

import java.util.Map;
import java.util.Set;

import org.nutz.dao.impl.NutDao;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Mirror;

import cn.vko.core.common.util.ArrayUtil;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.common.util.DateTimeUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IIdGen;
import cn.vko.core.db.dao.impl.support.IFetchUser;
import cn.vko.core.db.entity.ICreateEntity;
import cn.vko.core.db.entity.IModifyEntity;

/**
 * 针对当前用户做了薄封装的数据库持久
 * 
 * @author 庄君祥
 * @Date 2013-12-6
 * @see ICreateEntity
 * @see IModifyEntity
 */
public class OperateDbDao extends DbDao {

	/**
	 * 获取用户信息
	 */
	private IFetchUser fetchUser;

	public OperateDbDao(final NutDao nutDao, final IIdGen idGen,
			final IFetchUser fetchUser) {
		super(nutDao, idGen);
		this.fetchUser = fetchUser;
	}

	/**
	 * 插入对象
	 * <p>
	 * 不支持map,其他功能参见DbDao@insert
	 * <p>
	 * 如果crId,crName,crTime有值，则不更新，没有值的话，自动更新
	 * 
	 * @see cn.vko.core.db.dao.impl.DbDao#insert(java.lang.Object)
	 */
	@Override
	public <T> T insert(final T obj) {
		checkEmpty(obj, "插入的数据不为空");
		if (obj instanceof Map<?, ?>) {
			throw pEx("暂时不支持map插入");
		}
		setCreateInfos(obj);
		return super.insert(obj);

	}

	/**
	 * 设置创建信息
	 * 
	 * @param obj
	 *            对象
	 */
	private <T> void setCreateInfos(final T obj) {
		Lang.each(obj, new Each<T>() {
			@Override
			public void invoke(final int index, final T ele, final int length)
					throws ExitLoop, ContinueLoop, LoopException {
				if (ele instanceof ICreateEntity) {
					ICreateEntity entity = (ICreateEntity) ele;
					setUserId(entity, "crId");
					setColumn(entity, "crTime", DateTimeUtil.nowDateTime());
					setColumn(entity, "crName", getFetchUser()
							.getCurrentUserName());
				}
			}

		});
	}

	/**
	 * 设置属性
	 * <p>
	 * 如果属性为空，则设置值，如果不为空则不设置
	 * 
	 * @param entity
	 *            对象
	 * @param columnName
	 *            属性名称
	 */
	void setColumn(final Object entity, final String columnName,
			final Object value) {
		Mirror<?> me = Mirror.me(entity);
		Object obj = me.getValue(entity, columnName);
		if (Util.isEmpty(obj)) {
			me.setValue(entity, columnName, value);
		}
	}

	/**
	 * 设置主键
	 * <p>
	 * 如果用户主键存在则不插入，如果用户主键不存在则获取当前用户
	 * 
	 * @param entity
	 *            对象
	 * @param userIdName
	 *            用户主键名称
	 */
	void setUserId(final Object entity, final String userIdName) {
		Mirror<?> me = Mirror.me(entity);
		Object obj = me.getValue(entity, userIdName);
		int userId = ConvertUtil.obj2int(obj);
		if (userId <= 0) {
			me.setValue(entity, userIdName, getFetchUser().getCurrentUserId());
		}
	}

	/**
	 * 获取获取当前用户信息的对象
	 * 
	 * @return 获取当前用户信息的对象
	 */
	public IFetchUser getFetchUser() {
		return fetchUser;
	}

	/**
	 * 设置更新信息
	 * 
	 * @param obj
	 *            对象
	 */
	private <T> void setUpdateInfos(final T obj) {
		Lang.each(obj, new Each<T>() {
			@Override
			public void invoke(final int index, final T ele, final int length)
					throws ExitLoop, ContinueLoop, LoopException {
				if (ele instanceof IModifyEntity) {
					IModifyEntity entity = (IModifyEntity) ele;
					setUserId(entity, "mdId");
					setColumn(entity, "mdTime", DateTimeUtil.nowDateTime());
					setColumn(entity, "mdName", getFetchUser()
							.getCurrentUserName());
				}
			}
		});
	}

	/**
	 * 更新对象
	 * <p>
	 * 支持更新部分字段
	 * <p>
	 * 暂时不支持map更新，最多的参见DbDao@update
	 * <p>
	 * 如果mdId,mdName,mdTime有值，则不更新，没有值的话，自动更新
	 * 
	 * @param obj
	 *            对象
	 * @param cols
	 *            需要更新的字段名集合。缺省，则更新整个对象
	 * @return 执行成功的条数
	 * @see DbDao#update(Object, String...)
	 */
	@Override
	public int update(final Object obj, final String... cols) {
		checkEmpty(obj, "插入的数据不为空");
		checkIsMap(obj);
		setUpdateInfos(obj);
		if (isEmpty(cols)) {
			return super.update(obj);
		}
		Set<String> colsList = ArrayUtil.array2Set(cols);
		if (obj instanceof IModifyEntity) {
			colsList.add("mdId");
			colsList.add("mdTime");
			colsList.add("mdName");
		}
		return super.update(obj, CollectionUtil.collection2array(colsList));
	}

	private void checkIsMap(final Object obj) {
		if (obj instanceof Map<?, ?>) {
			throw pEx("暂时不支持map插入");
		}
	}
}
