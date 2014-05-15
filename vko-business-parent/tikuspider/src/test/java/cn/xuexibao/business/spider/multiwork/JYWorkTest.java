package cn.xuexibao.business.spider.multiwork;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.xuexibao.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.xuexibao.business.spider.scheduler.RedisFilterScheduler;

public class JYWorkTest {

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
		JYWork work = new JYWork(2, rs, mm);
		work.run();
	}
}
