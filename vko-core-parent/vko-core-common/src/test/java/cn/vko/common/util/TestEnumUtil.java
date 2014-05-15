/**
 * TestEnumUtil.java
 * cn.vko.common.enums
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.common.util;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.vko.common.entity.MockEnum;
import cn.vko.core.common.util.EnumUtil;

/**
 * @author 赵立伟
 * @Date 2013-2-1
 * @version 5.0.1
 */
public class TestEnumUtil {

	@Test
	public void testEnum2() throws Exception {
		Map<String, String> re = EnumUtil.enum2(MockEnum.class);
		Assert.assertEquals(2, re.size());
		Assert.assertEquals("测试", re.get("test"));
		Assert.assertEquals("模拟", re.get("mock"));
	}
}
