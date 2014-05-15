package cn.xuexibao.business.spider.multiwork;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.xuexibao.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.xuexibao.business.spider.scheduler.RedisFilterScheduler;

public class WuYouWorkTest {
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
		WuYouWork work = new WuYouWork(2, rs, mm);
		work.run();
	}

}
