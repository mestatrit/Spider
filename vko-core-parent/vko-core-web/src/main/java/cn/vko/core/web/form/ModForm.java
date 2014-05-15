/**
 * ModForm.java
 * cn.vko.form
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.form;

import javax.validation.constraints.Min;

import lombok.Data;
import cn.vko.core.db.dao.IDbDao;

/**
 * 所有modForm的超类
 * 
 * @author 庄君祥
 * @Date 2012-5-4
 */
@Data
public class ModForm implements IModForm {
	@Min(1)
	private long id;

	@Override
	public void check(final IDbDao dbDao) {

	}
}
