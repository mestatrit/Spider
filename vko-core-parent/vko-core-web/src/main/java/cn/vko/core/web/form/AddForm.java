/**
 * AddForm.java
 * cn.vko.web.form
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.form;

import cn.vko.core.db.dao.IDbDao;

/**
 * 默认的新增form，提供空的check方法
 * 
 * @author 彭文杰
 * @Date 2013-10-11
 */
public class AddForm implements IAddForm {

	@Override
	public void check(final IDbDao dbDao) {

	}

}
