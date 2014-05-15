package cn.vko.business.spider.multiwork;

import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Site;
import cn.vko.business.spider.MultiOOSpider;
import cn.vko.business.spider.downloader.ProxyDownloader;
import cn.vko.business.spider.model.WuYouTiku;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;
import cn.vko.core.common.util.CollectionUtil;
import cn.vko.core.common.util.Util;

public class WuYouWork extends MultiWork {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WuYouWork.class);

	public WuYouWork(int workThread, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline) {
		super("tiku.ks5u.cn", "tiku.ks5u.cn", workThread, 5, 0, scheduler,
				pipeline);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected MultiOOSpider initSpider(HttpHost hh, CookieStore cs) {
		Site site = Site.me();
		MultiOOSpider spider = MultiOOSpider
				.create(site
						.addHeader(Site.HeaderConst.REFERER,
								"http://tiku.ks5u.cn/")
						.setDomain(getDomain())
						.setRetryTimes(3)
						.setSleepTime(1000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), WuYouTiku.class);
		spider.setScheduler(scheduler);
		spider.setUUID(uuid);
		spider.setDownloader(new ProxyDownloader());
		return spider;
	}

	@Override
	public void addDownloadUrl() {

	}

	public void addDownloadUrlOnce() {
		List<String> batch = CollectionUtil.list();
		for (int i = 1001; i <= 1176980; i++) {
			batch.add("http://tiku.ks5u.cn/home.aspx?mod=showpaper&ac=base&op=base&PaperId="
					+ i);
			if (i % 1000 == 0) {
				scheduler.push(uuid, CollectionUtil.collection2array(batch));
				batch.clear();
				if (logger.isInfoEnabled()) {
					logger.info("addDownloadUrl i is " + i); //$NON-NLS-1$
				}
			}
		}
		if (Util.isEmpty(batch)) {
			scheduler.push(uuid, CollectionUtil.collection2array(batch));
		}
	}
}
