/**
 * TestDbExendUtil.java
 * cn.vko.core.db.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.util;

import static cn.vko.core.common.util.CollectionUtil.*;
import static org.testng.Assert.*;

import java.util.List;

import org.easymock.EasyMock;
import org.nutz.dao.Cnd;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.core.common.exception.impl.ParamException;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.IIdGen;
import cn.vko.core.db.dao.impl.DbDao;
import cn.vko.core.db.mock.entity.MockEntity4ExtendCount;
import cn.vko.core.db.mock.entity.MockEntity4Valid;

/**
 * DbExendUtil测试类
 * 
 * @author 庄君祥
 * @Date 2013-12-10
 */
public class TestDbExendUtil {
	private IDbDao dbDao;
	private IIdGen idGen;
	private NutDao nutDao;
	private Ioc ioc;
	private int totalQuerySize = 100;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("db/config/dao.js"));
		nutDao = ioc.get(NutDao.class, "nutDao");
		mockIIdGen();
		dbDao = new DbDao(nutDao, idGen);
		nutDao.create(MockEntity4ExtendCount.class, true);
		nutDao.create(MockEntity4Valid.class, true);
		initQuery();
	}

	private void initQuery() {
		List<MockEntity4ExtendCount> entites = list();
		for (int i = 0; i < totalQuerySize; i++) {
			entites.add(new MockEntity4ExtendCount("测试" + i));
		}
		dbDao.insert(entites);
	}

	/**
	 * mock IIdGen的接口
	 */
	private void mockIIdGen() {
		idGen = EasyMock.createMock(IIdGen.class);
		long currentTimeMillis = System.currentTimeMillis();
		for (int i = 0; i < 200; i++) {
			idGen.getId();
			EasyMock.expectLastCall().andReturn(currentTimeMillis++);
		}
		EasyMock.replay(idGen);
	}

	@Test
	public void testCount() throws Exception {
		assertEquals(totalQuerySize,
				DbExtendUtil.count(dbDao, MockEntity4ExtendCount.class));
	}

	@Test
	public void testCountCnd() {
		int size = 50;
		assertEquals(
				size,
				DbExtendUtil.count(dbDao, MockEntity4ExtendCount.class,
						Cnd.where("id", "<=", size)));
	}

	@Test(expectedExceptions = ParamException.class)
	public void testCountNull() throws Exception {
		Class<?> clazz = null;
		assertNull(clazz);
		DbExtendUtil.count(dbDao, clazz);
	}

	@Test
	public void testAbleSignleNoral() throws Exception {
		MockEntity4Valid entity = new MockEntity4Valid();
		dbDao.insert(entity);
		assertTrue(entity.isValid());
		DbExtendUtil.disable(dbDao, MockEntity4Valid.class, entity.getId());
		MockEntity4Valid disableEntity = dbDao.fetch(MockEntity4Valid.class,
				entity.getId());
		assertFalse(disableEntity.isValid());
		DbExtendUtil.enable(dbDao, MockEntity4Valid.class, entity.getId());
		MockEntity4Valid enableEntity = dbDao.fetch(MockEntity4Valid.class,
				entity.getId());
		assertTrue(enableEntity.isValid());
	}

	@Test
	public void testAbleMulipt() {
		MockEntity4Valid entity1 = new MockEntity4Valid();
		MockEntity4Valid entity2 = new MockEntity4Valid();
		dbDao.insert(CollectionUtil.list(entity1, entity2));
		DbExtendUtil.disable(dbDao, MockEntity4Valid.class, entity1.getId(),
				entity2.getId());
		MockEntity4Valid disableEntity1 = dbDao.fetch(MockEntity4Valid.class,
				entity1.getId());
		MockEntity4Valid disableEntity2 = dbDao.fetch(MockEntity4Valid.class,
				entity2.getId());
		assertFalse(disableEntity1.isValid());
		assertFalse(disableEntity2.isValid());
		DbExtendUtil.enable(dbDao, MockEntity4Valid.class, entity1.getId(),
				entity2.getId());

		MockEntity4Valid enableEntity1 = dbDao.fetch(MockEntity4Valid.class,
				entity1.getId());
		MockEntity4Valid enableEntity2 = dbDao.fetch(MockEntity4Valid.class,
				entity2.getId());
		assertTrue(enableEntity1.isValid());
		assertTrue(enableEntity2.isValid());
	}

	@Test
	public void testDisableStatus() throws Exception {
		MockEntity4Valid entity = new MockEntity4Valid();
		entity.setValid(false);
		dbDao.insert(entity);
		assertFalse(entity.isValid());
		DbExtendUtil.disable(dbDao, MockEntity4Valid.class, entity.getId());
		MockEntity4Valid disableEntity = dbDao.fetch(MockEntity4Valid.class,
				entity.getId());
		assertFalse(disableEntity.isValid());
	}

	@Test
	public void testEnableStatus() throws Exception {
		MockEntity4Valid entity = new MockEntity4Valid();
		entity.setValid(true);
		dbDao.insert(entity);
		assertTrue(entity.isValid());
		DbExtendUtil.enable(dbDao, MockEntity4Valid.class, entity.getId());
		MockEntity4Valid enableEntity = dbDao.fetch(MockEntity4Valid.class,
				entity.getId());
		assertTrue(enableEntity.isValid());
	}
}
