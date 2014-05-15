package cn.vko.business.spider.work;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.model.MoFangGe;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;

public class MoFangGeWork extends BaseWork {

	public MoFangGeWork(FilterScheduler scheduler, IMysqlPipeline<?> pipeline,
			boolean useProxy) {
		super("mofangge.com", scheduler, pipeline, useProxy);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected OOSpider createSpider() {
		OOSpider spider = OOSpider
				.create(Site
						.me()
						.addHeader("Connection", "close")
						.addHeader(Site.HeaderConst.REFERER,
								"http://www.mofangge.com")
						.setDomain(getDomain())
						.setRetryTimes(3)
						.setSleepTime(3000)
						.setTimeOut(20000)
						.setUseGzip(true)
						.setUserAgent(
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36"),
						getPipeline(), MoFangGe.class);

		return spider;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void addUrl(OOSpider spider) {
		spider.addUrl("http://www.mofangge.com");
	}
}
