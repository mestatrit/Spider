package cn.xuexibao.business.spider.multiwork;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import us.codecraft.webmagic.Site;
import cn.xuexibao.business.spider.MultiOOSpider;
import cn.xuexibao.business.spider.cookie.YiCookieQueue;
import cn.xuexibao.business.spider.downloader.ProxyDownloader;
import cn.xuexibao.business.spider.model.yitiku.YiTiKuChemical;
import cn.xuexibao.business.spider.pipeline.IMysqlPipeline;
import cn.xuexibao.business.spider.proxy.ProxyQueue;
import cn.xuexibao.business.spider.scheduler.FilterScheduler;

public class YiWork extends MultiWork {

	public YiWork(int spiderThread, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline) {
		super("ch.yitiku.cn", "yitiku.cn", 1, spiderThread, 0, scheduler,
				pipeline);
		this.setCookieQueue(new YiCookieQueue());
		ProxyQueue pq = new ProxyQueue();
		pq.setFetchNum(2);
		pq.setCheckUrl("http://www.yitiku.cn");
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
								"http://www.yitiku.cn")
						.setDomain(getDomain())
						.setRetryTimes(3)
						.setHttpProxy(hh)
						.setSleepTime(1000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), YiTiKuChemical.class);
		spider.setScheduler(scheduler);
		spider.setUUID(uuid);
		spider.setDownloader(new ProxyDownloader());
		return spider;
	}

	@Override
	public void addDownloadUrl() {
		scheduler.push(uuid, "http://www.yitiku.cn/tiku/huaxue");
	}
}
