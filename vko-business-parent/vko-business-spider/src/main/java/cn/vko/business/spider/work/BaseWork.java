package cn.vko.business.spider.work;

import lombok.Data;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.downloader.ProxyDownloader;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;
import cn.vko.core.common.util.ExceptionUtil;

@Data
public abstract class BaseWork {
	public BaseWork(String uuid, String domain, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline, boolean useProxy) {
		super();
		this.uuid = uuid;
		this.domain = domain;
		this.scheduler = scheduler;
		this.pipeline = pipeline;
		this.useProxy = useProxy;
	}

	public BaseWork(String domain, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline, boolean useProxy) {
		this(domain, domain, scheduler, pipeline, useProxy);
	}

	protected String uuid;
	protected String domain;
	protected FilterScheduler scheduler;
	protected IMysqlPipeline<?> pipeline;
	protected boolean useProxy;

	public void clearAll() {
		pipeline.clear(uuid);
		scheduler.clear(uuid);
	}

	public void clearCache() {
		scheduler.clear(uuid);
	}

	@SuppressWarnings("rawtypes")
	public void startWork(int thread) {
		init();
		OOSpider spider = initSpider(thread);
		spider.run();
	}

	@SuppressWarnings("rawtypes")
	private OOSpider initSpider(int thread) {
		OOSpider spider = null;
		try {
			spider = createSpider();
		} catch (Exception e) {
			throw ExceptionUtil.bEx("create spider error", e);
		}
		if (useProxy) {
			ProxyDownloader pd = new ProxyDownloader();
			pd.setThread(thread);
			spider.setDownloader(pd);
		}
		spider.setUUID(uuid);
		spider.setScheduler(scheduler);
		spider.thread(thread);
		addUrl(spider);
		return spider;
	}

	protected void init() {
		if (scheduler.isFilted(uuid)) {
			return;
		}
		if (scheduler.isScheduled(uuid)) {
			scheduler.addSchedulerUrl(uuid);
		}
		pipeline.addUrl(uuid, scheduler);
	}

	@SuppressWarnings("rawtypes")
	public void startWorkAsync(int thread) {
		OOSpider spider = initSpider(thread);
		spider.runAsync();
	}

	@SuppressWarnings("rawtypes")
	protected abstract OOSpider createSpider() throws Exception;

	@SuppressWarnings("rawtypes")
	protected abstract void addUrl(OOSpider spider);
}
