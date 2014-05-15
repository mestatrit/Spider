/**
 * TestOperateDbDao.java
 * cn.vko.core.db.dao.impl
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.dao.impl;

import static org.testng.Assert.*;

import java.sql.Timestamp;
import java.util.Collection;

import org.easymock.EasyMock;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.core.common.exception.impl.ParamException;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.DateTimeUtil;
import cn.vko.core.common.util.MapUtil;
import cn.vko.core.db.dao.IIdGen;
import cn.vko.core.db.dao.impl.support.IFetchUser;
import cn.vko.core.db.mock.entity.MockEntity4OperateDbDao;

/**
 * OperateDbDao单元测试类
 * 
 * @author 庄君祥
 * @Date 2013-12-10
 */
public class TestOperateDbDao {
	private OperateDbDao operateDbDao;
	private IIdGen idGen;
	private IFetchUser fetchUser;
	private NutDao nutDao;
	private String realName = "庄君祥";
	private long userId = 9300L;
	private Ioc ioc;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("db/config/dao.js"));
		nutDao = ioc.get(NutDao.class, "nutDao");
		mockIIdGen();
		mockIFetchUser();
		operateDbDao = new OperateDbDao(nutDao, idGen, fetchUser);
		nutDao.create(MockEntity4OperateDbDao.class, true);
	}

	private void mockIFetchUser() {
		fetchUser = EasyMock.createMock(IFetchUser.class);
		fetchUser.getCurrentUserName();
		EasyMock.expectLastCall().andReturn(realName).anyTimes();
		fetchUser.getCurrentUserId();
		EasyMock.expectLastCall().andReturn(userId).anyTimes();
		EasyMock.replay(fetchUser);
	}

	/**
	 * mock IIdGen的接口
	 */
	private void mockIIdGen() {
		idGen = EasyMock.createMock(IIdGen.class);
		long currentTimeMillis = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			idGen.getId();
			EasyMock.expectLastCall().andReturn(currentTimeMillis++);
		}
		EasyMock.replay(idGen);
	}

	@Test(expectedExceptions = ParamException.class)
	public void testInsertNull() {
		operateDbDao.insert(null);
	}

	@Test
	public void testInsertSignle() {
		MockEntity4OperateDbDao entity = new MockEntity4OperateDbDao("测试用的");
		operateDbDao.insert(entity);
		assertEquals(entity.getCrId(), userId);
		assertEquals(entity.getCrName(), realName);
	}

	@Test
	public void testInsertWithUserId() {
		MockEntity4OperateDbDao entity = new MockEntity4OperateDbDao("测试用的");
		long outSideUserId = 92333L;
		entity.setCrId(outSideUserId);
		String outSideUserName = "外面来的";
		entity.setCrName(outSideUserName);
		Timestamp now = DateTimeUtil.nowDateTime();
		entity.setCrTime(now);
		operateDbDao.insert(entity);
		assertEquals(outSideUserId, entity.getCrId());
		assertEquals(outSideUserName, entity.getCrName());
		assertEquals(now, entity.getCrTime());
	}

	@Test
	public void testInsertArray() {
		MockEntity4OperateDbDao entity1 = new MockEntity4OperateDbDao("测试用的");
		MockEntity4OperateDbDao entity2 = new MockEntity4OperateDbDao("测试用的");
		MockEntity4OperateDbDao[] entities = new MockEntity4OperateDbDao[] {
				entity1, entity2 };
		operateDbDao.insert(entities);
		assertEquals(entity1.getCrId(), userId);
		assertEquals(entity1.getCrName(), realName);
		assertEquals(entity2.getCrId(), userId);
		assertEquals(entity2.getCrName(), realName);
	}

	@Test
	public void testInsertCollection() {
		MockEntity4OperateDbDao entity1 = new MockEntity4OperateDbDao("测试用的");
		MockEntity4OperateDbDao entity2 = new MockEntity4OperateDbDao("测试用的");
		Collection<MockEntity4OperateDbDao> list = CollectionUtil.list(entity1,
				entity2);
		operateDbDao.insert(list);
		assertEquals(entity1.getCrId(), userId);
		assertEquals(entity1.getCrName(), realName);
		assertEquals(entity2.getCrId(), userId);
		assertEquals(entity2.getCrName(), realName);
	}

	@Test(expectedExceptions = ParamException.class)
	public void testInsertMap() {
		operateDbDao.insert(MapUtil.map());
	}

	@Test(expectedExceptions = ParamException.class)
	public void testUpdateNull() {
		operateDbDao.update(null);
	}

	@Test
	public void testUpdateSignle() {
		MockEntity4OperateDbDao entity = new MockEntity4OperateDbDao("测试用的");
		operateDbDao.insert(entity);
		int zero = 0;
		assertEquals(entity.getMdId(), zero);
		assertNull(entity.getMdName());
		operateDbDao.update(entity);

		assertEquals(entity.getMdId(), userId);
		assertEquals(entity.getMdName(), realName);
		long updateUserId = 22277L;
		entity.setMdId(updateUserId);
		String updateName = "名称";
		entity.setMdName(updateName);
		Timestamp now = DateTimeUtil.nowDateTime();
		entity.setMdTime(now);
		operateDbDao.update(entity);
		assertEquals(updateUserId, entity.getMdId());
		assertEquals(updateName, entity.getMdName());
		assertEquals(now, entity.getMdTime());
	}

	@Test
	public void testUpdateArray() {
		MockEntity4OperateDbDao entity1 = new MockEntity4OperateDbDao("测试用的");
		MockEntity4OperateDbDao entity2 = new MockEntity4OperateDbDao("测试用的");
		MockEntity4OperateDbDao[] entities = new MockEntity4OperateDbDao[] {
				entity1, entity2 };
		operateDbDao.insert(entities);
		int zero = 0;
		assertEquals(entity1.getMdId(), zero);
		assertNull(entity1.getMdName());
		assertEquals(entity2.getMdId(), zero);
		assertNull(entity2.getMdName());
		operateDbDao.update(entities);
		assertEquals(entity1.getMdId(), userId);
		assertEquals(entity1.getMdName(), realName);
		assertEquals(entity2.getMdId(), userId);
		assertEquals(entity2.getCrName(), realName);
	}

	@Test
	public void testUpdateCollection() {
		MockEntity4OperateDbDao entity1 = new MockEntity4OperateDbDao("测试用的");
		MockEntity4OperateDbDao entity2 = new MockEntity4OperateDbDao("测试用的");
		Collection<MockEntity4OperateDbDao> entities = CollectionUtil.list(
				entity1, entity2);
		operateDbDao.insert(entities);
		int zero = 0;
		assertEquals(entity1.getMdId(), zero);
		assertNull(entity1.getMdName());
		assertEquals(entity2.getMdId(), zero);
		assertNull(entity2.getMdName());
		operateDbDao.update(entities);
		assertEquals(entity1.getMdId(), userId);
		assertEquals(entity1.getMdName(), realName);
		assertEquals(entity2.getMdId(), userId);
		assertEquals(entity2.getCrName(), realName);
	}

	@Test(expectedExceptions = ParamException.class)
	public void testUpdateMap() {
		operateDbDao.update(MapUtil.map());
	}
}
