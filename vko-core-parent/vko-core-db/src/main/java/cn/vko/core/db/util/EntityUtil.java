/**
 * AnnotationUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.util;

import static cn.vko.core.common.util.ExceptionUtil.*;

import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.Mirror;

import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.ConvertUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.db.dao.IDbDao;

/**
 * 实体相关的工具类
 * <p>
 * 根据类取得数据库表名 创建实体等
 * 
 * @author 庄君祥
 * @author 赵立伟
 * @Date 2013-4-9
 * @version 5.1.0
 */
public class EntityUtil {

	/**
	 * 根据Table注解获取数据表名
	 * <p>
	 * 如果没有写的默认为：首字母小写，其他的大写字母用下划线替代
	 * 
	 * @param clazz
	 *            类
	 * @return 对应的数据库表名
	 * @exception DataException
	 *                参数为null
	 */
	public static String getTableName(final Class<?> clazz) {
		checkNull(clazz, "类型不能为空");
		if (!clazz.isAnnotationPresent(Table.class)) {
			return eName2TName(clazz.getName());
		}
		return clazz.getAnnotation(Table.class).value();
	}

	/**
	 * 根据实体名自动转成表名
	 * <p>
	 * 有大写的转成_拼上小写 如：
	 * 
	 * <pre>
	 * RoleUser-- &gt; role_user
	 * </pre>
	 * 
	 * @param entityName
	 * @return 根据实体名自动转成表名
	 */
	public static String eName2TName(final String entityName) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (char c : entityName.toCharArray()) {
			if (isFirst) {
				sb.append(Character.toLowerCase(c));
				isFirst = false;
				continue;
			}
			if (Character.isUpperCase(c)) {
				sb.append("_").append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 根据类型获取实体的对象
	 * 
	 * @param clazz
	 *            类型
	 * @return 实体对象
	 */
	public static final <T> T newInstance(final Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Throwable e) {
			throw pEx("创建泛型对象失败，请检查", e);
		}
	}

	/**
	 * 判断某列是否存在相同的数据
	 * 
	 * @param dao
	 *            数据库
	 * @param clazz
	 *            实体
	 * @param id
	 *            自己的id
	 * @param col
	 *            列名
	 * @param value
	 *            值
	 * @return 是否存在相同的数据
	 */
	public static <T> boolean checkSame(final IDbDao dao, final Class<T> clazz,
			final long id, final String col, final Object value) {
		checkNull(dao, "数据库链接为空!");
		checkNull(clazz, "实体对象为空!");
		checkEmpty(col, "列名为空!");
		int same = DbExtendUtil.count(dao, clazz,
				Cnd.where("id", "<>", id).and(col, "=", value));
		if (same > 0) {
			return true;
		}
		return false;
	}

	public static <T> List<Long> tranIds(final List<T> entities) {
		return tranIds(entities, "id");
	}

	public static <T> List<Long> tranIds(final List<T> entities,
			final String fieldName) {
		if (Util.isEmpty(entities)) {
			return CollectionUtil.list();
		}
		List<Long> ids = CollectionUtil.list();
		for (T entity : entities) {
			long id = ConvertUtil.obj2long(Mirror.me(entity).getValue(entity,
					fieldName));
			if (id <= 0) {
				continue;
			}
			ids.add(id);
		}
		return ids;
	}
}
