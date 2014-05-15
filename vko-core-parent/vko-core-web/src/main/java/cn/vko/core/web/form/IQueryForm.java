/**
 * IQueryForm.java
 * cn.vko.form
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.form;

import org.nutz.dao.Cnd;

/**
 * QueryForm的接口
 * <p>
 * 具有查询表单行为
 * 
 * @author 庄君祥
 * @Date 2012-5-10
 */
public interface IQueryForm {
	public Cnd createCnd();
}
