/**
 * FunctionTreeTld.java
 * cn.vko.authority.tld
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.tld;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cn.vko.core.common.util.StringUtil;

/**
 * 分页标签
 * 
 * @author 庄君祥
 * @Date 2013-9-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ToggleTableTld extends SimpleTagSupport {
	private static final String FMT = "<div id=\"{0}\" class=\"uintBox\" url=\"{1}\" style=\"width:49%;float:left;\"></div>";
	private String url;

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		createHTML(out);
		super.doTag();
	}

	private void createHTML(final JspWriter out) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(createOne(false, 0)).append(createOne(true, 1));
		out.print(sb.toString());// 输出结尾
	}

	private String createOne(final boolean inside, final int step) {
		String divId = "uintBox" + (System.currentTimeMillis() + step);
		StringBuilder sb = new StringBuilder(url);
		sb.append(url.indexOf("?") == -1 ? '?' : '&');
		sb.append("inside=").append(inside);
		sb.append("&rel=").append(divId);
		return StringUtil.format(FMT, divId, sb.toString());
	}
}
