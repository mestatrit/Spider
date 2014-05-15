/**
 * ViewTreeForm.java
 * cn.vko.form
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.form;

import lombok.Data;

/**
 * 树的viewform
 * 
 * @author 徐世超
 * @Date 2012-5-17
 */
@Data
public class ViewTreeForm implements IViewForm {
	private long parentId;
	private String parentName;
	private String name;
	protected int orderNum;
}
