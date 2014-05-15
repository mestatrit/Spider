package cn.vko.business.spider.uucode;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.business.spider.entity.SpiderUser;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;

public class ZuoYeBaoRegisterTest {
	private IDbDao dbDao;
	private NutDao nutDao;
	private Ioc ioc;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("spider/config/"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
		nutDao.create(SpiderUser.class, false);
	}

	@Test
	public void testRegisterOne() throws Exception {
		ZuoYeBaoRegister zr = new ZuoYeBaoRegister();
		zr.setDbDao(dbDao);
		zr.init();
		zr.registerOne();
	}

}
