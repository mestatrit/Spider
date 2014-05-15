/**
 * TestResponseWrapper.java
 * cn.vko.core.web.view.support
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.web.view.support;

import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * responseWrapper 单元测试
 * 
 * @author 彭文杰
 * @Date 2013-12-25
 */
public class TestResponseWrapper {
	private ResponseWrapper rw;

	@BeforeClass
	public void init() {
		HttpServletResponse rs = EasyMock
				.createNiceMock(HttpServletResponse.class);
		rs.getCharacterEncoding();
		EasyMock.expectLastCall().andReturn("UTF-8");

		EasyMock.replay(rs);
		rw = new ResponseWrapper(rs);
	}

	@Test
	public void testGetOutputStream() throws Throwable {
		rw.getWriter().write("abc");
		Assert.assertEquals(rw.getContent(), "abc");
	}
}
