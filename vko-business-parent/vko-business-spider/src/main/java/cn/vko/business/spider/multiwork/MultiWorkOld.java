package cn.vko.business.spider.multiwork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Data;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.selector.thread.CountableThreadPool;
import cn.vko.business.spider.MultiOOSpider;
import cn.vko.business.spider.cookie.ICookieQueue;
import cn.vko.business.spider.pipeline.IMysqlPipeline;
import cn.vko.business.spider.proxy.IProxyQueue;
import cn.vko.business.spider.scheduler.FilterScheduler;

@Data
public abstract class MultiWorkOld implements Runnable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MultiWork.class);

	protected String uuid;
	protected String domain;
	protected int workThread = 1;
	protected int spiderThread = 1;
	protected int spiderInterval = 500;
	protected int maxSuccesTimes;
	protected FilterScheduler scheduler;
	protected IMysqlPipeline<?> pipeline;
	protected ICookieQueue cookieQueue;
	protected IProxyQueue proxyQueue;
	protected ReentrantLock newUrlLock = new ReentrantLock();
	protected Condition newUrlCondition = newUrlLock.newCondition();
	protected final AtomicInteger threadAlive = new AtomicInteger(0);
	protected final AtomicInteger status = new AtomicInteger(0);
	protected boolean exitWhenComplete = true;
	protected static final int STOP = 1;
	protected CountableThreadPool threadPool;

	protected ExecutorService executorService;

	public MultiWorkOld(String uuid, String domain, int workThread,
			int spiderThread, int maxSuccesTimes, FilterScheduler scheduler,
			IMysqlPipeline<?> pipeline) {
		super();
		this.uuid = uuid;
		this.domain = domain;
		this.workThread = workThread;
		this.spiderThread = spiderThread;
		this.maxSuccesTimes = maxSuccesTimes;
		this.scheduler = scheduler;
		this.pipeline = pipeline;
	}

	public void clearAll() {
		pipeline.clear(uuid);
		scheduler.clear(uuid);
	}

	public void clearCache() {
		scheduler.clear(uuid);
	}

	protected void init() {
		if (threadPool == null || threadPool.isShutdown()) {
			if (executorService != null && !executorService.isShutdown()) {
				threadPool = new CountableThreadPool(workThread,
						executorService);
			} else {
				threadPool = new CountableThreadPool(workThread);
			}
		}
		if (scheduler.isFilted(uuid)) {
			return;
		}
		if (scheduler.isScheduled(uuid)) {
			scheduler.addSchedulerUrl(uuid);
		}
		// pipeline.addUrl(uuid, scheduler);
		if (workThread <= 0) {
			workThread = 1;
		}
	}

	@SuppressWarnings("rawtypes")
	private MultiOOSpider<?> runOne() {
		HttpHost hh = null;
		if (proxyQueue != null) {
			hh = proxyQueue.popHost();
			logger.info("now host is-----------------------" + hh.toURI());
		}
		CookieStore cs = null;
		if (cookieQueue != null) {
			cs = cookieQueue.popCookie(hh);
		}
		MultiOOSpider s = initSpider(hh, cs);
		s.thread(spiderThread);
		s.setMaxSuccesTimes(maxSuccesTimes);
		s.setSpiderInterval(spiderInterval);
		s.run();
		return s;
	}

	@SuppressWarnings("rawtypes")
	protected abstract MultiOOSpider initSpider(HttpHost hh, CookieStore cs);

	public void run() {
		init();
		addDownloadUrl();
		int i = 0;
		while (!Thread.currentThread().isInterrupted()) {
			threadAlive.incrementAndGet();
			executorService.execute(new Runnable() {
				@SuppressWarnings("rawtypes")
				public void run() {
					MultiOOSpider s = null;
					try {
						s = runOne();
					} catch (Exception e) {
						logger.error("run one error", e);
						status.set(STOP);
					} finally {
						threadAlive.decrementAndGet();
						if (s != null) {
							if (logger.isInfoEnabled()) {
								logger.info("run() - MultiOOSpider stop status is " + s.getIntStatus() + " alive is " + threadAlive.get()); //$NON-NLS-1$
							}
							if (s.getIntStatus() == 2 && threadAlive.get() == 0) {
								status.set(STOP);
							}
						}
					}
				}
			});
		}
		if (logger.isInfoEnabled()) {
			logger.info("main run  - stop status is " + status.get() + " alive is " + threadAlive.get()); //$NON-NLS-1$
		}
		logger.info("end while" + i);
		if (status.get() == STOP && exitWhenComplete) {
			return;
		}

	}

	public void runAsync() {
		Thread thread = new Thread(this);
		thread.setDaemon(false);
		thread.start();
	}

	public abstract void addDownloadUrl();
}
