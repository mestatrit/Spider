package cn.vko.business.spider.work;

import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.vko.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.vko.business.spider.scheduler.RedisFilterScheduler;
import cn.vko.business.spider.work.yitiku.YiTiKuMathWork;

public class YiTiKuWorkTest {
	private Ioc ioc;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("spider/config/"));
	}

	@Test
	public void testYiTiKuWork() throws Exception {
		MysqlPageModelSinglePipeline mm = ioc.get(
				MysqlPageModelSinglePipeline.class, "pipeline");
		RedisFilterScheduler rs = ioc.get(RedisFilterScheduler.class,
				"scheduler");
		YiTiKuWork work = new YiTiKuMathWork(rs, mm, true);
		work.startWork(1);
	}

}
