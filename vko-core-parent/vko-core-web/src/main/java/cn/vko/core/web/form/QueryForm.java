/**
 * QueryForm.java
 * cn.vko.form
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.form;

import java.lang.reflect.Field;

import org.nutz.dao.Cnd;
import org.nutz.dao.util.cri.Exps;
import org.nutz.dao.util.cri.SqlExpression;
import org.nutz.lang.Mirror;

import cn.vko.core.common.util.Util;
import cn.vko.core.web.form.support.Condition;
import cn.vko.core.web.form.support.Condition.ConnectType;
import cn.vko.core.web.form.support.Condition.MatchType;

/**
 * 所有queryFrom的超类
 * <p>
 * 快速拼接简单的Cnd
 * 
 * @author 庄君祥
 * @Date 2012-5-5
 */
public class QueryForm implements IQueryForm {

	/**
	 * 根据Condition注解生成Cnd
	 * 
	 * @return 生成的Cnd
	 * @see Cnd
	 */
	@Override
	public Cnd createCnd() {
		return resolveCondition();
	}

	/**
	 * 根据condition注解拼接
	 * 
	 * @see Cnd
	 */
	private Cnd resolveCondition() {
		Cnd cnd = null;
		Field[] fds = this.getClass().getDeclaredFields();
		for (Field fd : fds) {
			if (!fd.isAnnotationPresent(Condition.class)) {
				continue;
			}
			if (isFieldEmpty(fd)) {
				continue;
			}
			Object value = Mirror.me(this).getValue(this, fd);

			Condition condition = fd.getAnnotation(Condition.class);
			MatchType mt = condition.match();
			ConnectType ct = condition.connect();

			SqlExpression sql = null;
			if (mt.toString().equals("like")) {
				sql = Exps.create(fd.getName(), mt.toString(), "%" + value
						+ "%");
			} else {
				sql = Exps.create(fd.getName(), mt.toString(), value);
			}
			if (cnd == null) {
				cnd = Cnd.where(sql);
				continue;
			}

			if (ct.name().equals("AND")) {
				cnd.and(sql);
			} else {
				cnd.or(sql);
			}
		}
		return cnd;
	}

	/**
	 * 判断属性是否为空
	 * <ul>
	 * <li>如果是int或者long时，值为0时，则为empty
	 * <li>对象为null的时候，为empty
	 * </ul>
	 * <p>
	 * 其他的参见Util.vkoIsEmpty()方法
	 * 
	 * @param fd
	 * @return 是否为空
	 */
	private boolean isFieldEmpty(final Field fd) {
		Object value = Mirror.me(this).getValue(this, fd);
		if (value == null) {
			return true;
		}
		if (fd.getType().getName().equals("int")) {
			if ((Integer) value == -1) {
				return true;
			}
			return false;
		}
		if (fd.getType().getName().equals("long")) {
			if ((Long) value == -1l) {
				return true;
			}
			return false;
		}
		return Util.isEmpty(value);
	}
}
