/**
 * TestParseUtil.java
 * cn.vko.core.cms
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.cms;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 测试解析属性
 * 
 * @author 彭文杰
 * @Date 2013-12-20
 */
public class TestParseUtil {

	@Test
	public void testGetProps() throws Exception {
		Map<String, String> re = ParseUtil
				.getProps("k= a='b' c= ' d'  e='' f=\"ddddd\"");
		Assert.assertEquals(re.size(), 4);
		Assert.assertEquals(re.get("a"), "b");
		Assert.assertEquals(re.get("c"), " d");
		Assert.assertEquals(re.get("e"), "");
		Assert.assertEquals(re.get("f"), "ddddd");
	}

}
