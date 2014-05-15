package cn.vko.common.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.vko.common.entity.MockEntity;
import cn.vko.core.common.util.BeanUtil;

/**
 * @author 赵立伟
 * @author 刘一卓
 * @Date 2013-2-1
 * @version 5.0.1
 */
public class TestBeanUtil {

	@Test
	public void testCopyProperties() {
		MockEntity mockEntity = new MockEntity();
		mockEntity.setTitle("title");
		MockEntity mockEntity2 = new MockEntity();
		BeanUtil.copyProperties(mockEntity, mockEntity2);
		Assert.assertEquals("title", mockEntity2.getTitle());
	}

	@Test
	public void testContain() {
		Assert.assertEquals(true, BeanUtil.contain(MockEntity.class, "title"));
	}

	@Test
	public void testGet() {
		MockEntity mockEntity = new MockEntity();
		mockEntity.setStr("a");
		mockEntity.setTitle("b");
		Assert.assertEquals("b", BeanUtil.get(mockEntity, "title"));
	}

	@Test
	public void testGetString() {
		MockEntity mockEntity = new MockEntity();
		mockEntity.setStr("a");
		mockEntity.setTitle("b");
		Assert.assertEquals("a", BeanUtil.getString(mockEntity, "str"));

	}

	@Test
	public void testIsFieldEmpty() {
		MockEntity mockEntity = new MockEntity();
		mockEntity.setStr("a");
		mockEntity.setTitle("");
		mockEntity.setFatherName("dad");
		mockEntity.setAge(0);
		Assert.assertFalse(BeanUtil.isFieldEmpty(mockEntity, "str"));
		Assert.assertFalse(BeanUtil.isFieldEmpty(mockEntity, "fatherName"));
		Assert.assertTrue(BeanUtil.isFieldEmpty(mockEntity, "title"));
		Assert.assertFalse(BeanUtil.isFieldEmpty(mockEntity, "age"));
	}
}
