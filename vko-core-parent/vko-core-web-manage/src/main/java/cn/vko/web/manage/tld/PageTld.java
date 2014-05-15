/**
 * FunctionTreeTld.java
 * cn.vko.authority.tld
 * Copyright (c) 2012, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.web.manage.tld;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.dao.pager.Pager;

import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.IocUtil;
import cn.vko.core.common.util.MapUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.web.util.FtlUtil;
import freemarker.template.Configuration;

/**
 * 分页标签
 * 
 * @author 庄君祥
 * @Date 2013-9-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageTld extends SimpleTagSupport {

	private boolean isDialog = false;
	private int begin = 10;
	private int end = 40;
	private int step = 10;
	private int pageNumShown = 10;
	private Pager pager;
	private String rel;

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		createTree(out);
		super.doTag();
	}

	private void createTree(final JspWriter out) throws IOException {
		Map<String, Object> map = MapUtil.map();
		map.put("isDialog", isDialog);
		List<Integer> list = CollectionUtil.list();
		for (int i = begin; i <= end; i += step) {
			list.add(i);
		}
		map.put("list", list);
		map.put("pageNumShown", pageNumShown);
		map.put("pager", pager);
		map.put("rel", rel);
		Configuration conf = IocUtil.get(Configuration.class, "ftlConfig");
		String content = FtlUtil.build(conf, "page.ftl", map);
		if (Util.isEmpty(content)) {
			return;
		}
		out.print(content);// 输出结尾
	}
}
