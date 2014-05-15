/**
 * Cms.java
 * cn.vko.core.cms
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.cms;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;

import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.vko.core.cms.interfaces.ICms;
import cn.vko.core.common.util.MapUtil;
import cn.vko.core.common.util.StringUtil;
import cn.vko.core.common.util.Util;

/**
 * cms核心引擎，负责对外提供服务
 * 
 * @author 彭文杰
 * @Date 2013-12-20
 */
@Data
public class Cms {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(Cms.class);
	private static int MAX_PARSE_NUM = 3;
	private static String PATTERNSTR = "<cms:.*?/>";
	private static Pattern PATTERN = Pattern.compile(PATTERNSTR);
	private List<ICms> changes;

	/**
	 * 使用模板解析并替换内容
	 * 
	 * @param str
	 *            原字符串
	 * @param req
	 *            http请求对象
	 * @param context
	 *            ServletContext对象
	 * @return 待替换的字符传
	 */
	public String parse(final HttpServletRequest req, final String str) {
		return parseTime(req, str, 0);
	}

	/**
	 * 防止存在嵌套标签，进行多次处理 在不超过最大次数的情况下进行多次处理
	 * 
	 * @param req
	 *            请求对象
	 * @param str
	 *            待处理的字符串
	 * @param num
	 *            处理次数
	 * @return 处理后结果
	 */
	private String parseTime(final HttpServletRequest req, final String str,
			final int num) {
		if (Util.isEmpty(str)) {
			return "";
		}

		if (Util.isEmpty(changes)) {
			logger.error("not set cms changes yet!"); //$NON-NLS-1$
			return str;
		}
		Set<String> cmsTags = matchCms(str);
		if (Util.isEmpty(cmsTags)) {
			return str;
		}
		Map<String, String> needChange = MapUtil.map();
		for (ICms cms : changes) {
			cms.change(req, cmsTags, needChange);
		}
		String result = str;
		for (Entry<String, String> en : needChange.entrySet()) {
			result = StringUtil.replaceAll(result, en.getKey(), en.getValue());
		}
		if (!Util.isEmpty(cmsTags)) {
			logger.error("not changed cms: {0}", Json.toJson(cmsTags)); //$NON-NLS-1$
			for (String one : cmsTags) {
				result = StringUtil.replaceAll(result, one, "");
			}
		}
		if (num < MAX_PARSE_NUM) {
			return parseTime(req, result, num + 1);
		}
		return result;
	}

	/**
	 * 匹配cms标签
	 * 
	 * @param str
	 *            待匹配的字符串
	 * @return 匹配到的字符串
	 */
	private Set<String> matchCms(final String str) {
		Matcher matcher = PATTERN.matcher(str);
		Set<String> cmsTags = Lang.set();
		while (matcher.find()) {
			cmsTags.add(matcher.group(0));
		}
		return cmsTags;
	}

	/**
	 * 根据类型清理缓存数据
	 * 
	 * @param type
	 *            类型
	 * @param id
	 *            唯一标示
	 */
	public void rmCache(final String type, final String id) {
		if (Util.isEmpty(changes)) {
			return;
		}
		for (ICms cms : changes) {
			cms.rmCache(type, id);
		}
	}
}
