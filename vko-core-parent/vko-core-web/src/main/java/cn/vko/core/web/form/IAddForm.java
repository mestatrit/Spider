/**
 * IAddForm.java
 * cn.vko.form
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.form;

import cn.vko.core.db.dao.IDbDao;

/**
 * AddForm的接口
 * <p>
 * 标识位
 * <p>
 * 具有add行为
 * 
 * @author 庄君祥
 * @Date 2012-5-10
 */
public interface IAddForm {
	public void check(final IDbDao dbDao);
}
