/**
 * TestExceptionUtil.java
 * cn.vko.common.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.common.util;

import static cn.vko.core.common.util.ExceptionUtil.*;

import org.testng.annotations.Test;

import cn.vko.core.common.exception.impl.ParamException;

/**
 * @author 宋星明
 * @author 赵立伟
 * @Date 2013-1-30
 * @version 5.1.0
 */
public class TestExceptionUtil {
	@Test
	public void testCheckEmptyWithoutEx() {
		checkEmpty("", "");
	}

	@Test(expectedExceptions = ParamException.class)
	public void testCheckEmptyWithEmptyStr() {
		checkEmpty("", "");
	}
}
