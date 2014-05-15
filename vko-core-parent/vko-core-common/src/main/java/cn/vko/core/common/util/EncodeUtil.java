/**
 * Base64Util.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.Util.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base64工具类
 * 
 * @author 赵立伟
 * @author 宋星明
 * @author 彭文杰
 * @Date 2013-2-1
 * @version 5.1.0
 */
public class EncodeUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EncodeUtil.class);

	/**
	 * 将字符串BASE64编码
	 * 
	 * @param message
	 *            待编码数据
	 * @return BASE64编码结果
	 */
	public static String toBase64(final String message) {
		return new String(toBase64Byte(message));
	}

	/**
	 * 将字符串BASE64编码
	 * 
	 * @param message
	 *            待编码数据
	 * @return BASE64编码数组
	 */
	public static byte[] toBase64Byte(final String message) {
		if (isEmpty(message)) {
			return new byte[0];
		}
		try {
			byte[] b = Base64.encodeBase64(message.getBytes("UTF-8"), false);
			return b;
		} catch (UnsupportedEncodingException e) {
			logger.error("不支持Utf-8编码！", e);
		}
		return new byte[0];
	}

	/**
	 * 将字符串BASE64解码
	 * 
	 * @param message
	 *            待解码数据
	 * @return BASE64解码结果
	 */
	public static String deBase64(final String message) {
		if (isEmpty(message)) {
			return "";
		}
		try {
			byte[] b = Base64.decodeBase64(message.getBytes("UTF-8"));
			return new String(b);
		} catch (UnsupportedEncodingException e) {
			logger.error("不支持Utf-8编码！", e);
		}
		return "";
	}

	/**
	 * 将字符串转换成 html
	 * 
	 * @param message
	 *            待转换的 数据
	 * @return 转换后的html
	 */
	public static String toHtml(final String message) {
		if (isEmpty(message)) {
			return "";
		}
		return StringEscapeUtils.escapeHtml(message);
	}

	/**
	 * 将html 转换程 字符串
	 * 
	 * @param message
	 *            待转换的 数据
	 * @return 转换后的字符串
	 */
	public static String deHtml(final String message) {
		if (isEmpty(message)) {
			return "";
		}
		return StringEscapeUtils.unescapeHtml(message);
	}

	/**
	 * 对网址编码
	 * 
	 * @param str
	 *            网址
	 */
	public static String urlEncode(final String str) {
		if (isEmpty(str)) {
			return "";
		}
		String encode = "";
		try {
			encode = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("网址编码出错", e);
		}
		return encode;
	}

	/**
	 * 对网址解码
	 * 
	 * @param encode
	 *            编码后的字符串
	 */
	public static String urlDecode(final String encode) {
		if (isEmpty(encode)) {
			return "";
		}
		String decode = "";
		try {
			decode = URLDecoder.decode(encode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("网址解码出错", e);
		}
		return decode;
	}

	/**
	 * 将字符串转换成 xml
	 * 
	 * @param message
	 *            待转换的 数据
	 * @return 转换后的xml
	 */
	public static String toXml(final String message) {
		if (isEmpty(message)) {
			return "";
		}
		return StringEscapeUtils.escapeXml(message);
	}

	/**
	 * 将xml 转换程 字符串
	 * 
	 * @param message
	 *            待转换的 数据
	 * @return 转换后的字符串
	 */
	public static String deXml(final String message) {
		if (isEmpty(message)) {
			return "";
		}
		return StringEscapeUtils.unescapeXml(message);
	}
}
