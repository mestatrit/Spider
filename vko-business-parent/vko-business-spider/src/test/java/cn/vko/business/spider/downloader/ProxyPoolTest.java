package cn.vko.business.spider.downloader;

import org.apache.http.HttpHost;
import org.testng.annotations.Test;

public class ProxyPoolTest {

	@Test
	public void testCheckPool() throws Exception {
		ProxyPool.checkPool();
		System.in.read();
	}

	@Test
	public void testIsCanUse() throws Exception {
		HttpHost host = ProxyPool.popHost();
		System.out.print("------------------------------"
				+ ProxyPool.isCanUse(host));
	}

}
