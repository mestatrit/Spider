/**
 * TestRecordUtil.java
 * cn.vko.core.db.util
 * Copyright (c) 2013, 北京微课创景教育科技有限公司版权所有.
 */

package cn.vko.core.db.util;

import static org.testng.Assert.*;

import java.sql.Date;

import org.easymock.EasyMock;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.core.common.util.DateTimeUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.IIdGen;
import cn.vko.core.db.dao.impl.DbDao;
import cn.vko.core.db.mock.entity.MockEntity4Record;

/**
 * RecordUtil单元测试类
 * 
 * @author 庄君祥
 * @Date 2013-12-10
 */
public class TestRecordUtil {
	private IDbDao dbDao;
	private IIdGen idGen;
	private NutDao nutDao;
	private Ioc ioc;
	private double price = 1.2;
	private boolean valid = true;
	private Date date = DateTimeUtil.nowDate();
	private long id;
	private Record record;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("db/config/dao.js"));
		nutDao = ioc.get(NutDao.class, "nutDao");
		mockIIdGen();
		dbDao = new DbDao(nutDao, idGen);
		nutDao.create(MockEntity4Record.class, true);
		MockEntity4Record entity = dbDao.insert(new MockEntity4Record(date,
				price, valid));
		id = entity.getId();
		record = dbDao.fetch(Sqls
				.create("select * from mock_entity_4_record where id=" + id));
	}

	/**
	 * mock IIdGen的接口
	 */
	private void mockIIdGen() {
		idGen = EasyMock.createMock(IIdGen.class);
		long currentTimeMillis = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			idGen.getId();
			EasyMock.expectLastCall().andReturn(currentTimeMillis++);
		}
		EasyMock.replay(idGen);
	}

	@Test
	public void testGetLong() {
		assertEquals(RecordUtil.getLong(record, "id"), id);
	}

	@Test
	public void testGetLongIll() {
		assertEquals(RecordUtil.getLong(new Record(), "id"), -1L);
	}

	// @Test
	public void testGetDate() {
		assertEquals(RecordUtil.getDate(record, "date").getTime(),
				date.getTime());
	}

	@Test
	public void testGetDateIll() {
		assertNull(RecordUtil.getDate(new Record(), "date"));
	}

	@Test
	public void testGetBoolean() {
		assertTrue(RecordUtil.getBoolean(record, "valid") == valid);
	}

	@Test
	public void testGetBooleanIll() {
		assertFalse(RecordUtil.getBoolean(new Record(), "valid"));
	}

	@Test
	public void testGetDouble() {
		assertEquals(RecordUtil.getDouble(record, "price"), price);
	}

	@Test
	public void testGetDoubleIll() {
		assertEquals(RecordUtil.getDouble(new Record(), "price"), 0D);
	}

}
