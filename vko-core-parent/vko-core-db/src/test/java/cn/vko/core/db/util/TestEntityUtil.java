/**
 * TestAnnotationUtil.java
 * cn.vko.core.db.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.util;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import cn.vko.core.db.mock.entity.MockEntity;

/**
 * AnnotationUtil单元测试类
 * 
 * @author 庄君祥
 * @Date 2013-12-10
 */
public class TestEntityUtil {

	@Test
	public void testGetTableName() {
		assertEquals("mock_entity", EntityUtil.getTableName(MockEntity.class));
	}

	@Test
	public void testEName2TName() {
		assertEquals("mock_entity",
				EntityUtil.eName2TName(MockEntity.class.getSimpleName()));
	}

}
