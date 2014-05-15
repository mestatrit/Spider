/**
 * RandomUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.common.util;

import static cn.vko.core.common.util.ExceptionUtil.*;

import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.nutz.lang.random.R;

/**
 * 随机工具类
 * <p>
 * 生成随机数，随机字符串等和随机有关的工具
 * 
 * @author 庄君祥
 * @author 宋星明
 * @Date 2013-1-22
 * @version 5.1.1
 */
public abstract class RandomUtil {

	/**
	 * 产生随机字符串 字符串由26个字母以及数字构成
	 * 
	 * @param lengths
	 *            字符串长度
	 * @return 随机的字符串
	 */
	public static String randomString(final int lengths) {
		if (lengths < 0) {
			return "";
		}
		StringBuilder buffer = new StringBuilder(
				"0123456789abcdefghijklmnopqrstuvwsyz");
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < lengths; i++) {
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	/**
	 * 从列表中随机获取指定个数的不重复对象
	 * 
	 * @param <T>
	 *            对象类型
	 * @param source
	 *            来源
	 * @param count
	 *            个数
	 * @return 随机的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> randomSet(final List<T> source, final int count) {
		Set<T> result = new LinkedHashSet<T>();
		if (Util.isEmpty(source)) {
			return result;
		}
		if (source.size() < count) {
			result.addAll(source);
			return result;
		}
		List<T> temp = CollectionUtil.list();
		temp.addAll(source);
		Random random = new Random();
		while (temp.size() > 0 && result.size() < count) {
			int pos = random.nextInt(temp.size());
			result.add(temp.get(pos));
			temp.remove(pos);
		}
		return result;
	}

	/**
	 * 产生随机数 范围是:0-(n-1)
	 * 
	 * @param n
	 *            最大值
	 * @return 随机数
	 */
	public static int nextInt(final int n) {
		if (n <= 0) {
			throw pEx("n必须是正数");
		}
		return new SecureRandom().nextInt(n);
	}

	/**
	 * 16进制表示的紧凑格式的 UUID
	 * 
	 * @return 紧凑的64位
	 */
	public static String uu16() {
		return R.UU16();
	}

	public static <T> T random(Set<T> set) {
		if (Util.isEmpty(set)) {
			return null;
		}
		int ran = nextInt(set.size());
		int pos = 0;
		for (T one : set) {
			if (pos == ran) {
				return one;
			}
			pos++;
		}
		return null;
	}
}
