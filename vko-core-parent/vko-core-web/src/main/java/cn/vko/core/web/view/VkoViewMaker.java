/**
 * VkoViewMaker.java
 * cn.vko.core.web.view
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.view;

import java.util.regex.Pattern;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

import cn.vko.core.cms.Cms;
import cn.vko.core.common.util.Util;
import cn.vko.core.web.view.interfaces.impl.CmsTransfer;
import freemarker.template.Configuration;

/**
 * 自定义视图
 * 
 * @author 庄君祥
 * @Date Dec 26, 2013
 */
public class VkoViewMaker implements ViewMaker {
	@Override
	public View make(final Ioc ioc, final String type, final String value) {
		View view = fetchView(type, value, ioc);
		if (Util.isEmpty(view)) {
			return null;
		}
		// 添加处理器
		CmsTransfer.me().setCms(ioc.get(Cms.class, "cms")).warpper(type, view);
		return view;
	}

	/**
	 * 　获取视图 　TODO 这个再说吧
	 * 
	 * @param value
	 *            TODO
	 * 
	 * @param type
	 *            　类型
	 * @return 视图
	 */
	private View fetchView(final String type, final String value, final Ioc ioc) {
		Pattern jspPattern = Pattern.compile("\\s*jsp\\s*");
		if (jspPattern.matcher(type).find()) {
			return new JspTransferView(value);
		}
		Pattern jsonPattern = Pattern.compile("\\s*json\\s*");
		if (jsonPattern.matcher(type).find()) {
			return new Utf8JsonTransferView(null);
		}
		Pattern ftlPattern = Pattern.compile("\\s*ftl\\s*");
		if (ftlPattern.matcher(type).find()) {
			return new FtlTransferView(value, ioc.get(Configuration.class,
					"ftlConfig"));
		}
		if ("fail".equals(type)) {
			return new FailView();
		}
		return null;
	}
}
