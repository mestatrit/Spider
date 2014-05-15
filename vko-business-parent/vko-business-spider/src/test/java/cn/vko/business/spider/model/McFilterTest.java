package cn.vko.business.spider.model;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

public class McFilterTest {
	private XMemcachedClientBuilder pool = new XMemcachedClientBuilder(
			AddrUtil.getAddresses("192.168.192.132:12345"));

	@Test
	public void testAdd() throws Exception {
		MemcachedClient mc = pool.build();
		System.out.println(mc.add("test", 0, "10000|0.001", 1000));
		System.out.println(mc.add("test", 0, "10000|0.001", 1000));
		mc.set("test", 0, "a");
		mc.set("test", 0, "b");
		mc.set("test", 0, "c");
		Assert.assertEquals("1", mc.get("test|a"));
		Assert.assertNull(mc.get("test|d"));
		System.out.println(mc.getStats(1000000));
		System.out.println(mc.getStatsByItem("blooms", 1000000));
		System.out.println(mc.getStatsByItem("bloom test", 1000000));
		System.out.println(mc.getStatsByItem("bloom test2", 1000000));
		mc.delete("test");
		mc.add("test", 0, "10000|0.001", 1000);
		Assert.assertNull(mc.get("test|a"));
		Assert.assertNull(mc.get("test|d"));
		mc.delete("test");
	}
}
