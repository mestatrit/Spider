package cn.vko.business.spider;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import cn.vko.business.spider.success.ISuccessChecker;
import cn.vko.core.common.util.ThreadUtil;

public class MultiOOSpider<T> extends OOSpider<T> {
	private final AtomicInteger threadAlive = new AtomicInteger(0);
	private final AtomicInteger errorNum = new AtomicInteger(0);
	protected final AtomicLong successCount = new AtomicLong(0);

	private ReentrantLock newUrlLock = new ReentrantLock();
	private Condition newUrlCondition = newUrlLock.newCondition();

	public static final int MAX_ERROR_NUM = 10;
	public final static int STAT_ERROR = 3;
	public final static int STAT_SUCCESS = 4;
	public final static int STAT_IPFORBIDDEN = 5;
	private int maxSuccesTimes;
	private int spiderInterval;
	private ISuccessChecker successChecker;

	public MultiOOSpider(PageProcessor pageProcessor) {
		super(pageProcessor);
	}

	@SuppressWarnings("rawtypes")
	public MultiOOSpider(Site site, PageModelPipeline pageModelPipeline,
			Class... pageModels) {
		super(site, pageModelPipeline, pageModels);
	}

	@SuppressWarnings("rawtypes")
	public static MultiOOSpider create(Site site, Class... pageModels) {
		return new MultiOOSpider(site, null, pageModels);
	}

	@SuppressWarnings("rawtypes")
	public static MultiOOSpider create(Site site,
			PageModelPipeline pageModelPipeline, Class... pageModels) {
		return new MultiOOSpider(site, pageModelPipeline, pageModels);
	}

	@Override
	public void run() {
		checkRunningStat();
		initComponent();
		logger.info("Spider " + getUUID() + " started!");
		while (!Thread.currentThread().isInterrupted()
				&& stat.get() == STAT_RUNNING) {
			Request request = scheduler.poll(this);
			if (request == null) {
				if (threadAlive.get() == 0 && exitWhenComplete) {
					break;
				}
				// wait until new url added
				waitNewUrl();
			} else {
				final Request requestFinal = request;
				threadAlive.incrementAndGet();
				ThreadUtil.sleep(spiderInterval);
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							processRequest(requestFinal);
							addSuccess(requestFinal);
							// } catch (IpFibbidenException e) {
							// stat.set(STAT_IPFORBIDDEN);
						} catch (Exception e) {
							logger.error("download " + requestFinal + " error",
									e);
							errorNum.incrementAndGet();
							if (errorNum.get() >= MAX_ERROR_NUM) {
								stat.set(STAT_ERROR);
							}
						} finally {
							threadAlive.decrementAndGet();
							signalNewUrl();
						}
					}
				});
				if (maxSuccesTimes <= 0) {
					continue;
				}
				if (successCount.get() >= maxSuccesTimes) {
					stat.set(STAT_SUCCESS);
				}
			}
		}
		if (stat.get() == STAT_ERROR || stat.get() == STAT_SUCCESS
				|| stat.get() == STAT_IPFORBIDDEN) {
			destory();
			return;
		}

		stat.set(STAT_STOPPED);
		// release some resources
		destory();
	}

	private void destory() {
		if (destroyWhenExit) {
			close();
		}
	}

	private void checkRunningStat() {
		while (true) {
			int statNow = stat.get();
			if (statNow == STAT_RUNNING) {
				throw new IllegalStateException("Spider is already running!");
			}
			if (stat.compareAndSet(statNow, STAT_RUNNING)) {
				break;
			}
		}
	}

	private void waitNewUrl() {
		try {
			newUrlLock.lock();
			// double check
			if (threadAlive.get() == 0 && exitWhenComplete) {
				return;
			}
			try {
				newUrlCondition.await();
			} catch (InterruptedException e) {
			}
		} finally {
			newUrlLock.unlock();
		}
	}

	private void signalNewUrl() {
		try {
			newUrlLock.lock();
			newUrlCondition.signalAll();
		} finally {
			newUrlLock.unlock();
		}
	}

	public int getIntStatus() {
		return stat.get();
	}

	public int getMaxSuccesTimes() {
		return maxSuccesTimes;
	}

	public void setMaxSuccesTimes(int maxSuccesTimes) {
		this.maxSuccesTimes = maxSuccesTimes;
	}

	public int getSpiderInterval() {
		return spiderInterval;
	}

	public void setSpiderInterval(int spiderInterval) {
		this.spiderInterval = spiderInterval;
	}

	public ISuccessChecker getSuccessChecker() {
		return successChecker;
	}

	public void setSuccessChecker(ISuccessChecker successChecker) {
		this.successChecker = successChecker;
	}

	public void addSuccess(Request request) {
		if (successChecker == null) {
			successCount.incrementAndGet();
			return;
		}
		if (successChecker.check(request)) {
			successCount.incrementAndGet();
		}
	}
}
