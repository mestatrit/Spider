/**
 * TestJsonUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.common.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.vko.core.common.util.JsonUtil;
import cn.vko.core.common.util.MapUtil;

/**
 * @author 宋星明
 * @Date 2013-3-25
 * @version 5.1.0
 */
public class TestJsonUtil {

	@Test
	public void testToJson() {
		Set<String> sets = new HashSet<String>();
		sets.add("zhangsan");
		sets.add("lisi");
		Assert.assertEquals("[\"lisi\", \"zhangsan\"]", JsonUtil.toJson(sets));
		String[] a = new String[0];
		System.out.println(JsonUtil.toJson(a));
	}

	@Test
	public void testFromJsonAsMap() {
		Map<String, String> map = MapUtil.map();
		map.put("title", "头衔");
		map.put("name", "姓名");
		String json = JsonUtil.toJson(map);
		Map<String, Object> map1 = JsonUtil.fromJsonAsMap(Object.class, json);
		Assert.assertEquals("头衔", map1.get("title").toString());
		Assert.assertEquals("姓名", map1.get("name").toString());
	}

}
