/**
 * TestArrayUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.common.util;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.vko.core.common.util.ArrayUtil;
import cn.vko.core.common.util.CollectionUtil;

/**
 * @author 宋星明
 * @Date 2013-2-19
 * @version 5.1.0
 */
public class TestArrayUtil {

	@Test
	public void testArray2List() {
		String[] str = ArrayUtil.array("1", "2");
		Assert.assertEquals("1", ArrayUtil.array2List(str).get(0));
		Assert.assertEquals("2", ArrayUtil.array2List(str).get(1));
		Assert.assertEquals(str.length, ArrayUtil.array2List(str).size());
	}

	@Test
	public void testArray2array() throws Exception {
		Object[] objs = ArrayUtil.array("1", "2");
		Integer[] is = (Integer[]) ArrayUtil.array2array(objs, Integer.class);
		Assert.assertEquals(1, is[0].intValue());
		Assert.assertEquals(2, is[1].intValue());
		Assert.assertEquals(objs.length, is.length);
	}

	@Test
	public void testArray2StringArray() throws Exception {
		Object[] objs = ArrayUtil.array("1", "2");
		String[] is = ArrayUtil.array2StringArray(objs);
		Assert.assertEquals("1", is[0]);
		Assert.assertEquals("2", is[1]);
		Assert.assertEquals(objs.length, is.length);
	}

	@Test
	public void testArrayObjectString() throws Exception {
		Object[] objs = ArrayUtil.array("1", "2");
		String[] is = ArrayUtil.arrayObjectString(objs);
		Assert.assertEquals("1", is[0]);
		List<String> list = CollectionUtil.list("1", "2");
		String[] is4List = ArrayUtil.arrayObjectString(list);
		Assert.assertEquals("1", is4List[0]);
		String[] is4Object = ArrayUtil.arrayObjectString("1");
		Assert.assertEquals("1", is4Object[0]);
	}
}
