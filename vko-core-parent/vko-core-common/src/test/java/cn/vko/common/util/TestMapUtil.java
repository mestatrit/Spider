package cn.vko.common.util;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.vko.core.common.util.MapUtil;

/**
 * @author 赵立伟
 * @Date 2013-2-1
 * @version 5.0.2
 */
public class TestMapUtil {

	@Test
	public void testMapKV() {
		Assert.assertEquals("value1", MapUtil.map("key1", "value1").get("key1"));
	}

	@Test
	public void testMapObjectArray() {
		Assert.assertEquals("4444", MapUtil.map("key2", "2222", "key4", "4444")
				.get("key4"));
	}

	@Test
	public void testLinkedMapKV() {
		Assert.assertEquals("1111",
				MapUtil.linkedMap("keyssss", "1111").get("keyssss"));
	}

	@Test
	public void testLinkedMapObjectArray() {
		Assert.assertEquals("22222",
				MapUtil.linkedMap("key1", "11111", "key2", "22222").get("key2"));
	}

	@SuppressWarnings("cast")
	@Test
	public void testAddMapList() {
		Map<String, List<String>> map = MapUtil.map();
		MapUtil.addMapList(map, "str1", "strValue1");
		Assert.assertEquals(1, map.get("str1").size());
	}

	@Test
	public void testAddMapMap() {
		Map<String, Map<String, Object>> param = MapUtil.map();
		MapUtil.addMapMap(param, "str1", "str2", "value");
		Assert.assertEquals("value", MapUtil.getMapMap(param, "str1", "str2"));
	}
}
