package cn.vko.business.spider.urlfilter.impl;

import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;

public class RedisBloomFilterTest {

	@Test
	public void testCanPush() throws Exception {
		RedisBloomFilter rb = new RedisBloomFilter("192.168.1.13", 6379);
		Request r = new Request();
		Task t = new Task() {
			@Override
			public String getUUID() {
				return "testrb";
			}

			@Override
			public Site getSite() {
				return null;
			}
		};
		rb.clear(t.getUUID());
		r.setUrl("eed35048-8e88-4f9e-a99f-f9f88782d5a4");
		Assert.assertTrue(rb.canPush(r, t));
		r.setUrl("d8a968c8-77a1-44d5-a27b-38d54b5ee64c");
		Assert.assertTrue(rb.canPush(r, t));
		r.setUrl("01b126a1-d94c-440d-b4ee-7d6ddbf6250c");
		Assert.assertTrue(rb.canPush(r, t));
		r.setUrl("259e7445-d56a-42de-8a00-8a1606f02b3e");
		Assert.assertTrue(rb.canPush(r, t));
		r.setUrl("00a46505-8801-414e-a10f-859d6084bb8b");
		Assert.assertTrue(rb.canPush(r, t));
		r.setUrl("98d6ccf7-2589-4d07-a67d-3de1db2e3a6c");
		Assert.assertTrue(rb.canPush(r, t));
		r.setUrl("eed35048-8e88-4f9e-a99f-f9f88782d5a4");
		Assert.assertTrue(!rb.canPush(r, t));
		while (true) {
			r.setUrl(UUID.randomUUID().toString());
			boolean can = rb.canPush(r, t);
			if (!can) {
				System.out.println(r.getUrl() + ":"
						+ System.currentTimeMillis());
			}
		}
	}
}
