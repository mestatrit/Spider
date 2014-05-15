/**
 * AddTreeForm.java
 * cn.vko.form
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.form;

import javax.validation.constraints.Min;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 树的新增From超类
 * 
 * @author 徐世超
 * @Date 2012-5-9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddTreeForm extends AddForm {
	/**
	 * 父节点的id
	 */
	private long parentId;
	/**
	 * 名称
	 */
	@NotEmpty
	private String name;
	/**
	 * 顺序号
	 */
	@Min(value = 1)
	protected int orderNum;

}
