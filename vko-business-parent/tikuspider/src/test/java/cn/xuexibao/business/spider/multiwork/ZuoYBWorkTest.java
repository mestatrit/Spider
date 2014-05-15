package cn.xuexibao.business.spider.multiwork;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.core.common.util.ThreadUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;
import cn.xuexibao.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.xuexibao.business.spider.scheduler.RedisFilterScheduler;

public class ZuoYBWorkTest {
	private Ioc ioc;
	private NutDao nutDao;
	private IDbDao dbDao;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("spider/config/"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
	}

	@Test
	public void testInitSpider() throws Exception {
		MysqlPageModelSinglePipeline mm = ioc.get(
				MysqlPageModelSinglePipeline.class, "pipeline");
		RedisFilterScheduler rs = ioc.get(RedisFilterScheduler.class,
				"scheduler");
		ZuoYBWork work = new ZuoYBWork(10, rs, mm, dbDao);
		while (true) {
			work.run();
			ThreadUtil.sleep(5000);
		}
	}

}
