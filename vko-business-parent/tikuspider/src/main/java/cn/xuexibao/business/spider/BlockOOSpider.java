package cn.xuexibao.business.spider;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class BlockOOSpider<T> extends OOSpider<T> {
	private final AtomicInteger threadAlive = new AtomicInteger(0);
	private final AtomicInteger errorNum = new AtomicInteger(0);
	private final AtomicLong successCount = new AtomicLong(0);

	private ReentrantLock newUrlLock = new ReentrantLock();
	private Condition newUrlCondition = newUrlLock.newCondition();

	public static final int MAX_ERROR_NUM = 3;
	private final static int STAT_ERROR = 3;
	private int maxSuccesTimes;

	public BlockOOSpider(PageProcessor pageProcessor) {
		super(pageProcessor);
	}

	@SuppressWarnings("rawtypes")
	public BlockOOSpider(Site site, PageModelPipeline pageModelPipeline,
			Class... pageModels) {
		super(site, pageModelPipeline, pageModels);
	}

	@SuppressWarnings("rawtypes")
	public static BlockOOSpider create(Site site, Class... pageModels) {
		return new BlockOOSpider(site, null, pageModels);
	}

	@SuppressWarnings("rawtypes")
	public static BlockOOSpider create(Site site,
			PageModelPipeline pageModelPipeline, Class... pageModels) {
		return new BlockOOSpider(site, pageModelPipeline, pageModels);
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
				try {
					processRequest(requestFinal);
				} catch (Exception e) {
					logger.error("block download " + requestFinal + " error");
					errorNum.incrementAndGet();
					if (errorNum.get() >= MAX_ERROR_NUM) {
						stat.set(STAT_ERROR);
						if (destroyWhenExit) {
							close();
						}
						return;
					}
				} finally {
					threadAlive.decrementAndGet();
					successCount.incrementAndGet();
					signalNewUrl();
				}
				if (maxSuccesTimes <= 0) {
					continue;
				}
				if (successCount.get() >= maxSuccesTimes) {
					stat.set(STAT_STOPPED);
					// release some resources
					if (destroyWhenExit) {
						close();
					}
				}
			}
		}
		stat.set(STAT_STOPPED);
		// release some resources
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

}
