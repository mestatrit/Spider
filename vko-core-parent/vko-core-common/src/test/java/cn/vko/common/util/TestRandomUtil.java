/**
 * TestRandomUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.common.util;

import static cn.vko.core.common.util.Util.*;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

import cn.vko.core.common.util.RandomUtil;

/**
 * @author 宋星明
 * @Date 2013-1-30
 * @version 5.1.0
 */
public class TestRandomUtil {

	@Test
	public void testRandomString() {
		String str1 = RandomUtil.randomString(3);
		String str2 = RandomUtil.randomString(3);
		assertFalse(eq(str1, str2));
	}

	@Test
	public void testNextInt() throws Exception {
		for (int index = 0; index < 1000; index++) {
			int result = RandomUtil.nextInt(100);
			assertTrue(0 <= result && result < 100);
		}
	}

	@Test
	public void testUu16() throws Exception {
		String str1 = RandomUtil.uu16();
		String str2 = RandomUtil.uu16();
		assertNotEquals(str1, str2);
	}
}
