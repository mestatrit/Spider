/**
 * PageTag.java
 * cn.vko.core.web.www.tld
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.www.tld;

import javax.servlet.jsp.tagext.TagSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cn.vko.core.common.util.Util;

/**
 * 分页标签
 * 
 * @author 彭文杰
 * @Date 2013-12-24
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
public class PageTag extends TagSupport {
	public static final String DEFAULT_CSSNAME = "vkopage";
	/**
	 * 页面元素条数
	 */
	private int pageSize = 10;
	/**
	 * 当前页数
	 */
	private int pageNum = 0;
	/**
	 * 总条数
	 */
	private int total = 0;
	/**
	 * 页面链接
	 */
	private String link;
	/**
	 * 页面样式,可以不填
	 */
	private String cssName;

	/**
	 * 获取拼到div上的css
	 * 
	 * @return css样式
	 */
	protected String getCss() {
		if (Util.isEmpty(cssName)) {
			return DEFAULT_CSSNAME;
		}
		return DEFAULT_CSSNAME + " " + cssName;
	}

}
