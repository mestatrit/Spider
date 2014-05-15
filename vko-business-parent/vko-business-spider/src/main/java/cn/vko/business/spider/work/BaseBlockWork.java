package cn.vko.business.spider.work;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Spider.Status;
import us.codecraft.webmagic.model.OOSpider;
import cn.vko.business.spider.BlockOOSpider;
import cn.vko.business.spider.downloader.ProxyDownloader;
import cn.vko.business.spider.downloader.ProxyPool;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.scheduler.FilterScheduler;
import cn.vko.core.common.util.ExceptionUtil;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseBlockWork extends BaseWork {
	private boolean needLoginCookie;
	private static final AtomicInteger STATUS = new AtomicInteger(1);
	private static final int MAX_GET_COOKIE_NUM = 20;
	private int getCookieNum = 0;
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(BaseBlockWork.class);

	public BaseBlockWork(String uuid, String domain, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline, boolean useProxy,
			boolean needLoginCookie) {
		super(uuid, domain, scheduler, pipeline, useProxy);
		this.needLoginCookie = needLoginCookie;
	}

	public BaseBlockWork(String domain, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline, boolean useProxy,
			boolean needLoginCookie) {
		this(domain, domain, scheduler, pipeline, useProxy, needLoginCookie);
	}

	@SuppressWarnings("rawtypes")
	private final BlockingQueue<OOSpider> QUEUE = new LinkedBlockingQueue<OOSpider>();
	private final AtomicInteger threadAlive = new AtomicInteger(0);

	@SuppressWarnings("rawtypes")
	public void startWork(int thread) {
		init();
		ExecutorService ex = Executors.newFixedThreadPool(thread);
		CookieStore cookieStore = getCookieStore();
		HttpHost proxy = null;
		while (true) {
			if (threadAlive.get() > thread) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {

				}
				continue;
			}

			try {
				proxy = ProxyPool.popHost();
			} catch (Exception e) {

			}
			if (proxy == null) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {

				}
				continue;
			}

			OOSpider spider = initSpider(1, proxy, cookieStore);
			if (spider == null) {
				continue;
			}
			QUEUE.add(spider);
			ex.execute(new Runnable() {
				@Override
				public void run() {
					threadAlive.incrementAndGet();
					OOSpider spider = QUEUE.poll();
					spider.run();
					if (spider instanceof BlockOOSpider) {
						BlockOOSpider blockOOSpider = (BlockOOSpider) spider;
						int status = blockOOSpider.getIntStatus();
						if (status == 2 && threadAlive.get() == 1) {
							STATUS.set(-1);
							return;
						}
					} else {
						if (spider.getStatus().equals(Status.Stopped)
								&& threadAlive.get() == 1) {
							STATUS.set(-1);
							return;
						}
					}
					threadAlive.decrementAndGet();
				}
			});
			if (STATUS.get() == -1) {
				return;
			}
		}
	}

	private CookieStore getCookieStore() {
		HttpHost proxy = null;
		try {
			proxy = ProxyPool.popHost();
		} catch (Exception e) {

		}
		CookieStore cookieStore = null;
		if (needLoginCookie) {
			cookieStore = getCookie(proxy);
			if (cookieStore == null) {
				getCookieNum++;
				if (getCookieNum > MAX_GET_COOKIE_NUM) {
					throw ExceptionUtil.bEx("get cookie error time > 10");
				}
				return getCookieStore();
			}
			logger.info("fetch login cookie is" + cookieStore.toString());
			return cookieStore;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private OOSpider initSpider(int thread, HttpHost proxy,
			CookieStore cookieStore) {
		OOSpider spider = null;
		try {
			if (cookieStore == null) {
				spider = createSpider();
			} else {
				spider = createSpider(cookieStore);
			}
		} catch (Exception e) {
			logger.error("initSpider error", e); //$NON-NLS-1$
			return null;
		}
		if (spider == null) {
			throw ExceptionUtil
					.bEx("spider create null error,please check BaseBlockWork subClass!");
		}
		if (isUseProxy()) {
			ProxyDownloader pd = new ProxyDownloader();
			pd.setThread(thread);
			spider.setDownloader(pd);
			spider.getSite().setHttpProxy(proxy);
		}
		spider.setUUID(uuid);
		spider.setScheduler(scheduler);
		spider.thread(thread);
		addUrl(spider);
		return spider;
	}

	public void startWorkAsync(int thread) {
		throw ExceptionUtil.bEx("not impl");
	}

	protected abstract CookieStore getCookie(HttpHost proxy);

	@SuppressWarnings("rawtypes")
	protected abstract OOSpider createSpider() throws Exception;

	@SuppressWarnings("rawtypes")
	protected abstract OOSpider createSpider(CookieStore cookieStore)
			throws Exception;

	@SuppressWarnings("rawtypes")
	protected abstract void addUrl(OOSpider spider);
}
