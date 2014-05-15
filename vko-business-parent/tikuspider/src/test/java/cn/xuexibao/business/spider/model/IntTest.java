package cn.xuexibao.business.spider.model;

import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;
import cn.xuexibao.business.spider.downloader.ProxyDownloader;
import cn.xuexibao.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.xuexibao.business.spider.scheduler.RedisFilterScheduler;

public class IntTest {
	private IDbDao dbDao;
	private NutDao nutDao;
	private Ioc ioc;

	@BeforeClass
	public void init() {
		ioc = new NutIoc(new JsonLoader("spider/config/"));
		nutDao = ioc.get(NutDao.class, "nut");
		dbDao = new DbDao(nutDao, null);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testAll() {
		boolean isClear = false;
		MysqlPageModelSinglePipeline mm = ioc.get(
				MysqlPageModelSinglePipeline.class, "pipeline");
		RedisFilterScheduler rs = ioc.get(RedisFilterScheduler.class,
				"scheduler");
		ProxyDownloader pd = new ProxyDownloader();
		pd.setThread(30);
		String uuid = "jyeoo.com";
		if (isClear) {
			// nutDao.create(SpiderExam.class, true);
			rs.clear(uuid);
			mm.clear(uuid);
			return;
		} else {
			// rs.clear(uuid);
			// addUrl(mb, uuid);
		}
		OOSpider spider = OOSpider
				.create(Site
						.me()
						.addHeader("Connection", "close")
						.addHeader(Site.HeaderConst.REFERER, uuid)
						.setDomain("jyeoo.com")
						.setRetryTimes(3)
						.setSleepTime(3000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						mm, Jyeoo2.class);

		spider.thread(30).setScheduler(rs).setDownloader(pd);
		for (int i = 1; i < 1876; i++) {
			spider.addUrl("http://www.jyeoo.com/math/ques/search?p=" + i);
		}
		spider.run();
	}

}
