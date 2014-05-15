/**
 * TestCms.java
 * cn.vko.core.cms
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.cms;

import java.util.List;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.vko.core.cms.interfaces.ICms;
import cn.vko.core.cms.interfaces.impl.DateCms;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.DateTimeUtil;

/**
 * cms单元测试
 * 
 * @author 彭文杰
 * @Date 2013-12-23
 */
public class TestCms {

	private Cms cms;

	@BeforeMethod
	public void createCms() throws Exception {
		cms = new Cms();
		List<ICms> changes = CollectionUtil.list();
		cms.setChanges(changes);
	}

	@Test
	public void testParse() {
		String result = cms.parse(null, null);
		Assert.assertEquals(result, "");
		result = cms.parse(null, "abc");
		Assert.assertEquals(result, "abc");
		result = cms.parse(null, "abc<cms:date uid='aa'/>");
		Assert.assertEquals(result, "abc<cms:date uid='aa'/>");
		List<ICms> changes = CollectionUtil.list();
		changes.add(new DateCms());
		cms.setChanges(changes);
		result = cms.parse(null, "abc<cms:date uid='aa'/>");
		Assert.assertEquals(result, "abc");
		result = cms.parse(null, "abc<cms:date a='b' c='d' uid='"
				+ DateTimeUtil.nowStr("yyyy-MM-dd HH:mm:ss") + "'/>");
		Assert.assertEquals(result, "abc刚刚");
	}

	@Test
	public void testRegex() {
		Pattern p = Pattern.compile("<cms:.*/>");
		Assert.assertTrue(p.matcher("abc<cms:a/>").find());
	}
}
