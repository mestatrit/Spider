/**
 * UrlCms.java
 * cn.vko.business.cms
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.cms;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.nutz.ioc.impl.PropertiesProxy;

import cn.vko.core.cms.ParseUtil;
import cn.vko.core.cms.interfaces.impl.AbstractCms;
import cn.vko.core.common.util.StringUtil;
import cn.vko.core.common.util.Util;
import cn.vko.core.web.config.KvConfig;

/**
 * 处理网站内url
 * 
 * @author 彭文杰
 * @Date 2013-12-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WebUrlCms extends AbstractCms {
	public static final Pattern WEB_CONFIG_PATTERN = Pattern
			.compile("\\$(\\S*?)/");
	/**
	 * 路径对应关系
	 */
	private PropertiesProxy urlProp;
	private KvConfig webConfig;

	@Override
	protected String getType() {
		return "url";
	}

	@Override
	protected void changeMatch(final HttpServletRequest req,
			final Set<String> needDeal, final Map<String, String> result) {
		for (String one : needDeal) {
			Map<String, String> props = ParseUtil.getProps(one);
			result.put(one, getUrl(props));
		}
	}

	private String getUrl(final Map<String, String> props) {
		if (!props.containsKey(SUB_TYPE)) {
			return "/";
		}
		String format = urlProp.getTrim(props.get(SUB_TYPE), "/");
		if ("/".equals(format)) {
			return format;
		}
		format = replaceWebConfig(format);
		for (Entry<String, String> entry : props.entrySet()) {
			format = StringUtil.replaceAll(format, "#" + entry.getKey(),
					entry.getValue());
		}
		return format;
	}

	private String replaceWebConfig(final String format) {
		Matcher m = WEB_CONFIG_PATTERN.matcher(format);
		String configKey = "";
		if (!m.find()) {
			return format;
		}
		configKey = m.group(1);
		String config = webConfig.getValue(configKey);
		if (Util.isEmpty(config)) {
			return format;
		}
		return StringUtil.replaceAll(format, "\\$" + configKey, config);
	}
}
