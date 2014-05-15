package cn.xuexibao.business.spider.login;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.core.db.dao.impl.DbDao;
import cn.xuexibao.business.spider.entity.SpiderReRouter;

public class RouterReconnectionTest {
	private Ioc ioc;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("spider/config/"));
		NutDao nutz = ioc.get(NutDao.class, "nut");
		nutz.create(SpiderReRouter.class, false);
	}

	@Test
	public void testReconnection() throws Exception {
		DbDao dao = ioc.get(DbDao.class, "dao");
		RouterReconnection.reconnection(dao, "test");
	}
}
