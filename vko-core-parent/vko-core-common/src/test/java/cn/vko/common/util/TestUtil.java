/**
 * TestUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.common.util;

import static cn.vko.core.common.util.CollectionUtil.*;
import static cn.vko.core.common.util.MapUtil.*;
import static cn.vko.core.common.util.Util.*;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

import cn.vko.common.entity.MockEntity;

/**
 * @author 赵立伟
 * @Date 2013-2-1
 * @version 5.0.1
 */
public class TestUtil {

	@Test
	public void testIsEmpty() {
		assertTrue(isEmpty(null));
		assertTrue(isEmpty(""));
		assertTrue(isEmpty("   "));
		assertTrue(isEmpty(new String[0]));
		assertTrue(isEmpty(list()));
		assertTrue(isEmpty(map()));
		assertFalse(isEmpty(new MockEntity()));
	}

	@Test
	public void testEq() {
		assertTrue(eq(Long.valueOf("1"), Long.valueOf("1")));
		assertTrue(eq("1", Long.valueOf("1")));
		assertFalse(eq(null, "null"));
	}

	@Test
	public void testFirst() {
		assertNull(first(null));
		assertEquals(1l, first(1l));
		assertEquals(1l, first(list(1l, 2l)));
	}
}
