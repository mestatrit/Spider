package cn.xuexibao.business.spider.multiwork;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import us.codecraft.webmagic.Site;
import cn.xuexibao.business.spider.MultiOOSpider;
import cn.xuexibao.business.spider.downloader.ProxyDownloader;
import cn.xuexibao.business.spider.model.MoFangGe;
import cn.xuexibao.business.spider.pipeline.IMysqlPipeline;
import cn.xuexibao.business.spider.scheduler.FilterScheduler;

public class MfgWork extends MultiWork {

	public MfgWork(int workThread, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline) {
		super("mofangge.com", "mofangge.com", workThread, 10, 0, scheduler,
				pipeline);
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
								"http://www.mofangge.com")
						.setDomain(getDomain())
						.setRetryTimes(20)
						.setHttpProxy(hh)
						.setSleepTime(1000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), MoFangGe.class);
		spider.setScheduler(scheduler);
		spider.setUUID(uuid);
		spider.setDownloader(new ProxyDownloader());
		return spider;
	}

	@Override
	public void addDownloadUrl() {
		scheduler.push(uuid, "http://www.mofangge.com");
	}
}
