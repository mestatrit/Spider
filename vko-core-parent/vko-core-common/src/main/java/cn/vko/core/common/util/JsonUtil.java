/**
 * JsonUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.Util.*;

import java.util.List;
import java.util.Map;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

/**
 * json工具类
 * 
 * @author 宋星明
 * @author 赵立伟
 * @author 庄君祥
 * @Date 2013-3-25
 * @version 5.1.0
 */
public class JsonUtil {

	/**
	 * 将obj转换成json格式字符串
	 * 
	 * @param obj
	 *            被转对象
	 * @return json字符串
	 */
	public static String toJson(final Object obj) {
		return Json.toJson(obj);
	}

	/**
	 * 将obj转换成json格式字符串
	 * 
	 * @param obj
	 *            被转对象
	 * @return json字符串
	 */
	public static String toJson(final Object obj, final JsonFormat format) {
		return Json.toJson(obj, format);
	}

	/**
	 * 从 JSON 字符串中，根据获取某种指定类型的 JSON 对象。
	 * 
	 * @param json
	 * @param type
	 *            类型
	 * @return 对象
	 */
	public static <T> T fromJson(final String json, final Class<T> type) {
		if (type == null || isEmpty(json)) {
			return null;
		}
		return Json.fromJson(type, json);
	}

	/**
	 * 从 JSON 字符串中，根据获取某种指定类型的 Map 对象。
	 * 
	 * @param type
	 *            Map的值类型
	 * @param json
	 * @return Map
	 */
	public static <T> Map<String, T> fromJsonAsMap(final Class<T> type,
			final String json) {
		if (type == null || isEmpty(json)) {
			return null;
		}
		return Json.fromJsonAsMap(type, json);
	}

	/**
	 * 从 JSON 字符串中，根据获取某种指定类型的List 对象。
	 * 
	 * @param type
	 *            List的值类型
	 * @param json
	 * @return List
	 */
	public static <T> List<T> fromJsonAsList(final Class<T> type,
			final String json) {
		if (type == null || isEmpty(json)) {
			return null;
		}
		return Json.fromJsonAsList(type, json);
	}

}
