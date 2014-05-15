package cn.vko.business.spider.multiwork;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.vko.business.spider.scheduler.RedisFilterScheduler;
import cn.vko.core.db.dao.impl.DbDao;

public class JYFilterWorkTest {
	private Ioc ioc;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("spider/config/"));
	}

	@Test
	public void testWork() {
		// MysqlPageUpdatePipeline mm = ioc.get(MysqlPageUpdatePipeline.class,
		// "updatepl");
		// RedisFilterScheduler rs = ioc.get(RedisFilterScheduler.class,
		// "rejectsc");
		MysqlPageModelSinglePipeline mm = ioc.get(
				MysqlPageModelSinglePipeline.class, "pipeline");
		RedisFilterScheduler rs = ioc.get(RedisFilterScheduler.class,
				"scheduler");
		DbDao dao = ioc.get(DbDao.class, "dao");
		JYFilterWork work = new JYFilterWork(1, rs, mm, dao);
		work.run();
	}
}
