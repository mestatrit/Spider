/**
 * StringUtil.java
 * cn.vko.core.utils
 * Copyright (c) 2011, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.ExceptionUtil.*;
import static cn.vko.core.common.util.Util.*;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.nutz.lang.Lang;
import org.nutz.lang.Strings;

/**
 * 字符串处理的工具类
 * 
 * @author 彭文杰
 * @author 赵立伟
 * @author 庄君祥
 * @author 马磊
 * @Date 2011-9-1
 * @version 5.2.1
 */
public class StringUtil {
	/**
	 * 使用MessageFormat类的方式对字符串进行格式化
	 * <p>
	 * 传入空串或null时，返回空串 传入空参数时，返回原字符串
	 * 
	 * @param format
	 *            格式化串
	 * @param args
	 *            参数
	 * @return 格式化后的字符串
	 * @throws DataException
	 *             如果格式存在异常，则提示DataException
	 */
	public static String format(final String format, final Object... args) {
		return format(Locale.getDefault(), format, args);
	}

	/**
	 * 使用MessageFormat类的方式对字符串进行格式化
	 * <p>
	 * 传入空串或null时，返回空串 传入空参数时，返回原字符串
	 * 
	 * @param locale
	 *            本地化语言
	 * @param format
	 *            格式化串
	 * @param args
	 *            参数
	 * @return 格式化后的字符串
	 * @throws DataException
	 *             如果格式存在异常，则提示DataException
	 */
	public static String format(final Locale locale, final String format,
			final Object... args) {
		if (isEmpty(format)) {
			return "";
		}
		if (args == null) {
			return format;
		}
		try {
			MessageFormat formart = new MessageFormat(format, locale);
			return formart.format(args);
		} catch (Exception e) {
			throw pEx("字符串格式化错误", e);
		}
	}

	/**
	 * 根据分隔符把集合拼接字符串
	 * 
	 * @param c
	 *            分隔符
	 * @param coll
	 *            集合
	 * @return 拼接后的字符串
	 */
	public static <T> String join(final Object c, final Collection<T> coll) {
		return Lang.concat(c, coll).toString();
	}

	/**
	 * 根据分隔符把集合拼接字符串
	 * 
	 * @param c
	 *            分隔符
	 * @param coll
	 *            集合
	 * @return 拼接后的字符串
	 */
	public static <T> String join(final Object c, final T[] coll) {
		return Lang.concat(c, coll).toString();
	}

	/**
	 * 根据分隔符把不定长的集合拼接字符串
	 * 
	 * @param c
	 *            分隔符
	 * @param ts
	 *            不定长的集合
	 * @return 拼接后的字符串
	 */
	public static <T> String joinUncertain(final Object c, final T... ts) {
		return join(c, ts);
	}

	/**
	 * 移除匹配字符串及前面的内容
	 * 
	 * @param content
	 *            源字符串
	 * @param removed
	 *            匹配字符串
	 * @return 移除后的字符串
	 */
	public static String removeLeft(final String content, final String removed) {
		if (isEmpty(content) || isEmpty(removed)) {
			return content;
		}
		int index = content.indexOf(removed);
		if (index == -1) {
			return content;
		}
		return content.substring(index + removed.length());
	}

	/**
	 * 前面为空，则返回后面值
	 * 
	 * @param content
	 *            待判定的值
	 * @param as
	 *            如果为空返回的值
	 * @return 返回结果
	 */
	public static String nvl(final String content, final String as) {
		if (isEmpty(content)) {
			return as;
		}
		return content;
	}

	/**
	 * 如果为空，则返回空字符串
	 * 
	 * @param content
	 *            待判定的值
	 * @return 返回结果
	 */
	public static String nvl(final String content) {
		if (isEmpty(content)) {
			return "";
		}
		return content;
	}

	/**
	 * 判断字符是否是汉字
	 * 
	 * @param c
	 *            字符
	 * @return 是否汉字
	 */
	public static boolean isChinese(final char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是英文
	 * 
	 * @param c
	 *            字符
	 * @return 是否是英文
	 */
	public static boolean isEnglish(final char c) {
		if (c >= 'a' && c <= 'z') {
			return true;
		}
		if (c >= 'A' && c <= 'Z') {
			return true;
		}
		return false;
	}

	/**
	 * 是否是数字
	 * 
	 * @param c
	 *            字符
	 * @return 是否是英文
	 */
	public static boolean isNumber(final char c) {
		if (c >= '0' && c <= '9') {
			return true;
		}
		return false;
	}

	/**
	 * 是否是asii
	 * 
	 * @param c
	 *            字符
	 * @return 是否是英文
	 */
	public static boolean isASCII(final char c) {
		if (c >= 20 && c <= 126) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是中文
	 * 
	 * @param s
	 *            待判断的字符串
	 * @return 是否是中文
	 */
	public static boolean isChinese(final String s) {
		for (int i = 0, n = s.length(); i < n; i++) {
			char c = s.charAt(i);
			if (isChinese(c)) {
				continue;
			}
			return false;
		}
		return true;
	}

	/**
	 * 清除字符串中，非汉字、字母、数字的内容
	 * 
	 * @param s
	 *            待处理字符
	 * @return 处理后内容
	 */
	public static String clean(final String s) {
		if (isEmpty(s)) {
			return "";
		}
		return s.replaceAll("[^a-zA-Z0-9_\u4e00-\u9fa5]", "");
	}

	/**
	 * 自定义截取字符串
	 * 
	 * @param content
	 *            源字符串
	 * @param size
	 *            截取大小
	 * @return 截取的字符串
	 */
	public static String sub(final String content, final int size) {
		if (isEmpty(content) || size <= 0) {
			return "";
		}
		int length = content.length();
		if (size >= length) {
			return content;
		}
		return content.substring(0, size) + "…";
	}

	/**
	 * 自定义截取字符串
	 * 
	 * @param content
	 *            源字符串
	 * @param size
	 *            截取大小
	 * @return 截取的字符串
	 */
	public static String left(final String content, final int size) {
		if (isEmpty(content) || size <= 0) {
			return "";
		}
		String result = content.trim();
		int length = result.length();
		if (size >= length) {
			return result;
		}
		return result.substring(0, size);
	}

	/**
	 * 替换字符串内容
	 * 
	 * @param str
	 *            被替换字符串
	 * @param re
	 *            需要替换的内容
	 * @param replaceChar
	 *            替换成的char
	 * @return 替换后的字符串
	 */
	public static String replace(final String str, final List<String> re,
			final char replaceChar) {
		if (isEmpty(str)) {
			return "";
		}
		if (isEmpty(re)) {
			return str;
		}
		String result = str;
		for (String reStr : re) {
			if (isEmpty(reStr)) {
				continue;
			}
			result = result.replaceAll(reStr,
					Strings.alignLeft("", reStr.length(), replaceChar));
		}
		return result;
	}

	/**
	 * 将用固定符号隔开的字符串转为long[]
	 * 
	 * @param str
	 *            字符串
	 * @param split
	 *            分隔符
	 * @return long数组
	 */
	public static long[] splitLong(final String str, final String split) {
		String[] strArr = split(str, split);
		if (isEmpty(strArr)) {
			return new long[0];
		}
		long[] result = new long[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			result[i] = ConvertUtil.obj2long(strArr[i]);
		}
		return result;
	}

	/**
	 * 将用固定符号隔开的字符串转为String[]
	 * 
	 * @param str
	 *            字符串
	 * @param split
	 *            分隔符
	 * @return String数组
	 */
	public static String[] split(final String str, final String split) {
		if (isEmpty(str)) {
			return new String[0];
		}
		return str.split(split);
	}

	/**
	 * 替换字符串方法 普通replaceAll,如果替换的内容中存在$则会出现错误，为解决问题
	 * 特提供此方法，如果不能确定替换内容是否存在$时，使用此方法
	 * 
	 * @param orgin
	 *            原始字符串
	 * @param regex
	 *            正则
	 * @param replace
	 *            要替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceAll(final String orgin, final String regex,
			final String replace) {
		if (isEmpty(orgin)) {
			return "";
		}
		if (isEmpty(regex)) {
			return orgin;
		}
		String replaceStr = replace;
		if (replaceStr == null) {
			replaceStr = "";
		} else if (replaceStr.indexOf("$") > -1) {
			replaceStr = replaceStr.replaceAll("\\$", "\\\\\\$");
		}
		return orgin.replaceAll(regex, replaceStr);
	}

	/**
	 * 将文件路径中的反斜线变为http协议中的斜线
	 * 
	 * @param path
	 *            路径
	 * @return 转换后的路径
	 */
	public static String path2Web(final String path) {
		if (isEmpty(path)) {
			return path;
		}
		return path.replace('\\', '/');
	}

	/**
	 * @param str
	 *            : source string
	 * @param width
	 *            : string's byte width
	 * @param ellipsis
	 *            : a string added to abbreviate string bottom <a
	 *            href="http://my.oschina.net/u/556800" class="referer"
	 *            target="_blank">@return</a> String Object
	 * 
	 */
	public static String abbreviate(String str, int width, String ellipsis) {
		if (str == null || "".equals(str)) {
			return "";
		}

		int d = 0; // byte length
		int n = 0; // char length
		for (; n < str.length(); n++) {
			d = str.charAt(n) > 256 ? d + 2 : d + 1;
			if (d > width) {
				break;
			}
		}

		if (d > width) {
			n = n - ellipsis.length() / 2;
			return str.substring(0, n > 0 ? n : 0) + ellipsis;
		}

		return str = str.substring(0, n);
	}

	/**
	 * @param str
	 *            : source string
	 * @param width
	 *            : string's byte width
	 * @param ellipsis
	 *            : a string added to abbreviate string bottom <a
	 *            href="http://my.oschina.net/u/556800" class="referer"
	 *            target="_blank">@return</a> String Object
	 * 
	 */
	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		java.util.regex.Pattern p_html1;
		java.util.regex.Matcher m_html1;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}

	public static String subLeft(String org, String split) {
		if (Util.isEmpty(org)) {
			return "";
		}
		int index = org.indexOf(split);
		if (index < 0) {
			return org;
		} else {
			return org.substring(0, index);
		}
	}

	public static String subLastLeft(String org, String split) {
		if (Util.isEmpty(org)) {
			return "";
		}
		int index = org.lastIndexOf(split);
		if (index < 0) {
			return org;
		} else {
			return org.substring(0, index);
		}
	}

	public static String subRight(String org, String split) {
		if (Util.isEmpty(org)) {
			return "";
		}
		int index = org.lastIndexOf(split);
		if (index < 0) {
			return org;
		} else {
			return org.substring(index + 1);
		}
	}

	public static String subLastRight(String org, String split) {
		if (Util.isEmpty(org)) {
			return "";
		}
		int index = org.indexOf(split);
		if (index < 0) {
			return org;
		} else {
			return org.substring(index + 1);

		}
	}

	public static String trim(String org, String split) {
		if (Util.isEmpty(org)) {
			return "";
		}
		int index = org.indexOf(split);
		if (index < 0) {
			return org;
		} else {
			return org.substring(index + 1);
		}
	}
}
