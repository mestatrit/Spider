package cn.vko.business.spider.multiwork;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;

import us.codecraft.webmagic.Site;
import cn.vko.business.spider.MultiOOSpider;
import cn.vko.business.spider.downloader.ProxyDownloader;
import cn.vko.business.spider.model.XKTiKu;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;

public class XKWork extends MultiWork {

	public XKWork(int workThread, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline) {
		super("tiku.zujuan.com", "zujuan.com", workThread, 10, 0, scheduler,
				pipeline);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected MultiOOSpider initSpider(HttpHost hh, CookieStore cs) {
		Site site = Site.me();
		MultiOOSpider spider = MultiOOSpider
				.create(site
						.addHeader(Site.HeaderConst.REFERER,
								"http://tiku.zujuan.com")
						.setDomain(getDomain())
						.setRetryTimes(3)
						.setHttpProxy(hh)
						.setSleepTime(1000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), XKTiKu.class);
		spider.setScheduler(scheduler);
		spider.setUUID(uuid);
		spider.setDownloader(new ProxyDownloader());
		return spider;
	}

	@Override
	public void addDownloadUrl() {
		scheduler.push(uuid, "http://tiku.zujuan.com");
		scheduler.push(uuid, new String[] {
				"http://tiku.zujuan.com/Search.aspx?bi=10&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=11&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=12&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=13&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=14&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=15&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=17&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=18&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=16&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=1&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=2&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=3&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=4&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=5&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=6&kw=0&pg=1",
				"ttp://tiku.zujuan.com/Search.aspx?bi=7&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=8&kw=0&pg=1",
				"http://tiku.zujuan.com/Search.aspx?bi=9&kw=0&pg=1" });
	}
}
