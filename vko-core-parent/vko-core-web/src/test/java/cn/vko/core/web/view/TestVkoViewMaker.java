/**
 * TestVkoViewMaker.java
 * cn.vko.core.web.view
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.view;

import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * VkoViewMaker测试类
 * 
 * @author 庄君祥
 * @Date Dec 26, 2013
 */
public class TestVkoViewMaker {
	@Test
	public void testMake() throws Exception {
		Pattern jspPattern = Pattern.compile("\\s*jsp\\s*");
		String jsp = "jsp";
		Assert.assertTrue(jspPattern.matcher(jsp).find());
		String jspBlankBefore = "  jsp";
		Assert.assertTrue(jspPattern.matcher(jspBlankBefore).find());
		String jspBlankBoth = "  jsp    ";
		Assert.assertTrue(jspPattern.matcher(jspBlankBoth).find());
		String jspBlankAfter = "jsp    ";
		Assert.assertTrue(jspPattern.matcher(jspBlankAfter).find());
		String jspTableAfter = "jsp		";
		Assert.assertTrue(jspPattern.matcher(jspTableAfter).find());

		String jspWithParam = "jsp-p";
		Assert.assertTrue(jspPattern.matcher(jspWithParam).find());
	}
}
