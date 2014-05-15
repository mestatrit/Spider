/**
 * TestCollectionUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.common.util;

import static cn.vko.core.common.util.CollectionUtil.*;
import static cn.vko.core.common.util.Util.*;
import static org.testng.Assert.*;

import java.util.List;

import org.testng.annotations.Test;

import cn.vko.common.entity.MockEntity;

/**
 * @author 宋星明
 * @author 赵立伟
 * @Date 2013-1-30
 * @version 5.1.0
 */
public class TestCollectionUtil {

	@Test
	public void testList() {
		MockEntity mock = new MockEntity();
		mock.setTitle("2");
		List<MockEntity> list = list(mock);
		assertEquals("2", list.get(0).getTitle());

	}

	@Test
	public void testSubList() {
		List<String> list = list();
		assertTrue(isEmpty(subList(list, 0, 10)));
		list.add("测试1");
		assertEquals("测试1", subList(list, 0, 10).get(0));
		assertTrue(isEmpty(subList(list, 10, 0)));
		assertTrue(isEmpty(subList(list, 0, -1)));
		assertTrue(isEmpty(subList(list, 2, 3)));
	}
}
