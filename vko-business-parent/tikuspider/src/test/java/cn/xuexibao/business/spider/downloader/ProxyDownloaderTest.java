package cn.xuexibao.business.spider.downloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import us.codecraft.webmagic.selector.thread.CountableThreadPool;
import cn.vko.core.common.util.ExceptionUtil;
import cn.vko.core.common.util.RandomUtil;
import cn.vko.core.common.util.ThreadUtil;

public class ProxyDownloaderTest {
	protected CountableThreadPool threadPool;

	protected ExecutorService executorService;

	@BeforeTest
	public void init() {
		if (threadPool == null || threadPool.isShutdown()) {
			if (executorService != null && !executorService.isShutdown()) {
				threadPool = new CountableThreadPool(10, executorService);
			} else {
				threadPool = new CountableThreadPool(10);
			}
		}
	}

	@Test
	public void testEx() {
		init();
		final AtomicInteger size = new AtomicInteger(0);
		final AtomicInteger mainCount = new AtomicInteger(0);
		while (true) {
			final int num = mainCount.incrementAndGet();
			System.out.println("begin mainThread is " + num);
			threadPool.execute(new Runnable() {
				public void run() {
					int currentNum = size.incrementAndGet();
					System.out.println("begin mainThread is " + num
							+ "subThread is " + currentNum);
					if (num == 25) {
						throw ExceptionUtil.bEx("error");
					}
					ThreadUtil.sleep(RandomUtil.nextInt(5000));
					System.out.println("end mainThread is " + num
							+ "subThread is " + currentNum);
				}
			});
			System.out.println("end mainThread is " + num);
		}
	}
}
