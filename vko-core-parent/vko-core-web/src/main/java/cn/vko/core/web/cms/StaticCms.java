/**
 * StaticCms.java
 * cn.vko.core.web.cms
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.cms;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.ioc.loader.annotation.Inject;

import cn.vko.core.cms.ParseUtil;
import cn.vko.core.cms.interfaces.impl.AbstractCms;

/**
 * cms中静态内容处理类 如果公用的头、footer 处理 <cms:static st="head"/>,会从SevletContext中获取对应的数据
 * 
 * @author 彭文杰
 * @Date 2013-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StaticCms extends AbstractCms {
	// 这样写可以注入
	@Inject("refer:$servlet")
	private ServletContext context;

	@Override
	protected String getType() {
		return "static";
	}

	@Override
	protected void changeMatch(final HttpServletRequest req,
			final Set<String> needDeal, final Map<String, String> result) {
		for (String str : needDeal) {
			Map<String, String> props = ParseUtil.getProps(str);
			String st = props.get("st");
			result.put(str, String.valueOf(context.getAttribute(st)));
		}
	}
}
