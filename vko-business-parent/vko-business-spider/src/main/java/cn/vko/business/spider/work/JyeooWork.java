package cn.vko.business.spider.work;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.BlockOOSpider;
import cn.vko.business.spider.model.Jyeoo;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;

public class JyeooWork extends BaseBlockWork {

	public JyeooWork(FilterScheduler scheduler, IMysqlPipeline<?> pipeline,
			boolean useProxy) {
		super("jyeoo.com", scheduler, pipeline, useProxy, false);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected OOSpider createSpider() {
		OOSpider spider = BlockOOSpider
				.create(Site
						.me()
						.addHeader(Site.HeaderConst.REFERER,
								"http://www.baidu.com/spider.html")
						.setDomain(getDomain())
						.setRetryTimes(3)
						.setSleepTime(1000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), Jyeoo.class);
		return spider;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void addUrl(OOSpider spider) {
		spider.addUrl("http://www.jyeoo.com");
	}

	@Override
	protected CookieStore getCookie(HttpHost proxy) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected OOSpider createSpider(CookieStore cookieStore) throws Exception {
		return createSpider();
	}
}
