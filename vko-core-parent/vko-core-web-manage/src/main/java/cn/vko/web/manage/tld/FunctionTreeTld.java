/**
 * FunctionTreeTld.java
 * cn.vko.webbmp.tld
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.tld;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.core.common.util.Util;
import cn.vko.core.db.tree.ITree;
import cn.vko.web.manage.service.entity.BmpMenu;

/**
 * 根据权限生成系统菜单TLD
 * 
 * @author 刘一卓
 * @author 庄君祥
 * @Date 2013-10-9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FunctionTreeTld extends SimpleTagSupport {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(FunctionTreeTld.class);

	@SuppressWarnings("rawtypes")
	private List<ITree> items;
	private boolean manage;

	@Override
	public void doTag() throws JspException, IOException {
		if (items == null || items.size() == 0) {
			logger.info("items 不能为空");
			return;
		}
		JspWriter out = getJspContext().getOut();
		StringBuilder outSb = new StringBuilder();
		createTree(items, outSb);
		out.print(outSb.toString());// 输出
		super.doTag();
	}

	@SuppressWarnings("rawtypes")
	private void createTree(final List<ITree> nodeList,
			final StringBuilder outSb) {
		if (nodeList == null || nodeList.size() == 0) {
			return;
		}
		Iterator<ITree> it = nodeList.iterator();
		while (it.hasNext()) {
			BmpMenu auth = (BmpMenu) it.next();
			if (auth.getParentId() == 0l) {
				outSb.append(" <li>");
				outSb.append(" <a icon='table'>");
				outSb.append(auth.getTName());
				outSb.append(" </a>");
				createSubNode(outSb, auth);
				outSb.append(" </li>");
			} else {
				outSb.append(" <li>");
				if (!auth.isLeaf()) {
					outSb.append(" <a>").append(auth.getTName()).append("</a>");
				} else {
					outSb.append(" <a href='")
							.append(auth.getUrl())
							.append(".html")
							.append("' rel='")
							.append(auth.getUrl().length() > 0 ? auth.getUrl()
									.replace('/', '.').substring(1) : "")
							.append("' target='navTab'>")
							.append(auth.getTName()).append("</a>");
				}
				createSubNode(outSb, auth);
				outSb.append(" </li>");
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createSubNode(final StringBuilder outSb, final ITree auth) {
		if (Util.isEmpty(auth.getSubs())) {
			return;
		}
		outSb.append(" <ul>");
		createTree(auth.getSubs(), outSb);
		outSb.append(" </ul>");
	}
}
