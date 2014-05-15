package cn.vko.business.spider.multiwork;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.vko.business.spider.scheduler.RedisFilterScheduler;

public class YiWorkTest {
	private Ioc ioc;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("spider/config/"));
	}

	@Test
	public void testWork() {
		MysqlPageModelSinglePipeline mm = ioc.get(
				MysqlPageModelSinglePipeline.class, "pipeline");
		RedisFilterScheduler rs = ioc.get(RedisFilterScheduler.class,
				"scheduler");
		YiWork work = new YiWork(10, rs, mm);
		work.run();
	}
}
