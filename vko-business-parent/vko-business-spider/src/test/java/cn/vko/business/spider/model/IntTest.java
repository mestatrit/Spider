package cn.vko.business.spider.model;

import java.util.List;

import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.downloader.ProxyDownloader;
import cn.vko.business.spider.pipeline.MysqlPageModelSinglePipeline;
import cn.vko.business.spider.scheduler.RedisFilterScheduler;
import cn.vko.business.spider.urlfilter.impl.McBloomFilter;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.db.dao.IDbDao;
import cn.vko.core.db.dao.impl.DbDao;
import cn.vko.core.db.util.DbSqlUtil;

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

	private void addUrl(McBloomFilter mb, String uuid) {
		Pager p = new Pager();
		p.setPageSize(1000);
		List<String> s = CollectionUtil.list();

		do {
			Sql sql = Sqls
					.create("select url from spider_exam where domain=@domain");
			sql.params().set("domain", uuid);
			s = DbSqlUtil.queryStringList(dbDao, sql, p);
			mb.addUrl(uuid, s);
			p.setPageNumber(p.getPageNumber() + 1);
			System.out.println(p.getPageNumber());
		} while (s.size() == p.getPageSize());
	}
}
