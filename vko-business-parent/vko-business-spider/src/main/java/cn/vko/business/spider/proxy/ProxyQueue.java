package cn.vko.business.spider.proxy;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Data;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class ProxyQueue implements IProxyQueue {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ProxyQueue.class);
	private String checkUrl;
	private int fetchNum;
	private BlockingQueue<HttpHost> queue = new LinkedBlockingQueue<HttpHost>();

	public synchronized HttpHost popHost() {
		if (queue.size() == 0) {
			fetchProxyBlock();
		}
		if (queue.size() == 0) {
			logger.info("proxy ip is poped local ");
			return null;
		}
		HttpHost one = queue.poll();
		logger.info("proxy ip is poped " + one.toHostString());
		return one;
	}

	private void fetchProxyBlock() {
		List<HttpHost> result = ProxyUtil.downloadProxy(fetchNum);
		for (HttpHost one : result) {
			if (ProxyUtil.isCanUse(one, checkUrl)) {
				try {
					queue.put(one);
				} catch (InterruptedException e) {
				}
			}
		}
		if (queue.size() == 0) {
			fetchProxyBlock();
		}
	}
}
