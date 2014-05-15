package cn.vko.business.spider.multiwork;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import us.codecraft.webmagic.Site;
import cn.vko.business.spider.MultiOOSpider;
import cn.vko.business.spider.downloader.ProxyDownloader;
import cn.vko.business.spider.model.Jyeoo;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.proxy.ProxyQueue;
import cn.vko.business.spider.scheduler.FilterScheduler;

public class JYWork extends MultiWork {

	public JYWork(int workThread, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline) {
		super("all.jyeoo.com", "jyeoo.com", workThread, 10, 0, scheduler,
				pipeline);
		ProxyQueue pq = new ProxyQueue();
		pq.setFetchNum(2);
		pq.setCheckUrl("http://www.jyeoo.com");
		this.setProxyQueue(pq);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected MultiOOSpider initSpider(HttpHost hh, CookieStore cs) {
		Site site = Site.me();
		if (cs != null) {
			List<Cookie> cookies = cs.getCookies();
			for (Cookie cookie : cookies) {
				site.addCookie(cookie.getName(), cookie.getValue());
			}
		}
		MultiOOSpider spider = MultiOOSpider
				.create(site
						.addHeader(Site.HeaderConst.REFERER,
								"http://www.jyeoo.com")
						.setDomain(getDomain())
						.setRetryTimes(30)
						.setHttpProxy(hh)
						.setSleepTime(1000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), Jyeoo.class);
		spider.setScheduler(scheduler);
		spider.setUUID(uuid);
		spider.setDownloader(new ProxyDownloader());
		return spider;
	}

	@Override
	public void addDownloadUrl() {
		scheduler.push(uuid, "http://www.jyeoo.com");
	}

}
