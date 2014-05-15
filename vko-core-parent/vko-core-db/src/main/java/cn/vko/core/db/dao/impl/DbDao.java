/**
 * DbDao.java
 * cn.vko.core.db.dao.impl
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.dao.impl;

import static cn.vko.core.common.util.ExceptionUtil.checkEmpty;
import static cn.vko.core.common.util.ExceptionUtil.checkNull;
import static cn.vko.core.common.util.ExceptionUtil.pEx;
import static cn.vko.core.common.util.Util.isEmpty;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.ContinueLoop;
import org.nutz.lang.Each;
import org.nutz.lang.ExitLoop;
import org.nutz.lang.Lang;
import org.nutz.lang.LoopException;
import org.nutz.lang.Mirror;

import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.common.util.StringUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.IIdGen;
import cn.vko.core.db.dao.IPreDelete;
import cn.vko.core.db.dao.IPreInsert;
import cn.vko.core.db.dao.IPreUpdate;

/**
 * 纯属的基于nut的数据库持久
 * 
 * @author 庄君祥
 * @Date 2013-12-6
 */
public class DbDao implements IDbDao {
	/**
	 * 持有的NutDao
	 */
	protected NutDao nutDao;
	/**
	 * 主键生成策略
	 */
	protected IIdGen idGen;

	public DbDao(final NutDao nutDao) {
		this.nutDao = nutDao;
	}

	public DbDao(final NutDao nutDao, final IIdGen idGen) {
		this.nutDao = nutDao;
		this.idGen = idGen;
	}

	@Override
	public <T> T insert(final T obj) {
		checkEmpty(obj, "插入的数据不为空");
		final DbDao self = this;
		Lang.each(obj, new Each<T>() {
			@Override
			public void invoke(final int index, final T ele, final int length)
					throws ExitLoop, ContinueLoop, LoopException {
				if (ele instanceof IPreInsert && index == 0) {
					((IPreInsert) ele).preInsert(self);
				}
				Mirror<T> me = Mirror.me(ele);
				Field[] fds = me.getFields();
				if (null == fds) {
					return;
				}
				for (Field field : fds) {
					if (field.isAnnotationPresent(Id.class)) {
						Id id = field.getAnnotation(Id.class);
						if (!id.auto()) {
							Object value = me.getValue(ele, field);
							long result = ConvertUtil.obj2long(value);
							if (result > 0) {
								break;
							}
							me.setValue(ele, field, idGen.getId());
						}
						break;
					}
				}
			}
		});
		return nutDao.insert(obj);

	}

	@Override
	public void insert(final String tableName, final Chain chain) {
		checkEmpty(tableName, "tableName不能为空");
		checkNull(chain, "chain不能为空");
		nutDao.insert(tableName, chain);
	}

	@Override
	public void insert(final Class<?> clazz, final Chain chain) {
		checkNull(clazz, "class不能为空");
		checkNull(chain, "chain不能为空");
		nutDao.insert(clazz, chain);
	}

	@Override
	public int update(final Object obj, final String... cols) {
		checkEmpty(obj, "更新的对象不允许为空");
		if (obj instanceof Map<?, ?>) {
			throw pEx("暂时不支持map更新操作");
		}
		if (isEmpty(cols)) {
			return nutDao.update(obj);
		}
		if (Lang.length(obj) == 1 && obj instanceof IPreUpdate) {
			((IPreUpdate) obj).preUpdate(this);
		}
		StringBuilder sb = new StringBuilder("^(").append(
				StringUtil.join("|", cols)).append(")$");
		return nutDao.update(obj, sb.toString());
	}

	@Override
	public int update(final String tableName, final Chain chain,
			final Condition cnd) {
		checkEmpty(tableName, "表名不能为空");
		checkNull(chain, "chain不能为空");
		return nutDao.update(tableName, chain, cnd);

	}

	@Override
	public int update(final Class<?> clazz, final Chain chain,
			final Condition cnd) {
		checkNull(clazz, "类型不能为空");
		checkNull(chain, "chain不能为空");
		return nutDao.update(clazz, chain, cnd);

	}

	@Override
	public int delete(final Object obj) {
		checkEmpty(obj, "要删除的对象不能为空");
		if (Lang.length(obj) == 1 && obj instanceof IPreDelete) {
			((IPreDelete) obj).preDelete(this);
		}
		return nutDao.delete(obj);
	}

	@Override
	public int delete(final Class<?> clazz, final long id) {
		checkNull(clazz, "clazz不能为空");
		return nutDao.delete(clazz, id);
	}

	@Override
	public void excute(final Sql... sqls) {
		checkEmpty(sqls, "sql对象不允许为空");
		nutDao.execute(sqls);
	}

	@Override
	public <T> List<T> query(final Class<T> clazz, final Condition cnd,
			final Pager pager) {
		checkNull(clazz, "查询的类型不为空");
		return nutDao.query(clazz, cnd, pager);
	}

	@Override
	public List<Record> query(final String tableName, final Condition cnd,
			final Pager pager) {
		checkEmpty(tableName, "查询的表不为空");
		return nutDao.query(tableName, cnd, pager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Record> query(final Sql sql, final Condition cnd,
			final Pager pager) {
		checkNull(sql, "sql对象不允许为空");
		if (!sql.isSelect()) {
			throw pEx("执行查询sql时，sql不是select语句！");
		}
		if (null != cnd) {
			sql.setCondition(cnd);
		}
		if (null != pager) {
			sql.setPager(pager);
		}
		sql.setCallback(Sqls.callback.records());
		excute(sql);
		return (List<Record>) sql.getResult();
	}

	@Override
	public <T> T fetch(final Class<T> clazz, final long id) {
		checkNull(clazz, "class不能为空");
		return nutDao.fetch(clazz, id);

	}

	@Override
	public <T> T fetch(final Class<T> clazz, final Condition cnd) {
		checkNull(clazz, "class不能为空");
		return nutDao.fetch(clazz, cnd);

	}

	/**
	 * 根据条件获取一个 Record 对象
	 */
	@Override
	public Record fetch(final String tableName, final long id) {
		checkEmpty(tableName, "tableName不能为空");
		return fetch(tableName, Cnd.where("id", "=", id));
	}

	@Override
	public Record fetch(final String tableName, final Condition cnd) {
		checkEmpty(tableName, "tableName不能为空");
		return nutDao.fetch(tableName, cnd);
	}

	@Override
	public Record fetch(final Sql sql) {
		checkEmpty(sql, "sql不能为空");
		sql.setCallback(Sqls.callback.record());
		excute(sql);
		return sql.getObject(Record.class);
	}
}
