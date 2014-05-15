/**
 * ParseUtil.java
 * cn.vko.core.cms
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.cms;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.vko.core.common.util.MapUtil;
import cn.vko.core.common.util.Util;

/**
 * 解析的辅助工具类
 * 
 * @author 彭文杰
 * @Date 2013-12-20
 */
public class ParseUtil {
	private static Pattern PATTERN = Pattern
			.compile("\\s*(\\S*)=\\s*[\'|\"]((.*?))[\'|\"]");

	/**
	 * 解析字符串 字符串 a='b' c='d' 解析为map
	 * 
	 * @param str
	 * @return 对应的map
	 */
	public static Map<String, String> getProps(final String str) {
		if (Util.isEmpty(str)) {
			return null;
		}
		Matcher matcher = PATTERN.matcher(str);
		Map<String, String> map = MapUtil.map();
		while (matcher.find()) {
			if (matcher.groupCount() < 2) {
				continue;
			}
			map.put(matcher.group(1), matcher.group(2));
		}
		return map;
	}
}
